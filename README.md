# Chess 

<img src="https://travis-ci.com/danielricci/Chess.svg?token=mMTpyEqHpouDpJNArzCm&branch=master" />

<h3>Setup</h3>
- Chess is played on a square board consisting of eight rows called ranks (1 - 8), and eight columns called files (a - h)
- The colors of the square starting at the top left of the board are alternating between light and dark
- The game pieces are divided into white and black sets
- Each player begin with 16 pieces
	○ 1 king
	○ 1 queen
	○ 2 rooks
	○ 2 bishops
	○ 2 knights
	○ 8 pawns

<h3>Movement</h3>
- White always moves first
- King moves one units to any square adjacent to its own square
- Rook moves on the far extremes of the axis
- Bishop moves on the diagonal
- Queen moves on the diagonal and the axis
- Knight has two units movement on one axis, and one unit movement on the other axis, in any combination and in any facing direction
- Pawns move up one unit in their facing direction, or two units if it is their first move
	
<h3>Castling</h3>
- Castling can only occur once per player per game
- Castling happens when there is no piece in between king and rook 
- You move the king two steps to your rook, and your castle to the other side of the king, this can be done on either side.  
- You cannot castle when you are in check
- You cannot castle through check 
- You cannot castle into check
- You cannot castle if you have moved your king
- If you move your rook, you can no longer castle with it.  So when a rook moves, you cannot castle with it.

<h3>En Passant</h3>
- When a pawn advances two units on its first move, if it could have been captured on the first square, then it can be captured
	
<h3>Promotion</h3>
- When a pawn advances to the 8th rank, it becomes promoted and must be exchanged for one of the following pieces
	○ Queen
	○ Rook
	○ Bishop
	○ Knight
- Based on the chosen piece, it is possible that there can now be for example two queens of the same color on the board.

<h3>Check</h3>
- When an opposing players piece is able to capture a king, the player is said to be in a check state
- The king must move to a valid king position that does not put the king in check again at that moved location
- The king can also capture the piece in question so long as the capture position does not result in another check
- Castling is not a permissible response to a check
- When the king can no longer perform a move out of check, the king is considered to be in checkmate, and that player loses the game
- Usually when a king is check, there is a notification that the king is in check
	
<h3>End of the Game</h3>
- The game ends when a player's king piece is in checkmate
