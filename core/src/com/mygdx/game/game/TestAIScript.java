package com.mygdx.game.game;

import com.badlogic.gdx.Gdx;
import com.mygdx.game.data.Data;
import com.mygdx.game.data.Stats;

/**
 * Created by Sylphe on 14/06/2016.
 * Classe permettant de tester toutes les methodes qui seront utilisees dans les scripts d'IA genetique
 */
public class TestAIScript {

    private String LABEL = "TestAIScript";
    private Character charToTest;

    public TestAIScript(Character charToTest){
        this.charToTest = charToTest;
    }

    public void testAllScripts(){
        Gdx.app.log(LABEL,"On lance les tests unitaires pour chaque methode utilisee en IA genetique.");
       // Gdx.app.log(LABEL,"Le personnage à tester est "+charToTest.getName()+", id ="+charToTest.getTrueID()+", en position : x="+charToTest.getX()+"-y="+charToTest.getY());
        Gdx.app.log(LABEL,"Le personnage à tester est "+charToTest.toString());
        Gdx.app.log(LABEL,"============================================================================");
        Gdx.app.log(LABEL,"Test de la methode getDeplacement(int1, int2) : ");
        for(int i = -2; i< 4;i++){
            for(int j = -2; j< 4;j++) {
                Gdx.app.log(LABEL, "int1=" + i+" int2="+j+" -> "+charToTest.getDeplacement(i,j));
            }
        }
        Gdx.app.log(LABEL,"============================================================================");
        Gdx.app.log(LABEL,"Test de la methode getSpells(0) -> "+charToTest.getSpells().get(0).getName());
        Gdx.app.log(LABEL,"============================================================================");

        Gdx.app.log(LABEL,"Test de la methode getMaxDamagingSpellId() -> "+charToTest.getMaxDamagingSpellId());

        Gdx.app.log(LABEL,"============================================================================");
        Gdx.app.log(LABEL,"Test de la methode getMaxHealingSpellId() -> "+charToTest.getMaxHealingSpellId());

        Gdx.app.log(LABEL,"============================================================================");
        Character ch2 = charToTest.researchCharacter(Data.SOUTH);
        Gdx.app.log(LABEL, "Test de la methode researchCharacter(SOUTH) -> " + ((ch2 == null)?
                "empty" : ch2.toString()) );
        ch2 = charToTest.researchCharacter(Data.NORTH);
        Gdx.app.log(LABEL,"Test de la methode researchCharacter(NORTH) -> " + ((ch2 == null)?
                "empty" : ch2.toString()) );
        ch2 = charToTest.researchCharacter(Data.WEST);
        Gdx.app.log(LABEL,"Test de la methode researchCharacter(WEST) -> " + ((ch2 == null)?
                "empty" : ch2.toString()) );
        ch2 = charToTest.researchCharacter(Data.EAST);
        Gdx.app.log(LABEL,"Test de la methode researchCharacter(EAST) -> " + ((ch2 == null)?
                "empty" :ch2.toString()));
        Gdx.app.log(LABEL,"============================================================================");
        Gdx.app.log(LABEL,"Test de la methode Portee(MaxDamagingSpell) -> "+charToTest.Portee(charToTest.getMaxDamagingSpellId()));
        Gdx.app.log(LABEL,"Test de la methode Portee(MaxHealingSpell) -> "+charToTest.Portee(charToTest.getMaxHealingSpellId()));
        Gdx.app.log(LABEL,"============================================================================");
        Gdx.app.log(LABEL,"Test de la methode isCharacterInLine(SOUTH) -> "+(charToTest.isCharacterInLine(Data.SOUTH)?1:0));
        Gdx.app.log(LABEL,"Test de la methode isCharacterInLine(NORTH) -> "+(charToTest.isCharacterInLine(Data.NORTH)?1:0));
        Gdx.app.log(LABEL,"Test de la methode isCharacterInLine(WEST) -> "+(charToTest.isCharacterInLine(Data.WEST)?1:0));
        Gdx.app.log(LABEL,"Test de la methode isCharacterInLine(EAST) -> "+(charToTest.isCharacterInLine(Data.EAST)?1:0));
        Gdx.app.log(LABEL,"============================================================================");
        Gdx.app.log(LABEL, "Recuperation des stats : Test des methodes de la classe Stats ");
        Stats chst = charToTest.getStats();
        Gdx.app.log(LABEL,"============================================================================");
        Gdx.app.log(LABEL,"Test de la methode getLife() -> "+chst.getLife());
        Gdx.app.log(LABEL,"Test de la methode getMaxLife() -> "+chst.getMaxLife());
        Gdx.app.log(LABEL,"Test de la methode getLifePercentage() -> "+chst.getLifePercentage());

        Gdx.app.log(LABEL,"============================================================================");
        Gdx.app.log(LABEL,"Test de la methode getMana() -> "+chst.getMana());
        Gdx.app.log(LABEL,"Test de la methode getMaxMana() -> "+chst.getMaxMana());
        Gdx.app.log(LABEL,"Test de la methode getManaPercentage() -> "+chst.getManaPercentage());
        Gdx.app.log(LABEL,"============================================================================");
        Gdx.app.log(LABEL,"Test de la methode getMagicResist() -> "+chst.getMagicResist());
        Gdx.app.log(LABEL,"Test de la methode getArmor() -> "+chst.getArmor());

        Gdx.app.log(LABEL,"============================================================================");
        Gdx.app.log(LABEL,"Test de la methode getStrength() -> "+chst.getStrength());
        Gdx.app.log(LABEL,"Test de la methode getMagicPower() -> "+chst.getMagicPower());


        Gdx.app.log(LABEL,"============================================================================");
        Gdx.app.log(LABEL,"Test de la methode getMovement() -> "+chst.getMovementPoints());
        Gdx.app.log(LABEL,"Test de la methode getEyeSight() -> "+chst.getEyeSight());

        Gdx.app.log(LABEL,"============================================================================");
        Gdx.app.log(LABEL,"Test de la methode getCharacterClass() -> "+chst.getCharacterClass());

        Gdx.app.log(LABEL,"============================================================================");

        Gdx.app.log(LABEL,"Tests unitaires termines ");

    }
}
