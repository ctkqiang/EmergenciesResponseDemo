using Aspose.Cells;
using Aspose.Cells.Utility;
using EmergenciesDemoMonitor.utilities;
using Microsoft.AspNet.SignalR.Json;
using System;
using System.Net.Http;
using System.Threading.Tasks;
using System.Windows;

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

        public Workbook workbook()
        {
            return new Workbook();
        }
        public void Download(object sender, RoutedEventArgs routedEventArgs)
        {
            Utilities.log(Message: "Download...", IsDebug: false);


            Task? refresh = this.GetEmergenciesBroadcast(1);
            refresh.ContinueWith(t =>
            {
                if (t is null) throw new ArgumentNullException(nameof(t));

                // MessageBox.Show("Ok");

                Utilities.log(Message: "ok", IsDebug: false);
            });
        }

        public void Refresh(object sender, RoutedEventArgs routedEventArgs)
        {
            Utilities.log(Message: "Refreshing...", IsDebug: false);

            Task? refresh = this.GetEmergenciesBroadcast(0);
            refresh.ContinueWith(t =>
            {
                if (t is null) throw new ArgumentNullException(nameof(t));

                // MessageBox.Show("Ok");

                Utilities.log(Message: "ok", IsDebug: false);
            });
        }

        [Obsolete]
        private async Task GetEmergenciesBroadcast(int mode)
        {
            Worksheet? worksheet = this.workbook().Worksheets[0];
            JsonLayoutOptions? layoutOptions = new JsonLayoutOptions();

            layoutOptions.ArrayAsTable = true;

            try
            {
                HttpResponseMessage? response = await this.httpClient().GetAsync(Constants.emergenciesEndpoint);

                string jsonReponse = await response.Content.ReadAsStringAsync();


                Utilities.log(Message: jsonReponse, IsDebug: true);

                if (mode == 0x0)
                {
                    this.result.Text = jsonReponse;
                }
                else
                {
                    Aspose.Cells.Utility.JsonUtility.ImportData(
                        json: jsonReponse,
                        cells: worksheet.Cells,
                        row: 0x0,
                        column: 0x0,
                        option: layoutOptions
                    );

                    workbook().Save("output.csv", SaveFormat.CSV);
                }
            }
            catch (Exception e)
            {
                Utilities.log(Message: e.ToString(), IsDebug: true);
            }
        }
    }
}
