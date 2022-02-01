using EmergenciesDemoMonitor.utilities;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Net.Http;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Shapes;

namespace EmergenciesDemoMonitor
{
    public partial class main : Window
    {
        public main()
        {
            InitializeComponent();
        }

        public HttpClient httpClient()
        {
            return new HttpClient();
        }

        private async Task GetEmergenciesBroadcast()
        {
            try
            {
                HttpResponseMessage? response = await httpClient().GetAsync(Constants.emergenciesEndpoint);
                string jsonReponse = await response.Content.ReadAsStringAsync();

                Utilities.log(Message: jsonReponse, IsDebug: true);
            }
            catch (Exception e)
            {
                Utilities.log(Message: e. ToString(), IsDebug: true);
            }
        }
    }
}
