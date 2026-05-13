package com.alchemy.game;
import java.util.Objects;

public final class PotionFormula {
    public enum Base { NONE, WATER, OIL, ACID, SMOKE, FOG }
    
    private final Base base;
    private final int herbs, gHerb;
    private final int mushrooms, gMush;
    private final int moss, gMoss;
    private final int feathers, gFeather;
    private final int crystals, gCrystal;
    private final int brewTime;

    public PotionFormula(Base base, int herbs, int gHerb, int mushrooms, int gMush, 
                    int moss, int gMoss, int feathers, int gFeather, 
                    int crystals, int gCrystal, int brewTime) {

        if (base == null || base == Base.NONE) {
            throw new IllegalArgumentException("Зелье не может быть без основы!");
        }

        if (herbs < 0 || mushrooms < 0 || moss < 0 || feathers < 0 || crystals < 0) {
            throw new IllegalArgumentException("Количество ингредиентов не может быть отрицательным!");
        }

        if (isInvalidGrind(gHerb) || isInvalidGrind(gMush) || isInvalidGrind(gMoss) || 
            isInvalidGrind(gFeather) || isInvalidGrind(gCrystal)) {
            throw new IllegalArgumentException("Степень помола должна быть от 0 до 3!");
        }

        if (brewTime < 0 || brewTime > 100) {
            throw new IllegalArgumentException("Время варки должно быть от 0 до 100!");
        }

        this.base = base;
        this.herbs = herbs; this.gHerb = gHerb;
        this.mushrooms = mushrooms; this.gMush = gMush;
        this.moss = moss; this.gMoss = gMoss;
        this.feathers = feathers; this.gFeather = gFeather;
        this.crystals = crystals; this.gCrystal = gCrystal;
        this.brewTime = brewTime;
    }

    private boolean isInvalidGrind(int value) {
        return value < 0 || value > 3;
    }

    public int calculateCompositionAccuracy(PotionFormula ideal) {
        if (this.base != ideal.base) return 0;
        
        int penalty = 0;
        penalty += checkIng(this.herbs, this.gHerb, ideal.herbs, ideal.gHerb);
        penalty += checkIng(this.mushrooms, this.gMush, ideal.mushrooms, ideal.gMush);
        penalty += checkIng(this.moss, this.gMoss, ideal.moss, ideal.gMoss);
        penalty += checkIng(this.feathers, this.gFeather, ideal.feathers, ideal.gFeather);
        penalty += checkIng(this.crystals, this.gCrystal, ideal.crystals, ideal.gCrystal);
        
        penalty += Math.abs(this.brewTime - ideal.brewTime);
        
        return Math.max(0, 100 - penalty);
    }

    private int checkIng(int count, int grind, int idealCount, int idealGrind) {
        int p = Math.abs(count - idealCount) * 20;
        if (count > 0 && count == idealCount && grind != idealGrind) p += 25;
        return p;
    }

    public Base getBase() { return base; }
    public int getHerbs() { return herbs; }
    public int getgHerb() { return gHerb; }
    public int getMushrooms() { return mushrooms; }
    public int getgMush() { return gMush; }
    public int getMoss() { return moss; }
    public int getgMoss() { return gMoss; }
    public int getFeathers() { return feathers; }
    public int getgFeather() { return gFeather; }
    public int getCrystals() { return crystals; }
    public int getgCrystal() { return gCrystal; }
    public int getBrewTime() { return brewTime; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof PotionFormula)) return false;
        PotionFormula that = (PotionFormula) obj;
        return herbs == that.herbs && gHerb == that.gHerb && mushrooms == that.mushrooms && 
            gMush == that.gMush && moss == that.moss && gMoss == that.gMoss && 
            feathers == that.feathers && gFeather == that.gFeather && 
            crystals == that.crystals && gCrystal == that.gCrystal && 
            brewTime == that.brewTime && base == that.base;
    }

    @Override
    public int hashCode() { return Objects.hash(base, herbs, gHerb, mushrooms, gMush, moss, gMoss, feathers, gFeather, crystals, gCrystal, brewTime); }
}