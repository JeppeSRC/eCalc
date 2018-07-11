using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;

namespace eCalc {
    /// <summary>
    /// Interaction logic for MainWindow.xaml
    /// </summary>
    public partial class MainWindow : Window {
        public MainWindow() {
            InitializeComponent();

            Types[] types = new Types[(int)Types.NumTypes];

            for (int i = 0; i < (int)Types.NumTypes; i++) {
                types[i] = (Types)i;
            }

            cbType.ItemsSource = types;
            cbType.SelectedIndex = 0;

            
        }

        public enum Types {
            OpAmp_NonInverting,
            OpAmp_Inverting,
            OpAmp_SchmittTrigger,

            NumTypes
        };

        private void cbType_SelectionChanged(object sender, SelectionChangedEventArgs e) {

        }
    }
}
