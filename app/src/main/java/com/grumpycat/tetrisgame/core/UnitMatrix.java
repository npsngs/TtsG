package com.grumpycat.tetrisgame.core;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UnitMatrix {
    public static final int NULL =   -1;
    public static final int WALL_L = -2;
    public static final int WALL_R = -3;
    public static final int WALL_T = -4;
    public static final int WALL_B = -5;
    public static final int NEW_ADD = 7;

    private int [][] units;
    private int row;
    private int column;
    private List<int[]> validUnits;
    public UnitMatrix(int row, int column) {
        this.row = row;
        this.column = column;
        units = new int[column][row];
        for(int x=0;x<column;x++){
            for(int y=0;y<row;y++){
                units[x][y] = -1;
            }
        }
        validUnits = new ArrayList<>();
    }

    public void clear(){
        if(validUnits.size() > 0){
            int ptr = validUnits.size() -1;
            while (ptr > -1){
                int[] index = validUnits.remove(ptr);
                units[index[0]][index[1]] = NULL;
                ptr--;
                IndexCache.recycle(index);
            }
        }
    }

    public void set(int x, int y, int value){
        if(x < 0 || y < 0 || x > column-1 || y > row-1){
            return;
        }

        int oldValue = units[x][y];
        if(oldValue != value){
            units[x][y] = value;
            if(oldValue == NULL){
                int[] index = IndexCache.getIndex();
                index[0] = x;
                index[1] = y;
                validUnits.add(index);
            }
        }
    }

    public boolean isFinishLine(int line){
        if(line < 0 || line >= row){
            return false;
        }

        for(int x=0;x<column;x++){
            if (units[x][line] == NULL) return false;
        }
        return true;
    }

    public boolean isEmptyLine(int line){
        for(int x=0;x<column;x++){
            if (units[x][line] != NULL) return false;
        }
        return true;
    }

    public void deleteLines(int[] lines){
        if(validUnits.size() > 0){
            int ptr = validUnits.size() -1;
            while (ptr > -1){
                int[] index = validUnits.remove(ptr);
                ptr--;
                IndexCache.recycle(index);
            }
        }

        int ptr = lines.length-1;
        int dropLine = 0;
        for(int y = row-1;y>=0;y--){
            if(ptr >=0 && y == lines[ptr]){
                for(int x=0;x<column;x++){
                    units[x][y] = NULL;
                }
                dropLine++;
                ptr--;
            }else{
                if (dropLine > 0){
                    moveLine(y, y+dropLine);
                    addAllIndex(y+dropLine);
                }else{
                    addAllIndex(y);
                }
            }
        }
    }

    public void addBottomLine(int[] values){
        for(int i=1;i<row;i++){
            moveLine(i, i-1);
        }
        for(int[] ids:validUnits){
            ids[1]--;
        }

        int y = row-1;
        for(int x = 0; x< column;x++){
            if(x<values.length){
                if(values[x]==1){
                    units[x][y] = NEW_ADD;
                    int[] index = IndexCache.getIndex();
                    index[0] = x;
                    index[1] = y;
                    validUnits.add(index);
                }else{
                    units[x][y] = NULL;
                }
            }else{
                units[x][y] = NULL;
            }
        }



    }

    private void addAllIndex(int line){
        for(int x=0;x<column;x++){
            if(units[x][line] != NULL){
                int[] index = IndexCache.getIndex();
                index[0] = x;
                index[1] = line;

                validUnits.add(index);
            }
        }
    }

    private void moveLine(int from, int to){
        for(int x=0;x<column;x++){
            units[x][to] = units[x][from];
            units[x][from] = NULL;
        }
    }

    public int get(int x, int y){
        if(x < 0)
            return WALL_L;
        if(x > column -1)
            return WALL_R;
        if(y < 0)
            return WALL_T;
        if(y > row -1)
            return WALL_B;

        return units[x][y];
    }

    public int getDownUnit(int x, int y){
        if( y < -1)
            return WALL_T;
        if(y > row -2)
            return WALL_B;
        return units[x][y+1];
    }

    public int[][] getUnits() {
        return units;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public List<int[]> getValidUnits() {
        return validUnits;
    }

    public boolean hasValidUnit(){
        return !validUnits.isEmpty();
    }

    private static class IndexCache{
        private static int maxCacheSize = 50;
        private static List<int[]> pool= new ArrayList<>(10);
        private static int ptr = -1;
        private static int[] getIndex(){
            if(ptr < 0){
                return new int[]{-1, -1};
            }else{
                int [] index = pool.remove(ptr);
                ptr--;
                return index;
            }
        }

        private static void recycle(int[] index){
            if (pool.size() < maxCacheSize){
                pool.add(index);
                ptr++;
            }
        }
    }


    public static JSONObject writeToJson(UnitMatrix unitMatrix) throws Exception{
        JSONObject json = new JSONObject();
        json.put("row", unitMatrix.row);
        json.put("column", unitMatrix.column);
        JSONArray array = new JSONArray();
        for(int[] index:unitMatrix.validUnits){
            JSONObject item = new JSONObject();
            item.put("x", index[0]);
            item.put("y", index[1]);
            item.put("value", unitMatrix.units[index[0]][index[1]]);
            array.put(item);
        }
        json.put("units",array);
        return json;
    }

    public static UnitMatrix readFromJson(JSONObject json) throws Exception{
        int row = json.getInt("row");
        int column = json.getInt("column");
        UnitMatrix unitMatrix = new UnitMatrix(row, column);
        JSONArray array = json.getJSONArray("units");
        int len = array.length();
        for(int i=0;i<len;i++){
            JSONObject item = array.getJSONObject(i);
            unitMatrix.set(
                    item.getInt("x"),
                    item.getInt("y"),
                    item.getInt("value"));
        }
        return unitMatrix;
    }

}
