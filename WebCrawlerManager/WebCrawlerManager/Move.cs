using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace WebCrawlerManager
{
    class Move
    {
        public string Name { get; set; }
        public string CharacterID { get; set; }
        public string Command { get; set; } = null;
        public string HitLevel { get; set; }
        public string Damage { get; set; }
        public string StartUpFrame { get; set; }
        public string BlockFrame { get; set; }
        public string HitFrame { get; set; }
        public string CounterHitFrame { get; set; }
        public string MoveType { get; set; }
    }
}
