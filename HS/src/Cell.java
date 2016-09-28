public class Cell {
		
		public int i;
		public int j;
		public int value;
		
		private boolean crossed;
		private boolean canBeCrossed;
		
		public Cell(int i, int j, int value) {
			this.i = i;
			this.j = j;
			this.value = value;
			this.crossed = false;
			this.canBeCrossed = true;
		}
		
		public boolean isCrossed() {
			return this.crossed;
		}
		
		public boolean canBeCrossed() {
			return this.canBeCrossed;
		}
		
		public void crossOut() {
			this.crossed = true;
			this.canBeCrossed = false;
		}
		
		public void unCrossOut(boolean prevCanBeCrossed) {
			this.crossed = false;
			this.canBeCrossed = prevCanBeCrossed;
		}
		
		public void setUnCrossable() {
			this.canBeCrossed = false;
		}
		
		public void setCrossable() {
			this.canBeCrossed = true;
		}
		
		@Override
		public String toString() {
			return this.value + "";
		}
	}