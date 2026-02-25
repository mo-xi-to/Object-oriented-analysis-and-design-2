using System;
using System.ComponentModel;
using System.Runtime.CompilerServices;
using System.Windows.Media.Imaging;
using System.Globalization;
using System.Windows.Data;
using System.Windows.Media;

namespace PhotoEditor.Models
{
    public interface IMyCloneable<T>
    {
        T Clone();
    }

    public class EffectSettings : IMyCloneable<EffectSettings>, INotifyPropertyChanged
    {
        private double _brightness = 0;
        private double _exposure = 0;
        private double _warmth = 0;
        private double _blurRadius = 0;
        private bool _isBW = false;

        public double Brightness { get => _brightness; set { _brightness = value; OnPropertyChanged(); OnPropertyChanged(nameof(AbsBrightness)); } }
        public double Exposure { get => _exposure; set { _exposure = value; OnPropertyChanged(); OnPropertyChanged(nameof(AbsExposure)); } }
        public double Warmth { get => _warmth; set { _warmth = value; OnPropertyChanged(); OnPropertyChanged(nameof(AbsWarmth)); } }
        public double BlurRadius { get => _blurRadius; set { _blurRadius = value; OnPropertyChanged(); } }
        public bool IsBW { get => _isBW; set { _isBW = value; OnPropertyChanged(); } }

        public double AbsBrightness => Math.Abs(_brightness);
        public double AbsExposure => Math.Abs(_exposure);
        public double AbsWarmth => Math.Abs(_warmth);

        public EffectSettings Clone()
        {
            return (EffectSettings)this.MemberwiseClone();
        }

        public event PropertyChangedEventHandler PropertyChanged;
        protected void OnPropertyChanged([CallerMemberName] string name = null) => 
            PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(name));
    }

    public class ImageLayer : INotifyPropertyChanged
    {
        public string Name { get; set; }
        public BitmapSource ImageSource { get; set; }
        public BitmapSource GrayscaleSource { get; set; }

        private EffectSettings _settings;
        public EffectSettings Settings 
        { 
            get => _settings; 
            set { _settings = value; OnPropertyChanged(); } 
        }

        public void ApplySettings(EffectSettings prototype)
        {
            this.Settings = prototype.Clone();
        }

        public event PropertyChangedEventHandler PropertyChanged;
        protected void OnPropertyChanged([CallerMemberName] string name = null)
        {
            PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(name));
        }
    }
}

namespace PhotoEditor
{
    public class BoolToVisibilityConverter : IValueConverter
    {
        public object Convert(object value, Type targetType, object parameter, CultureInfo culture)
        {
            bool isBW = (bool)value;
            if (parameter?.ToString() == "invert") return isBW ? System.Windows.Visibility.Collapsed : System.Windows.Visibility.Visible;
            return isBW ? System.Windows.Visibility.Visible : System.Windows.Visibility.Collapsed;
        }
        public object ConvertBack(object value, Type targetType, object parameter, CultureInfo culture) => null;
    }

    public class NegativeConverter : IValueConverter
    {
        public object Convert(object value, Type targetType, object parameter, CultureInfo culture)
        {
            if (value is double v) return v < 0; 
            return false;
        }
        public object ConvertBack(object value, Type targetType, object parameter, CultureInfo culture) => null;
    }

    public class WarmthToColorConverter : IValueConverter
    {
        public object Convert(object value, Type targetType, object parameter, CultureInfo culture)
        {
            if (value is double v) return v >= 0 ? Brushes.Orange : Brushes.SkyBlue;
            return Brushes.Transparent;
        }
        public object ConvertBack(object value, Type targetType, object parameter, CultureInfo culture) => null;
    }

    public class NullToVisibilityConverter : IValueConverter
    {
        public static NullToVisibilityConverter Instance = new NullToVisibilityConverter();
        public object Convert(object value, Type targetType, object parameter, CultureInfo culture)
        {
            return value == null ? System.Windows.Visibility.Visible : System.Windows.Visibility.Collapsed;
        }
        public object ConvertBack(object value, Type targetType, object parameter, CultureInfo culture) => null;
    }
}