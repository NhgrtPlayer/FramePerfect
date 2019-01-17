using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace WebCrawlerManager
{
    class Move
    {
        public string Id { get; set; } = "";
        public string CharacterID { get; set; } = "";
        public string Command { get; set; } = null;
        public string StartUp { get; set; } = "";
        public string Active { get; set; } = "";
        public string Recovery { get; set; } = "";
        public string FrameAdvantage { get; set; } = "";
        public string Damage { get; set; } = "";
        public string Guard { get; set; } = "";
        public string MoveType { get; set; } = "";
    }
}
