package it.polimi.ingsw.Server.Model;

import it.polimi.ingsw.Networking.Message.UpdateMessages.MSG_UPD_Market;
import it.polimi.ingsw.Networking.Message.UpdateMessages.PlayerUpdate.MSG_UPD_WarehouseDepot;
import it.polimi.ingsw.Server.Model.Enumerators.Resource;
import it.polimi.ingsw.Server.Utils.ModelObservable;

import java.util.*;
public class WarehouseDepot extends ModelObservable {

    private Resource shelf1;
    private Resource[] shelf2;
    private Resource[] shelf3;

    public WarehouseDepot(){
        this.shelf1 =Resource.NONE;
        this.shelf2=new Resource[2];
        this.shelf3 =new Resource[3];
        for(int i=0; i<3;i++){
            if(i<2) shelf2[i]=Resource.NONE;
            shelf3[i]=Resource.NONE;
        }
    }

    //returns true if the rules for the depot are met by the given configuration
    public static boolean validateNewConfig(Resource shelf1, Resource[] shelf2, Resource[] shelf3){
        return shelf1 != null && shelf2 != null && shelf3 != null
                && (shelf2.length == 2 && shelf3.length == 3)
                && shelf1!=Resource.FAITH &&shelf1 !=Resource.EXTRA
                && (Arrays.stream(shelf2).noneMatch(r -> r == null || r==Resource.FAITH || r==Resource.EXTRA || (shelf1 != Resource.NONE && r == shelf1)))
                && (Arrays.stream(shelf3).noneMatch(r -> r == null || r==Resource.FAITH ||r==Resource.EXTRA || (shelf1 != Resource.NONE && r == shelf1)))
                && (Arrays.stream(shelf2).filter(r -> r != Resource.NONE).noneMatch(d -> (Arrays.stream(shelf3).anyMatch(t -> t == d))))
                && (Arrays.stream(shelf2).filter(r -> r != Resource.NONE).distinct().count() <= 1)
                && (Arrays.stream(shelf3).filter(r -> r != Resource.NONE).distinct().count() <= 1);
    }


    //sets the given configuration if valid
    public boolean setConfig(Resource shelf1, Resource[] shelf2, Resource[] shelf3){
        if(!validateNewConfig(shelf1,shelf2,shelf3)) return false;
        this.shelf1 = shelf1;
        for(int i=0; i<3;i++){
            if(i<2) this.shelf2[i]=shelf2[i];
            this.shelf3[i]=shelf3[i];
        }
        notifyObservers();
        return true;
    }

    public Map<Resource,Integer> getResources(){
        Map<Resource,Integer> result=new HashMap<>();
        Resource r2, r3;
        result.put(Resource.COIN,0);
        result.put(Resource.SERVANT,0);
        result.put(Resource.SHIELD,0);
        result.put(Resource.STONE,0);
        if(this.shelf1 !=Resource.NONE) {
            result.put(this.shelf1, 1);
        }
        r2= Arrays.stream(this.shelf2).filter(r->r!=Resource.NONE).findFirst().orElse(Resource.NONE);
        r3= Arrays.stream(this.shelf3).filter(r->r!=Resource.NONE).findFirst().orElse(Resource.NONE);
        if(r2!=Resource.NONE) result.put(r2,(int) Arrays.stream(this.shelf2).filter(r->r!=Resource.NONE).count());
        if(r3!=Resource.NONE) result.put(r3,(int) Arrays.stream(this.shelf3).filter(r->r!=Resource.NONE).count());
        return result;
    }

    public int getTotal(){
        int result=0;
        if(this.shelf1 !=Resource.NONE) result++;
        for(int i=0; i<3; i++){
            if(i<2 && this.shelf2[i]!=Resource.NONE) result++;
            if(this.shelf3[i]!=Resource.NONE) result++;
        }
        return result;
    }

    //used when the player buys a devCard or activates production
    public boolean consume(Resource resource){
        if(this.shelf1==resource){
            this.shelf1=Resource.NONE;
            notifyObservers();
            return true;
        }
        else{
            for(int i=2; i>=0; i--){
                if(i<2 && this.shelf2[i]==resource){
                    this.shelf2[i]=Resource.NONE;
                    notifyObservers();
                    return true;
                }
                else if(this.shelf3[i]==resource){
                    this.shelf3[i]=Resource.NONE;
                    notifyObservers();
                    return true;
                }
            }
        }
        //no such resource in the depot
        return false;
    }

