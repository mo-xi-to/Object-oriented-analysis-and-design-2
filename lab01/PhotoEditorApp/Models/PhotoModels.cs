using System;
using System.ComponentModel;
using System.Runtime.CompilerServices;
using System.Windows.Media.Imaging;

namespace PhotoEditor.Models
{
    public interface IMyCloneable
    {
        IMyCloneable clone();
    }

    public class EffectSettings : IMyCloneable, INotifyPropertyChanged
    {
        private double _brightness = 0;
        private double _exposure = 0;
        private double _warmth = 0;
        private double _blurRadius = 0;
        private bool _isBW = false;

        public double brightness { get => _brightness; set { _brightness = value; OnPropertyChanged(); OnPropertyChanged(nameof(absBrightness)); } }
        public double exposure { get => _exposure; set { _exposure = value; OnPropertyChanged(); OnPropertyChanged(nameof(absExposure)); } }
        public double warmth { get => _warmth; set { _warmth = value; OnPropertyChanged(); OnPropertyChanged(nameof(absWarmth)); } }
        public double blurRadius { get => _blurRadius; set { _blurRadius = value; OnPropertyChanged(); } }
        public bool isBW { get => _isBW; set { _isBW = value; OnPropertyChanged(); } }

        public double absBrightness => Math.Abs(_brightness);
        public double absExposure => Math.Abs(_exposure);
        public double absWarmth => Math.Abs(_warmth);

        public EffectSettings clone()
        {
            return (EffectSettings)this.MemberwiseClone();
        }

        IMyCloneable IMyCloneable.clone() => this.clone();

        public event PropertyChangedEventHandler PropertyChanged;
        protected void OnPropertyChanged([CallerMemberName] string name = null) => 
            PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(name));
    }

    public class ImageLayer : INotifyPropertyChanged
    {
        public string name { get; set; }
        public BitmapSource imageSource { get; set; }
        public BitmapSource grayscaleSource { get; set; }

        private EffectSettings _settings; 
        public EffectSettings settings 
        { 
            get => _settings; 
            set { _settings = value; OnPropertyChanged(); } 
        }

        public void applySettings(IMyCloneable prototype)
        {
            this.settings = (EffectSettings)prototype.clone();
        }

        public event PropertyChangedEventHandler PropertyChanged;
        protected void OnPropertyChanged([CallerMemberName] string name = null)
        {
            PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(name));
        }
    }
}