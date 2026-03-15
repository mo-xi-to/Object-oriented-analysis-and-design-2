using PhotoEditor.Models;

namespace PhotoEditor.Logic
{
    public static class NoPatternProcessor
    {
        public static EffectSettings CreateManualCopy(EffectSettings source)
        {
            EffectSettings copy = new EffectSettings();
            
            copy.brightness = source.brightness;
            copy.exposure = source.exposure;
            copy.warmth = source.warmth;
            copy.blurRadius = source.blurRadius;
            copy.isBW = source.isBW;
            
            return copy;
        }

        public static void ManualApply(ImageLayer target, EffectSettings source)
        {
            EffectSettings manualCopy = CreateManualCopy(source);
            target.settings = manualCopy;
        }
    }
}