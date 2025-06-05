package hex.rave;

import java.util.List;

import hex.model.Position;

public class SimulationResult {

        private boolean result;
        private List<Position> moves;
        
        public SimulationResult(boolean result, List<Position> moves) {
            this.result = result;
            this.moves = moves;
        }

        public List<Position> getMoves() {
            return moves;
        }

        public boolean getResult() {
            return result;
        }
}
