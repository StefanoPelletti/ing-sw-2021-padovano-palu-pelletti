package it.polimi.ingsw.server.model;


import it.polimi.ingsw.networking.message.updateMessages.playerUpdate.MSG_UPD_WarehouseDepot;
import it.polimi.ingsw.server.model.enumerators.Resource;
import it.polimi.ingsw.server.utils.A;
import it.polimi.ingsw.server.utils.ModelObservable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class WarehouseDepot extends ModelObservable {

    private final Resource[] shelf2;
    private final Resource[] shelf3;
    private Resource shelf1;

    /**
     * CONSTRUCTOR
     * Creates an empty WarehouseDepot with 3 shelves (3 arrays), all filled with Resource.NONE.
     * Shelf1: Resource.NONE (size 1);
     * Shelf2: Resource.NONE Resource.NONE (size 2);
     * Shelf3: Resource.NONE Resource.NONE Resource.NONE (size 3).
     */
    public WarehouseDepot() {
        this.shelf1 = Resource.NONE;
        this.shelf2 = new Resource[2];
        this.shelf3 = new Resource[3];
        for (int i = 0; i < 3; i++) {
            if (i < 2) shelf2[i] = Resource.NONE;
            shelf3[i] = Resource.NONE;
        }
    }

    /**
     * Given 3 shelves (respectively of sizes 1, 2 and 3), validates that the following rules are met:
     * <ul>
     * <li> not null parameters or elements of the arrays shelf2 and shelf3
     * <li> shelf2 array must be of size 2
     * <li> shelf3 array must be of size 3
     * <li> admitted values for the elements of the shelves are: Resource.NONE, Resource.SHIELD, Resource.STONE, Resource.COIN, Resource.SERVANT
     * <li> in a valid configuration, a shelf can not contain 2 different resources, both not being Resource.NONE (represents the empty space in a depot)
     * <li> no common resources are admitted between 2 shelves, except for Resource.NONE
     * @param shelf1 First shelf of the depot.
     * @param shelf2 Second shelf of the depot.
     * @param shelf3 Third shelf of the depot.
     * @return True if and only if the new configuration satisfies the above rules, False otherwise.
     */
    public static boolean validateNewConfig(Resource shelf1, Resource[] shelf2, Resource[] shelf3) {
        return shelf1 != null && shelf2 != null && shelf3 != null
                && (shelf2.length == 2 && shelf3.length == 3)
                && shelf1 != Resource.FAITH && shelf1 != Resource.EXTRA
                && (Arrays.stream(shelf2).noneMatch(r -> r == null || r == Resource.FAITH || r == Resource.EXTRA || (shelf1 != Resource.NONE && r == shelf1)))
                && (Arrays.stream(shelf3).noneMatch(r -> r == null || r == Resource.FAITH || r == Resource.EXTRA || (shelf1 != Resource.NONE && r == shelf1)))
                && (Arrays.stream(shelf2).filter(r -> r != Resource.NONE).noneMatch(d -> (Arrays.stream(shelf3).anyMatch(t -> t == d))))
                && (Arrays.stream(shelf2).filter(r -> r != Resource.NONE).distinct().count() <= 1)
                && (Arrays.stream(shelf3).filter(r -> r != Resource.NONE).distinct().count() <= 1);
    }

    /**
     * Returns the representation of the current state of a given WarehouseDepot.
     * A WarehouseDepot or a WarehouseDepotSimplified may use this shared method by passing their internal values.
     * @param shelf1 First shelf of the depot.
     * @param shelf2 Second shelf of the depot.
     * @param shelf3 Third shelf of the depot.
     * @return A String representing the current state of the WarehouseDepot.
     */
    public static String toString(Resource shelf1, Resource[] shelf2, Resource[] shelf3) {
        StringBuilder result = new StringBuilder();
        result.append("               WAREHOUSE DEPOT:").append("\n");
        result.append("\n").append(A.CYAN + "=====X=====X=====X=====X=====X=====X=====X=====" + A.RESET).append("\n");
        result.append(A.CYAN + "  Shelf 1:  " + A.RESET).append(shelf1).append("\n");
        result.append(A.CYAN + "  Shelf 2:  " + A.RESET).append(shelf2[0]).append(" - ").append(shelf2[1]).append("\n");
        result.append(A.CYAN + "  Shelf 3:  " + A.RESET).append(shelf3[0]).append(" - ").append(shelf3[1]).append(" - ").append(shelf3[2]).append("\n");
        result.append("\n").append(A.CYAN + "=====X=====X=====X=====X=====X=====X=====X=====" + A.RESET).append("\n");
        return result.toString();
    }

    /**
     * Tries to set a new configuration of the WarehouseDepot, using the validateNewConfig() method.
     * If the operation terminated successfully, notifies observers.
     * @param shelf1 First shelf of the depot.
     * @param shelf2 Second shelf of the depot.
     * @param shelf3 Third shelf of the depot.
     * @return True if and only if the new configuration was set correctly. False otherwise.
     * @see #validateNewConfig(Resource, Resource[], Resource[])
     */
    public boolean setConfig(Resource shelf1, Resource[] shelf2, Resource[] shelf3) {
        if (!validateNewConfig(shelf1, shelf2, shelf3)) return false;
        this.shelf1 = shelf1;
        for (int i = 0; i < 3; i++) {
            if (i < 2) this.shelf2[i] = shelf2[i];
            this.shelf3[i] = shelf3[i];
        }
        notifyObservers();
        return true;
    }

    /**
     * Returns a map of the resources contained in the depot, Resource.NONE excluded.
     * @return A map of the resources contained in the depot, Resource.NONE excluded.
     */
    public Map<Resource, Integer> getResources() {
        Map<Resource, Integer> result = new HashMap<>();
        Resource r2;
        Resource r3;
        result.put(Resource.COIN, 0);
        result.put(Resource.SERVANT, 0);
        result.put(Resource.SHIELD, 0);
        result.put(Resource.STONE, 0);
        if (this.shelf1 != Resource.NONE) {
            result.put(this.shelf1, 1);
        }
        r2 = Arrays.stream(this.shelf2).filter(r -> r != Resource.NONE).findFirst().orElse(Resource.NONE);
        r3 = Arrays.stream(this.shelf3).filter(r -> r != Resource.NONE).findFirst().orElse(Resource.NONE);
        if (r2 != Resource.NONE)
            result.put(r2, getShelf2ResourceNumber());
        if (r3 != Resource.NONE)
            result.put(r3, getShelf3ResourceNumber());
        return result;
    }

    /**
     * Returns the amount of resources contained in the depot, Resource.NONE excluded.
     * @return The amount of resources contained in the depot, Resource.NONE excluded.
     */
    public int getTotal() {
        int result = 0;
        if (this.shelf1 != Resource.NONE) result++;
        for (int i = 0; i < 3; i++) {
            if (i < 2 && this.shelf2[i] != Resource.NONE) result++;
            if (this.shelf3[i] != Resource.NONE) result++;
        }
        return result;
    }

    /**
     * Tries to consume a specified resource from the WarehouseDepot.
     * If it consumes it successfully, notifies observers and returns True.
     * The consumed resource is swapped with a Resource.NONE.
     * @param resource The Resource to consume.
     * @return True if and only if the Resource was correctly consumed. False otherwise.
     */
    public boolean consume(Resource resource) {
        if (this.shelf1 == resource) {
            this.shelf1 = Resource.NONE;
            notifyObservers();
            return true;
        } else {
            for (int i = 2; i >= 0; i--) {
                if (i < 2 && this.shelf2[i] == resource) {
                    this.shelf2[i] = Resource.NONE;
                    notifyObservers();
                    return true;
                } else if (this.shelf3[i] == resource) {
                    this.shelf3[i] = Resource.NONE;
                    notifyObservers();
                    return true;
                }
            }
        }
        //no such resource in the depot
        return false;
    }

    /**
     * Tries to add a specified Resource in this depot.
     * This method follows the rules of validateNewConfig().
     * @param resource The Resource to add.
     * @return True if and only if the Resource was correctly added. False otherwise.
     * @see #validateNewConfig(Resource, Resource[], Resource[])
     */
    public boolean add(Resource resource) {
        if (!this.isAddable(resource)) return false;
        boolean found = false;
        Resource[] tmpShelf2 = this.getShelf2(), tmpShelf3 = this.getShelf3();

        if (this.shelf1 == Resource.NONE) {
            if (this.setConfig(resource, tmpShelf2, tmpShelf3)) found = true;
        }
        if (!found) {
            for (int i = 0; i < 2; i++) {
                if (tmpShelf2[i] == Resource.NONE) {
                    tmpShelf2[i] = resource;
                    if (this.setConfig(this.shelf1, tmpShelf2, tmpShelf3)) {
                        found = true;
                        break;
                    }
                    tmpShelf2[i] = Resource.NONE;
                }
            }
        }
        if (!found) {
            for (int i = 0; i < 3; i++) {
                if (tmpShelf3[i] == Resource.NONE) {
                    tmpShelf3[i] = resource;
                    if (this.setConfig(this.shelf1, tmpShelf2, tmpShelf3)) break;
                    tmpShelf3[i] = Resource.NONE;
                }
            }
        }
        return true;
    }

    /**
     * Checks if a resource is addable in the WarehouseDepot, following the rules for a valid configuration.
     * @param resource The Resource to check if it is addable.
     * @return True if and only if the specified Resource is addable in the current configuration of the depot.
     * @see #validateNewConfig(Resource, Resource[], Resource[])
     */
    public boolean isAddable(Resource resource) {
        Resource[] tmpShelf2 = this.getShelf2(), tmpShelf3 = this.getShelf3();

        if (this.shelf1 == Resource.NONE) {
            if (validateNewConfig(resource, this.shelf2, this.shelf3)) return true;
        }
        for (int i = 0; i < 2; i++) {
            if (tmpShelf2[i] == Resource.NONE) {
                tmpShelf2[i] = resource;
                if (validateNewConfig(this.shelf1, tmpShelf2, this.shelf3)) return true;
                else tmpShelf2[i] = Resource.NONE;
            }
        }
        for (int i = 0; i < 3; i++) {
            if (tmpShelf3[i] == Resource.NONE) {
                tmpShelf3[i] = resource;
                if (validateNewConfig(this.shelf1, this.shelf2, tmpShelf3)) return true;
                else tmpShelf3[i] = Resource.NONE;
            }
        }
        //not possible to add resource in the depot
        return false;
    }

    public Resource getShelf1() {
        return this.shelf1;
    }

    /**
     * Returns 1 if the shelf1 is not empty, 0 otherwise.
     * @return 1 if the shelf1 is not empty, 0 otherwise.
     */
    public int getShelf1ResourceNumber() {
        if (shelf1 != Resource.NONE) return 1;
        return 0;
    }

    public Resource[] getShelf2() {
        return this.shelf2.clone();
    }

    /**
     * Returns the amount of Resource that are not Resource.NONE in shelf2.
     * @return The amount of Resource that are not Resource.NONE in shelf2.
     */
    public int getShelf2ResourceNumber() {
        int result = 0;
        if (shelf2[0] != Resource.NONE) result++;
        if (shelf2[1] != Resource.NONE) result++;
        return result;
    }

    public Resource[] getShelf3() {
        return this.shelf3.clone();
    }

    /**
     * Returns the amount of Resource that are not Resource.NONE in shelf3.
     * @return The amount of Resource that are not Resource.NONE in shelf3.
     */
    public int getShelf3ResourceNumber() {
        int result = 0;
        if (shelf3[0] != Resource.NONE) result++;
        if (shelf3[1] != Resource.NONE) result++;
        if (shelf3[2] != Resource.NONE) result++;
        return result;
    }

    /**
     * Creates a new WarehouseDepot which previews the action of swapping two specified rows.
     * @param r1 The row to swap with r2.
     * @param r2 The row to swap with r1.
     * @return A new WarehouseDepot with the rows r1 and r2 swapped.
     */
    public WarehouseDepot getSwapPreview(int r1, int r2) {
        WarehouseDepot result = new WarehouseDepot();
        result.setConfig(this.shelf1, new Resource[]{this.shelf2[0], this.shelf2[1]}, new Resource[]{this.shelf3[0], this.shelf3[1], this.shelf3[2]});
        result.swapRow(r1, r2);
        return result;
    }

    /**
     * Tries to swap two specified rows in this depot.
     * The swapping preferences a left-insertion of the present Resources.
     * Returns true if the two row were successfully swapped, false otherwise.
     * If the rows were swapped, notifies observers.
     * @param r1 The row to swap with r2.
     * @param r2 The row to swap with r1.
     * @return True if and only if the rows were swapped correctly. False in any other case.
     */
    public boolean swapRow(int r1, int r2) {
        if (r1 <= 0 || r1 >= 4) return false;
        if (r2 <= 0 || r2 >= 4) return false;
        if (r1 == r2) return true;

        if ((r1 == 1 && r2 == 2) || (r1 == 2 && r2 == 1)) {
            if (shelf2[0] != Resource.NONE && shelf2[1] != Resource.NONE) return false;
            Resource tmp = shelf1;
            if (shelf2[0] != Resource.NONE) {
                shelf1 = shelf2[0];
                shelf2[0] = tmp;
            } else if (shelf2[1] != Resource.NONE) {
                shelf1 = shelf2[1];
                shelf2[0] = tmp;
                shelf2[1] = Resource.NONE;
            } else {
                shelf1 = Resource.NONE;
                shelf2[0] = tmp;
            }
        } else if (r1 == 1 || r1 == 3 && r2 == 1) {
            if (shelf3[0] != Resource.NONE && shelf3[1] != Resource.NONE && shelf3[2] != Resource.NONE) return false;
            if (shelf3[0] != Resource.NONE && shelf3[1] != Resource.NONE) return false;
            if (shelf3[1] != Resource.NONE && shelf3[2] != Resource.NONE) return false;
            if (shelf3[0] != Resource.NONE && shelf3[2] != Resource.NONE) return false;

            Resource tmp = shelf1;
            if (shelf3[0] != Resource.NONE) {
                shelf1 = shelf3[0];
                shelf3[0] = tmp;
            } else if (shelf3[1] != Resource.NONE) {
                shelf1 = shelf3[1];
                shelf3[0] = tmp;
                shelf3[1] = Resource.NONE;
            } else if (shelf3[2] != Resource.NONE) {
                shelf1 = shelf3[2];
                shelf3[0] = tmp;
                shelf3[2] = Resource.NONE;
            } else {
                shelf1 = shelf3[0];
                shelf3[0] = tmp;
            }
        } else {
            if (shelf3[0] != Resource.NONE && shelf3[1] != Resource.NONE && shelf3[2] != Resource.NONE) return false;
            Resource tmp1;
            Resource tmp2;

            if (shelf2[0] == Resource.NONE) {
                tmp1 = shelf2[1];
                tmp2 = shelf2[0];
            } else {
                tmp1 = shelf2[0];
                tmp2 = shelf2[1];
            }

            if (shelf3[0] != Resource.NONE && shelf3[1] != Resource.NONE) {
                shelf2[0] = shelf3[0];
                shelf2[1] = shelf3[1];
                shelf3[0] = tmp1;
                shelf3[1] = tmp2;
            } else if (shelf3[1] != Resource.NONE && shelf3[2] != Resource.NONE) {
                shelf2[0] = shelf3[1];
                shelf2[1] = shelf3[2];
                shelf3[0] = tmp1;
                shelf3[1] = tmp2;
                shelf3[2] = Resource.NONE;
            } else if (shelf3[0] != Resource.NONE && shelf3[2] != Resource.NONE) {
                shelf2[0] = shelf3[0];
                shelf2[1] = shelf3[2];
                shelf3[0] = tmp1;
                shelf3[1] = tmp2;
                shelf3[2] = Resource.NONE;
            } else if (shelf3[0] != Resource.NONE) {
                shelf2[0] = shelf3[0];
                shelf2[1] = Resource.NONE;
                shelf3[0] = tmp1;
                shelf3[1] = tmp2;
            } else if (shelf3[1] != Resource.NONE) {
                shelf2[0] = shelf3[1];
                shelf2[1] = Resource.NONE;
                shelf3[0] = tmp1;
                shelf3[1] = tmp2;
            } else if (shelf3[2] != Resource.NONE) {
                shelf2[0] = shelf3[2];
                shelf2[1] = Resource.NONE;
                shelf3[0] = tmp1;
                shelf3[1] = tmp2;
                shelf3[2] = Resource.NONE;
            } else {
                shelf2[0] = Resource.NONE;
                shelf2[1] = Resource.NONE;
                shelf3[0] = tmp1;
                shelf3[1] = tmp2;
            }
        }
        notifyObservers();
        return true;
    }

    @Override
    public String toString() {
        return WarehouseDepot.toString(this.shelf1, this.shelf2, this.shelf3);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof WarehouseDepot)) return false;
        WarehouseDepot o = (WarehouseDepot) obj;
        return (this.shelf1 == o.shelf1
                && Arrays.equals(this.shelf2, o.shelf2)
                && Arrays.equals(this.shelf3, o.shelf3));
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.shelf1, Arrays.hashCode(this.shelf2), Arrays.hashCode(this.shelf3));
    }

    /**
     * Creates a message using generateMessage() and notifies observers.
     * @see #generateMessage()
     */
    private void notifyObservers() {
        this.notifyObservers(generateMessage());
    }

    /**
     * Returns a MSG_UPD_WarehouseDepot representing the current state of the WarehouseDepot.
     * @return A MSG_UPD_WarehouseDepot representing the current state of the WarehouseDepot.
     */
    public MSG_UPD_WarehouseDepot generateMessage() {
        return new MSG_UPD_WarehouseDepot(
                shelf1,
                shelf2,
                shelf3
        );
    }
}