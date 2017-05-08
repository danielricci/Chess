package controllers.enums;

public enum NeighborYPosition {
	TOP	(1 << 0, false),
	TOP_AGNOSTIC(1 << 1, true),
	
	BOTTOM (1 << 2, false),
	BOTTOM_AGNOSTIC	(1 << 3, true),
	
	NEUTRAL(1 << 4, false),
	NEUTRAL_AGNOSTIC(1 << 5, true);

	private final int _value;
	private final boolean _agnostic;
	
	private NeighborYPosition(int value, boolean agnostic) {
		_value = value;
		_agnostic = agnostic;
	}
	
	protected static NeighborYPosition flip(NeighborYPosition pos) {
		switch(pos) {
		case BOTTOM:
			return TOP;
		case TOP:
			return BOTTOM;
		default:
			return pos;
		}
	}
	
	protected static NeighborYPosition fromAgnostic(NeighborYPosition position) {
		switch(position) {
			case BOTTOM_AGNOSTIC:
			case TOP_AGNOSTIC:
			case NEUTRAL_AGNOSTIC:
			{
				int val = position._value >> 1;
				for(NeighborYPosition pos : NeighborYPosition.values()) {
					if(pos._value == val) {
						return pos;
					}
				}			
				break;
			}
		}
		System.out.println("Error with fromAgnostic");
		System.out.println(java.util.Arrays.toString((new Throwable()).getStackTrace()));
		return position;
	}
	
	protected boolean isAgnostic() {
		return _agnostic;
	}
	
	protected static NeighborYPosition toAgnostic(NeighborYPosition position) {
		switch(position) {
		case BOTTOM:
		case TOP:
			int val = position._value << 1;
			for(NeighborYPosition pos : NeighborYPosition.values()) {
				if(pos._value == val) {
					return pos;
				}
			}
			break;
		case BOTTOM_AGNOSTIC:
			break;
		case TOP_AGNOSTIC:
			break;
		default:
			break;
		}
		
		System.out.println("Error with toAgnostic");
		System.out.println(java.util.Arrays.toString((new Throwable()).getStackTrace()));
		return position;
	}
}
