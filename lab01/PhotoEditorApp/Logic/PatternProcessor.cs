using PhotoEditor.Models;

namespace PhotoEditor.Logic
{
    public static class PatternProcessor
    {
        public static void PrototypeApply(ImageLayer target, IMyCloneable source)
        {
            target.applySettings(source); 
        }
    }
}