package controllers.enums;

public enum NeighborXPosition {
	LEFT	(1 << 0, false),
	LEFT_AGNOSTIC(1 << 1, true),
	
	RIGHT (1 << 2, false),
	RIGHT_AGNOSTIC	(1 << 3, true),
	
	NEUTRAL(1 << 4, false),
	NEUTRAL_AGNOSTIC(1 << 5, true);

	private final int _value;
	private final boolean _agnostic;
	
	private NeighborXPosition(int value, boolean agnostic) {
		_value = value;
		_agnostic = agnostic;
	}
}
