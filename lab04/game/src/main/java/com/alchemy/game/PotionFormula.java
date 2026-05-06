package com.alchemy.game;
import java.util.Objects;

public final class PotionFormula {
    public enum Base { NONE, WATER, OIL }
    
    private final Base base;
    private final int herbs;
    private final int mushrooms;
    private final int boilTime;

    public PotionFormula(Base base, int herbs, int mushrooms, int boilTime) {
        if (herbs < 0 || mushrooms < 0 || boilTime < 0) throw new IllegalArgumentException();
        this.base = base;
        this.herbs = herbs;
        this.mushrooms = mushrooms;
        this.boilTime = boilTime;
    }

    public int calculateAccuracy(PotionFormula ideal) {
        int penalty = 0;
        if (this.base != ideal.base) penalty += 50;
        penalty += Math.abs(this.herbs - ideal.herbs) * 15;
        penalty += Math.abs(this.mushrooms - ideal.mushrooms) * 20;
        penalty += Math.abs(this.boilTime - ideal.boilTime) * 5;
        return Math.max(0, 100 - penalty);
    }

    public String generateFeedback(PotionFormula ideal) {
        if (this.base != ideal.base) return "🤢 Не та основа! Это пить нельзя!";
        if (this.herbs != ideal.herbs) return "🌿 Не тот вкус трав. Пропорции нарушены.";
        if (this.mushrooms != ideal.mushrooms) return "🍄 Грибов не столько, сколько нужно!";
        if (this.boilTime > ideal.boilTime) return "🔥 Зелье перекипело и пахнет гарью!";
        if (this.boilTime < ideal.boilTime) return "🧊 Оно сырое! Надо было варить дольше.";
        return "✨ Идеально! Вы мастер-алхимик!";
    }

    public Base getBase() { return base; }
    public int getHerbs() { return herbs; }
    public int getMushrooms() { return mushrooms; }
    public int getBoilTime() { return boilTime; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PotionFormula)) return false;
        PotionFormula that = (PotionFormula) o;
        return herbs == that.herbs && mushrooms == that.mushrooms && boilTime == that.boilTime && base == that.base;
    }
    @Override
    public int hashCode() { return Objects.hash(base, herbs, mushrooms, boilTime); }
}