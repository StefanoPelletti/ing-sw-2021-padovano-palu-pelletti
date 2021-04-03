package it.polimi.ingsw.Model;

import java.util.*;
public class WarehouseDepot {

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

    public static boolean validateNewConfig(Resource shelf1, Resource[] shelf2, Resource[] shelf3){
        return shelf1 != null && shelf2 != null && shelf3 != null
                && (shelf2.length == 2 && shelf3.length == 3)
                && shelf1 != Resource.FAITH
                && (Arrays.stream(shelf2).noneMatch(r -> r == null || r == Resource.FAITH || (shelf1 != Resource.NONE && r == shelf1)))
                && (Arrays.stream(shelf3).noneMatch(r -> r == null || r == Resource.FAITH || (shelf1 != Resource.NONE && r == shelf1)))
                && (Arrays.stream(shelf2).filter(r -> r != Resource.NONE).noneMatch(d -> (Arrays.stream(shelf3).anyMatch(t -> t == d))))
                && (Arrays.stream(shelf2).filter(r -> r != Resource.NONE).distinct().count() <= 1)
                && (Arrays.stream(shelf3).filter(r -> r != Resource.NONE).distinct().count() <= 1);
    }

    public boolean setConfig(Resource shelf1, Resource[] shelf2, Resource[] shelf3){
        if(!validateNewConfig(shelf1,shelf2,shelf3)) return false;
        this.shelf1 = shelf1;
        for(int i=0; i<3;i++){
            if(i<2) this.shelf2[i]=shelf2[i];
            this.shelf3[i]=shelf3[i];
        }
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

    //could be not used, but will remain here for now
    public boolean consume(ArrayList<Resource> resToDiscard){
        boolean result=true;
        boolean found=false;
        Resource tmpShelf1=this.shelf1;
        Resource[] tmpShelf2=new Resource[2], tmp3Shelf=new Resource[3];
        for(int i=0;i<3;i++){
            if(i<2) tmpShelf2[i]=this.shelf2[i];
            tmp3Shelf[i]=this.shelf3[i];
        }

        for(Resource r:resToDiscard){
            if((r==Resource.NONE)||(r==Resource.FAITH)){
                result=false;
                break;
            }
            else if(r==tmpShelf1){
                tmpShelf1=Resource.NONE;
                found=true;
            }
            else {
                for (int i = 2; i >=0; i--) {
                    if (i < 2 && tmpShelf2[i] == r) {
                        tmpShelf2[i] = Resource.NONE;
                        found=true;
                        break;
                    }
                    else if (tmp3Shelf[i] == r) {
                        tmp3Shelf[i] = Resource.NONE;
                        found=true;
                        break;
                    }
                }
            }
            if(!found) {
                result=false;
                break;
            }
            found=false;
        }

        if(result) {
            this.setConfig(tmpShelf1, tmpShelf2, tmp3Shelf);
        }
        return result;
    }

    //used when the player buys a devCard or activates production
    public boolean consume(Resource resource){
        if(this.shelf1==resource){
            this.shelf1=Resource.NONE;
            return true;
        }
        else{
            for(int i=2; i>=0; i--){
                if(i<2 && this.shelf2[i]==resource){
                    this.shelf2[i]=Resource.NONE;
                    return true;
                }
                else if(this.shelf3[i]==resource){
                    this.shelf3[i]=Resource.NONE;
                    return true;
                }
            }
        }
        //no such resource in the depot
        return false;
    }

    public String toString(){
        return "Shelf 1:"+this.shelf1.toString()+"\nShelf 2:"+this.shelf2[0].toString()+","+this.shelf2[1].toString()+"\nShelf 3:"+this.shelf3[0].toString()+","+this.shelf3[1].toString()+","+this.shelf3[2].toString();
    }
}
