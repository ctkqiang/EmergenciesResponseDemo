using EmergenciesDemoMonitor.model;
using EmergenciesDemoMonitor.utilities;
using Nancy.Responses;
using ServiceStack;
using System;
using System.Collections.Generic;
using System.Net.Http;
using System.Text.Json;
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

        public void Refresh(object sender, RoutedEventArgs routedEventArgs)
        {
            Utilities.log(Message: "Refreshing...", IsDebug: false);

            Task? refresh = this.GetEmergenciesBroadcast();
            refresh.ContinueWith(t =>
            {
                MessageBox.Show("Ok");
            });
        }

        private async Task GetEmergenciesBroadcast()
        {
            try
            {
                HttpResponseMessage? response = await httpClient().GetAsync(Constants.emergenciesEndpoint);
                string jsonReponse = await response.Content.ReadAsStringAsync();

                var body = Constants.emergenciesEndpoint.GetJsonFromUrl().FromJson<Emergencies>();

                Emergencies emergencies = JsonSerializer.Deserialize<Emergencies>(json: jsonReponse);

                MessageBox.Show(jsonReponse.ToJson());

                Utilities.log(Message: body.ToJson(), IsDebug: true);
            }
            catch (Exception e)
            {
                Utilities.log(Message: e.ToString(), IsDebug: true);
            }
        }
    }
}
