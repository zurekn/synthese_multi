package com.mygdx.game.game;

//TODO add comments

import com.badlogic.gdx.utils.Array;
import com.mygdx.game.data.TrapD;
import com.mygdx.game.data.TrapData;
import java.util.Iterator;

import static com.badlogic.gdx.utils.XmlReader.Element;

public class Map {
    private int xSize;
    private int ySize;
    private Block[][] map;
    @SuppressWarnings("unused")
    private String background;

    public Map(Element xml) {
        Element mapInfo = xml.getChildByName("mapInfo");
        if (mapInfo == null)
            throw new NullPointerException(
                    "Map Information not found in xml file.");
        background = mapInfo.getChildByName("background").getText();
        int x = Integer.parseInt(mapInfo.getChildByName("x").getText());
        int y = Integer.parseInt(mapInfo.getChildByName("y").getText());

        map = new Block[x][y];
        this.xSize = x;
        this.ySize = y;
        for (int row = 0; row < x; row++) {
            for (int col = 0; col < y; col++) {
                map[row][col] = new Block(row, col);
            }
        }
        Array<Element> blocks = xml.getChildrenByName("block");
        Element block, e;
        for (Iterator<Element> it = blocks.iterator(); it.hasNext(); ) {
            block = it.next();
            x = block.getIntAttribute("x");
            y = block.getIntAttribute("y");

            if (block.getChildrenByName("untraversable") != null)
                map[x][y].setTraversable(false);

            e = block.getChildByName("trap");
            if (e != null) {
                TrapD trap = TrapData.getTrapById(e.getAttribute("id"));
                if (trap == null)
                    throw new NullPointerException("Trap not found");
                map[x][y].setTrap(trap);
            }

            e = block.getChildByName("damage");
            if (e != null) {
                map[x][y].setDamage(Integer.parseInt(e.getText()));
                map[x][y].setDamageType(e.getAttribute("type"));
            }
            // TODO Add conditions when blocks parameters are added

        }

    }

    public int getXSize() {
        return xSize;
    }

    public int getYSize() {
        return ySize;
    }

    public Block[][] getMap() {
        return map;
    }
}
