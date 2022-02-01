using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace EmergenciesDemoMonitor.model
{
    public class Emergencies
    {
        
        public int id;
        public string? datetime 
        { 
            get; 
            set; 
        }

        public string? longitude 
        { 
            get; 
            set; 
        }

        public string? latitude 
        { 
            get; 
            set; 
        }
        
        public string? phone 
        { 
            get; 
            set; 
        }
    }
}
