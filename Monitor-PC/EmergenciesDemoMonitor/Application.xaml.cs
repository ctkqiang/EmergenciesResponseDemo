using EmergenciesDemoMonitor.utilities;
using Newtonsoft.Json.Linq;
using System;
using System.Net.Http;
using System.Net.Http.Headers;
using System.Text.RegularExpressions;
using System.Threading.Tasks;
using System.Windows;

namespace EmergenciesDemoMonitor
{
    public partial class Application : Window
    {
        public const int STATUS_CODE = 0xC8;
        public string UserUrl = Constants.userEndpoint;
        private static readonly HttpClient client = new HttpClient();

        public Application()
        {
            InitializeComponent();

            this.UserComponents();
        }

        public void UserComponents()
        {
            this.login.FontSize = 0xB;
            this.login.Content = "LOGIN";
        }

        public static bool IsEmailValid(String email)
        {
            if (!(Regex.IsMatch(
                email,
                @"^[a-zA-Z][\w\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\w\.-]*[a-zA-Z0-9]\.[a-zA-Z][a-zA-Z\.]*[a-zA-Z]$"
            )))
            {
                return false;
            }

            return true;
        }

        private async Task<string> GetUser(string Url)
        {


            client.DefaultRequestHeaders.Accept.Clear();
            client.DefaultRequestHeaders.Accept.Add(new MediaTypeWithQualityHeaderValue("application/json"));

            HttpResponseMessage? response = await client.GetAsync(Url);
            response.EnsureSuccessStatusCode();

            if (response.StatusCode == System.Net.HttpStatusCode.BadRequest)
            {
                Utilities.log(Message: "Status Not [OK]", IsDebug: true);
            }
            
            string? body = await response.Content.ReadAsStringAsync();

            JObject? data = JObject.Parse(body);

            Utilities.log(Message: data["4cc44069-6413-466b-a888-ba6738562086"].ToString(), IsDebug: true);

            return data.ToString();

        }

        private async void Login(object sender, RoutedEventArgs routedEventArgs)
        {
            Utilities.log(Message: "...login", IsDebug: false);

            if (this.loginEmail.Text.Length == 0x0)
            {
                this.errormessage.Text = "Your email address is required";
                this.loginEmail.Focus();

                Utilities.log(Message: "...email is empty", IsDebug: true);
            }
            else
            {
                if (!(IsEmailValid(this.loginEmail.Text)))
                {
                    this.errormessage.Text = "Your Email is invalid";
                    this.loginEmail.Select(start: 0x0, length: this.loginEmail.Text.Length);
                    this.loginEmail.Focus();

                    Utilities.log(Message: "...email is invalid", IsDebug: true);
                }

                await this.GetUser(Url: this.UserUrl);

            }
        }
    }
}
