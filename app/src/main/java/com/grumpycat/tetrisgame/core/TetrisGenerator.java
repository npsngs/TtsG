package com.grumpycat.tetrisgame.core;

import com.grumpycat.tetrisgame.nodes.TetrisNode;
import com.grumpycat.tetrisgame.tetris.Tetris0;
import com.grumpycat.tetrisgame.tetris.Tetris1;
import com.grumpycat.tetrisgame.tetris.Tetris2;
import com.grumpycat.tetrisgame.tetris.Tetris3;
import com.grumpycat.tetrisgame.tetris.Tetris4;
import com.grumpycat.tetrisgame.tetris.Tetris5;
import com.grumpycat.tetrisgame.tetris.Tetris6;

public class TetrisGenerator {
    private TetrisNode[] tetrisCache;
    public TetrisGenerator() {
        tetrisCache = new TetrisNode[7];
    }


    public TetrisNode getTetrisNode(int mode , TetrisContainer container){
        if(mode < 0 || mode > 6){
           return null;
        }
        TetrisNode tetrisNode = tetrisCache[mode];
        if(tetrisNode == null){
            switch (mode){
                case 0:
                    tetrisNode = new Tetris0(container.getUnitSize(),
                            container.getUnitMargin(),
                            container.getRealFrame());
                    break;
                case 1:
                    tetrisNode = new Tetris1(container.getUnitSize(),
                            container.getUnitMargin(),
                            container.getRealFrame());
                    break;
                case 2:
                    tetrisNode = new Tetris2(container.getUnitSize(),
                            container.getUnitMargin(),
                            container.getRealFrame());
                    break;
                case 3:
                    tetrisNode = new Tetris3(container.getUnitSize(),
                            container.getUnitMargin(),
                            container.getRealFrame());
                    break;
                case 4:
                    tetrisNode = new Tetris4(container.getUnitSize(),
                            container.getUnitMargin(),
                            container.getRealFrame());
                    break;
                case 5:
                    tetrisNode = new Tetris5(container.getUnitSize(),
                            container.getUnitMargin(),
                            container.getRealFrame());
                    break;
                case 6:
                    tetrisNode = new Tetris6(container.getUnitSize(),
                            container.getUnitMargin(),
                            container.getRealFrame());
                    break;
            }

            tetrisCache[mode] = tetrisNode;
        }else{
            tetrisNode.initWithSize(container.getUnitSize(),
                    container.getUnitMargin(),
                    container.getRealFrame());
        }
        return tetrisNode;
    }
}
