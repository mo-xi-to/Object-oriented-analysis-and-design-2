using Microsoft.Win32;
using PhotoEditor.Models;
using System;
using System.Collections.ObjectModel;
using System.Windows;
using System.Windows.Media.Imaging;

namespace PhotoEditor
{
    public partial class MainWindow : Window
    {
        public ObservableCollection<ImageLayer> Photos { get; set; } = new ObservableCollection<ImageLayer>();
        
        private EffectSettings _presetPrototype;

        public MainWindow()
        {
            InitializeComponent();
            ThumbnailList.ItemsSource = Photos;
        }

        private void AddImage_Click(object sender, RoutedEventArgs e)
        {
            OpenFileDialog op = new OpenFileDialog { Multiselect = true, Filter = "Images|*.jpg;*.jpeg;*.png" };
            if (op.ShowDialog() == true)
            {
                foreach (string filename in op.FileNames)
                {
                    var original = new BitmapImage(new Uri(filename));
                    var gray = new FormatConvertedBitmap(original, System.Windows.Media.PixelFormats.Gray8, null, 0);

                    Photos.Add(new ImageLayer {
                        Name = System.IO.Path.GetFileName(filename),
                        ImageSource = original,
                        GrayscaleSource = gray,
                        Settings = new EffectSettings() 
                    });
                }
            }
        }

        private void CopyStyle_Click(object sender, RoutedEventArgs e)
        {
            if (ThumbnailList.SelectedItem is ImageLayer selected)
            {
                _presetPrototype = selected.Settings.Clone();
                
                StatusText.Text = "Стиль скопирован. Выберите другое фото в ленте и нажмите 'Вставить'.";
            }
            else
            {
                StatusText.Text = "Сначала выберите фото для копирования стиля.";
            }
        }

        private void PasteStyle_Click(object sender, RoutedEventArgs e)
        {
            if (ThumbnailList.SelectedItem is ImageLayer selected && _presetPrototype != null)
            {
                selected.ApplySettings(_presetPrototype);
                
                StatusText.Text = "Настройки применены!";
            }
            else if (_presetPrototype == null)
            {
                StatusText.Text = "Буфер пуст! Сначала нажмите 'Копировать'.";
            }
        }
    }
}