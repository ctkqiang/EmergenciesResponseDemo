using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace EmergenciesDemoMonitor
{
    internal partial class Constants
    {
        public static String userEndpoint = @"https://opensourceprojects-44592-default-rtdb.firebaseio.com/user.json"; 
        public static String emergenciesEndpoint = @"https://opensourceprojects-44592-default-rtdb.firebaseio.com/emergency.json?print=pretty";
    }
}