    //tries to add the given resource to the current configuration
    public boolean add(Resource resource){
        if(!this.isAddable(resource)) return false;
        boolean found=false;
        Resource[] tmpShelf2=this.getShelf2(), tmpShelf3=this.getShelf3();

        if(this.shelf1==Resource.NONE){
            if(this.setConfig(resource, tmpShelf2, tmpShelf3)) found=true;
        }
        if(!found) {
            for (int i = 0; i < 2; i++) {
                if (tmpShelf2[i] == Resource.NONE) {
                    tmpShelf2[i] = resource;
                    if (this.setConfig(this.shelf1, tmpShelf2, tmpShelf3)) {
                        found=true;
                        break;
                    }
                    tmpShelf2[i] = Resource.NONE;
                }
            }
        }
        if(!found) {
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

    //checks if the given resource is addable in any position in the depot
    public boolean isAddable(Resource resource){
        Resource[] tmpShelf2=this.getShelf2(), tmpShelf3=this.getShelf3();

        if(this.shelf1==Resource.NONE){
            if(validateNewConfig(resource, this.shelf2, this.shelf3)) return true;
        }
        for(int i=0; i<2; i++){
            if(tmpShelf2[i]==Resource.NONE) {
                tmpShelf2[i]=resource;
                if(validateNewConfig(this.shelf1, tmpShelf2, this.shelf3)) return true;
                else tmpShelf2[i]=Resource.NONE;
            }
        }
        for(int i=0; i<3; i++){
            if(tmpShelf3[i]==Resource.NONE) {
                tmpShelf3[i]=resource;
                if(validateNewConfig(this.shelf1, this.shelf2, tmpShelf3)) return true;
                else tmpShelf3[i]=Resource.NONE;
            }
        }
        //not possible to add resource in the depot
        return false;
    }

    public Resource getShelf1(){
        return this.shelf1;
    }

    public int getShelf1ResourceNumber() {
        if (shelf1 != Resource.NONE) return 1;
        return 0;
    }

    public Resource[] getShelf2(){
        return this.shelf2.clone();
    }

    public int getShelf2ResourceNumber() {
        int result=0;
        if (shelf2[0] != Resource.NONE) result++;
        if (shelf2[1] != Resource.NONE) result++;
        return result;
    }

    public Resource[] getShelf3(){
        return this.shelf3.clone();
    }

    public int getShelf3ResourceNumber() {
        int result=0;
        if (shelf3[0] != Resource.NONE) result++;
        if (shelf3[1] != Resource.NONE) result++;
        if (shelf3[2] != Resource.NONE) result++;
        return result;
    }



    public WarehouseDepot getSwapPreview(int r1, int r2)
    {
        WarehouseDepot result = new WarehouseDepot();
        result.setConfig(this.shelf1, new Resource[] { this.shelf2[0], this.shelf2[1] }, new Resource[] { this.shelf3[0], this.shelf3[1], this.shelf3[2]});
        result.swapRow(r1,r2);
        return result;
    }

    public boolean swapRow(int r1, int r2)
    {
        if (r1 <= 0 || r1>=4) return false;
        if (r2 <= 0 || r2>=4) return false;
        if (r1 == r2) return true;

        if( (r1==1&&r2==2) || (r1==2&&r2==1) )
        {
            if(shelf2[0] != Resource.NONE && shelf2[1] != Resource.NONE) return false;
            Resource tmp = shelf1;
            if(shelf2[0] != Resource.NONE) {
                shelf1 = shelf2[0];
                shelf2[0]=tmp;
            }
            else if(shelf2[1] != Resource.NONE) {
                shelf1 = shelf2[1];
                shelf2[0]=tmp;
                shelf2[1]=Resource.NONE;
            }
            else if(shelf2[0] == Resource.NONE && shelf2[1] == Resource.NONE )
            {
                shelf1 = Resource.NONE;
                shelf2[0] = tmp;
            }
        }
        else if( ( r1==1&&r2==3) || (r1==3&&r2==1) )
        {
            if(shelf3[0] != Resource.NONE && shelf3[1] != Resource.NONE && shelf3[2] != Resource.NONE ) return false;
            if(shelf3[0] != Resource.NONE && shelf3[1] != Resource.NONE) return false;
            if(shelf3[1] != Resource.NONE && shelf3[2] != Resource.NONE) return false;
            if(shelf3[0] != Resource.NONE && shelf3[2] != Resource.NONE) return false;

            Resource tmp = shelf1;
            if(shelf3[0] != Resource.NONE)
            {
                shelf1 = shelf3[0];
                shelf3[0] = tmp;
            }
            else if(shelf3[1] != Resource.NONE)
            {
                shelf1 = shelf3[1];
                shelf3[0] = tmp;
                shelf3[1] = Resource.NONE;
            }
            else if(shelf3[2] != Resource.NONE)
            {
                shelf1 = shelf3[2];
                shelf3[0] = tmp;
                shelf3[2] = Resource.NONE;
            }
            else if(shelf3[0] == Resource.NONE && shelf3[1] == Resource.NONE && shelf3[2] == Resource.NONE)
            {
                shelf1 = shelf3[0];
                shelf3[0] = tmp;
            }
        }
        else if( ( r1==2&&r2==3) || (r1==3&&r2==2) )
        {
            if(shelf3[0] != Resource.NONE && shelf3[1] != Resource.NONE && shelf3[2] != Resource.NONE) return false;
            Resource tmp1;
            Resource tmp2;

            if(shelf2[0]==Resource.NONE)
            {
                tmp1 = shelf2[1];
                tmp2 = shelf2[0];
            }
            else
            {
                tmp1 = shelf2[0];
                tmp2 = shelf2[1];
            }

            if(shelf3[0] != Resource.NONE && shelf3[1] != Resource.NONE)
            {
                shelf2[0]=shelf3[0];
                shelf2[1]=shelf3[1];
                shelf3[0]=tmp1;
                shelf3[1]=tmp2;
            }
            else if( shelf3[1] != Resource.NONE && shelf3[2] != Resource.NONE)
            {
                shelf2[0]=shelf3[1];
                shelf2[1]=shelf3[2];
                shelf3[0]=tmp1;
                shelf3[1]=tmp2;
                shelf3[2]=Resource.NONE;
            }
            else if( shelf3[0] != Resource.NONE && shelf3[2] != Resource.NONE)
            {
                shelf2[0]=shelf3[0];
                shelf2[1]=shelf3[2];
                shelf3[0]=tmp1;
                shelf3[1]=tmp2;
                shelf3[2]=Resource.NONE;
            }
            else if ( shelf3[0] != Resource.NONE)
            {
                shelf2[0]=shelf3[0];
                shelf2[1]=Resource.NONE;
                shelf3[0]=tmp1;
                shelf3[1]=tmp2;
                shelf3[2]=Resource.NONE;
            }
            else if ( shelf3[1] != Resource.NONE)
            {
                shelf2[0]=shelf3[1];
                shelf2[1]=Resource.NONE;
                shelf3[0]=tmp1;
                shelf3[1]=tmp2;
                shelf3[2]=Resource.NONE;
            }
            else if ( shelf3[2] != Resource.NONE)
            {
                shelf2[0] = shelf3[2];
                shelf2[1]= Resource.NONE;
                shelf3[0]=tmp1;
                shelf3[1]=tmp2;
                shelf3[2]=Resource.NONE;
            }
            else if (shelf3[0] == Resource.NONE && shelf3[1] == Resource.NONE && shelf3[2] == Resource.NONE )
            {
                shelf2[0] = Resource.NONE;
                shelf2[1] = Resource.NONE;
                shelf3[0] = tmp1;
                shelf3[1] = tmp2;
                shelf3[2]=Resource.NONE;
            }
        }
        notifyObservers();
        return true;
    }

    @Override
    public String toString(){
        StringBuilder result = new StringBuilder("");
        result.append("Shelf 1: ").append(this.shelf1.toString());
        result.append("Shelf 2: ").append(this.shelf2[0].toString()).append(", ").append(this.shelf2[1].toString());
        result.append("Shelf 3: ").append(this.shelf3[0].toString()).append(", ").append(this.shelf3[1].toString()).append(", ").append(this.shelf3[2].toString());
        return result.toString();
    }

    @Override
    public boolean equals( Object obj )
    {
        if(obj == this) return true;
        if(!(obj instanceof WarehouseDepot)) return false;
        WarehouseDepot o = (WarehouseDepot) obj;
        return(this.shelf1 == o.shelf1
                && Arrays.equals(this.shelf2, o.shelf2)
                && Arrays.equals(this.shelf3, o.shelf3));
    }

    private void notifyObservers(){
        this.notifyObservers(generateMessage());
    }

    public MSG_UPD_WarehouseDepot generateMessage() {
        return new MSG_UPD_WarehouseDepot(
                shelf1,
                shelf2,
                shelf3
        );
    }

}
