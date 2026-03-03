using System.Collections.ObjectModel;
using System.Windows;
using Microsoft.Win32;
using System.Windows.Media.Imaging;
using PhotoEditor.Models;
using PhotoEditor.Logic; 

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

        private void addImageClick(object sender, RoutedEventArgs e)
        {
            OpenFileDialog op = new OpenFileDialog { Multiselect = true, Filter = "Images|*.jpg;*.jpeg;*.png" };
            if (op.ShowDialog() == true)
            {
                foreach (string filename in op.FileNames)
                {
                    var original = new BitmapImage(new Uri(filename));
                    var gray = new FormatConvertedBitmap(original, System.Windows.Media.PixelFormats.Gray8, null, 0);

                    Photos.Add(new ImageLayer {
                        name = System.IO.Path.GetFileName(filename),
                        imageSource = original,
                        grayscaleSource = gray,
                        settings = new EffectSettings() 
                    });
                }
            }
        }

        private void copyStyleClick(object sender, RoutedEventArgs e)
        {
            if (ThumbnailList.SelectedItem is ImageLayer selected)
            {
                if (UsePatternToggle.IsChecked == true)
                {
                    _presetPrototype = selected.settings.clone();
                    StatusText.Text = "Паттерн: Объект склонирован";
                }
                else
                {
                    _presetPrototype = Logic.NoPatternProcessor.CreateManualCopy(selected.settings);
                    StatusText.Text = "Без паттерна: ручное копирование полей";
                }
            }
        }

        private void pasteStyleClick(object sender, RoutedEventArgs e)
        {
            if (ThumbnailList.SelectedItem is ImageLayer selected && _presetPrototype != null)
            {
                if (UsePatternToggle.IsChecked == true)
                {
                    selected.applySettings(_presetPrototype);
                    StatusText.Text = "Вставлено через паттерн Прототип";
                }
                else
                {
                    Logic.NoPatternProcessor.ManualApply(selected, _presetPrototype);
                    StatusText.Text = "Вставлено вручную";
                }
            }
        }

        private void savePhotoClick(object sender, RoutedEventArgs e)
        {
            if (ThumbnailList.SelectedItem == null)
            {
                MessageBox.Show("Сначала выберите и отредактируйте фото!");
                return;
            }

            SaveFileDialog saveDialog = new SaveFileDialog
            {
                Filter = "PNG Image|*.png|JPEG Image|*.jpg",
                FileName = "Результат_обработки"
            };

            if (saveDialog.ShowDialog() == true)
            {
                try
                {
                    var target = ActivePhotoContainer;

                    double width = target.ActualWidth;
                    double height = target.ActualHeight;

                    RenderTargetBitmap renderTarget = new RenderTargetBitmap(
                        (int)width, (int)height, 96, 96, System.Windows.Media.PixelFormats.Pbgra32);
                    renderTarget.Render(target);

                    BitmapEncoder encoder = saveDialog.FilterIndex == 1 ? new PngBitmapEncoder() : new JpegBitmapEncoder();
                    encoder.Frames.Add(BitmapFrame.Create(renderTarget));

                    using (var stream = System.IO.File.Create(saveDialog.FileName))
                    {
                        encoder.Save(stream);
                    }

                    MessageBox.Show("Фото успешно сохранено!");
                }
                catch (Exception ex)
                {
                    MessageBox.Show("Ошибка при сохранении: " + ex.Message);
                }
            }
        }
    }
}