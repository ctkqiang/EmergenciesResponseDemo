using System.Diagnostics;

namespace EmergenciesDemoMonitor.utilities
{
    public class Utilities
    {
        public Utilities()
        {
        }

        public static void log(string Message, bool IsDebug)
        {
            if (IsDebug)
            {
                Debug.WriteLine("[debug]: " + Message);
            }
            else
            {
                Debug.WriteLine("[info]: " + Message);
            }
        }
    }
}
