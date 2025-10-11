
package main;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import entity.Entity;
import entity.NPC;
import pokemon.Move;
import pokemon.Pokemon;
import pokemon.TypeChart;

public class BattleHandler {
	GamePanel gp;
	
	NPC opponent;
	//opponent is 'none' for wild pokemon.
	
	Pokemon pMon, oMon, target;
	
	String battlefield = "grass";
	
	Graphics2D g2;

	int userOption = 0, runAttempts = 1, userState, temp = -1, subState = 0, turnState = 0, nextMon;
	
	int charIndex = 0;
	String currentText = "";
	
	String playerMove, opponentMove;
	
	boolean canFlee, attackLanded, statusHappening = false, ailmentHappening = false, firstMover, isCrit, pMonFainted = false, oMonFainted = false, playerWon = false, opponentWon = false;
	double random;
	int damageDone = 0, newHP;
	
	long time;
	
	public BattleHandler(GamePanel gp) {
		this.gp = gp;
	}
	
	public void startNewBattle(NPC opponent) {
		
		this.opponent = opponent;
		
		gp.gameState = gp.battleState;
		
		userState = 9;
		
		gp.player.noMovement();
		
		pMon = gp.player.heldPokemon[0];
		oMon = opponent.heldPokemon[0];
		
	} 
	
	public void startWildBattle(Pokemon pkmn) {
		this.opponent = new NPC(gp);
		this.opponent.name = "pkmn";
		
		this.opponent.heldPokemon[0] = pkmn;
		
		gp.gameState = gp.battleState;
		
		userState = 0;
		
		gp.player.noMovement();
		pMon = gp.player.heldPokemon[0];
		oMon = opponent.heldPokemon[0];
	}

	public void drawBattle(Graphics2D g2) {
		this.g2 = g2;
		
		if(userState == 0) {
			
			drawBackground();

			drawPMon();
			drawOMon();
			
			drawOBar();
			drawPBar();
			
			
			drawText("What will " + pMon.name + " do?");
			
			temp = drawBattleOptions();
			
			if(temp != -1) {
				switch(temp) {
					case 0: userState = 1; break;
					case 1: userState = 2; break;
					case 2: userState = 3; break;
					case 3: userState = 4; break;
					case 4: userState = 0; break;
				}
				reset();
			}
		} 
		if(userState == 1){
			//fight
			drawBackground();
			drawPMon();
			drawOMon();
			
			drawOBar();
			drawPBar();
			
			drawText("What will " + pMon.name + " do?");
			
			temp = drawFightOptions();
			
			if(temp != -1) {
				userState = 5;
				switch(temp) {
					case 0: playerMove = pMon.currentMoves[0].name; break;
					case 1: playerMove = pMon.currentMoves[1].name; break;
					case 2: playerMove = pMon.currentMoves[2].name; break;
					case 3: playerMove = pMon.currentMoves[3].name; break;
					case 4: userState = 0; break;
				}
				reset();
			}
		}
		if (userState == 2) {
			//pokemon
			
			temp = drawSwitchScreen();
			
			if(temp != -1) {
				userState = 5; // should get overidden in case of case 6.
				switch(temp) {
					case 1: playerMove = "switch: 1"; break;
					case 2: playerMove = "switch: 2"; break;
					case 3: playerMove = "switch: 3"; break;
					case 4: playerMove = "switch: 4"; break;
					case 5: playerMove = "switch: 5"; break;
					case 6: userState = 0; break;
				}
				reset();
			}
		} 
		if(userState == 3) {
			//bag 
			System.out.println("no bag rn (just like currydooodoo)");
			userState = 0;
			reset();
		} 
		if(userState == 4) {
			//run
			if(!opponent.name.equals("none")) {
				userState = 5;
				playerMove = "run";
			} else {
				System.out.println("No running from trainer battle");
				userState = 0;
			}
			reset();
		}
		if(userState == 5) {
			//turn processing
			
			drawBackground();
			
			if(turnState == 0) {
				opponentMove = getOppMove();
				firstMover = getFirstMover();
				turnState ++;
			}
			if (turnState == 1) {
				if(firstMover) {
					if(processTurn(true)) turnState ++;
				} else {
					if(processTurn(false)) turnState ++;
				}
			}
			if(turnState == 2) {
				subState = 0;
				if(oMonFainted || pMonFainted) {
					userState = 6;
					turnState = 0;
				}
				else {
					turnState ++;
				}
				reset();
			}
			if(turnState == 3) {
				if(firstMover) {
					if(processTurn(false)) turnState ++;
				} else {
					if(processTurn(true)) turnState ++;
				}
			}
			if(turnState == 4) {
				subState = 0;
				turnState = 0;
				if(oMonFainted || pMonFainted) {
					userState = 6;
				} else {
					userState = 7;
				}
				reset();
			}
			
		}
		if(userState == 6) {
			//deal with fainting
			drawBackground();
			
			if(oMonFainted) {
				drawPMon();
				drawPBar();
				
				if(subState == 0) {
					drawOMon();
					drawOBar();
					
					drawText("The foe's " + oMon.name + " fainted.");
					if(gp.keyH.spacePressed) {
						gp.keyH.spacePressed = false;
						subState ++;
					}
				}
				
				if(subState == 1) {
					if(playerWon) {
						userState = 8;
					} else {
						nextMon = chooseNextMon();
						subState ++;
					}
				}
				
				if(subState == 2) {
					drawText(opponent.name + " sent out " + opponent.heldPokemon[nextMon].name);
					if(gp.keyH.spacePressed) {
						gp.keyH.spacePressed = false;
						oMon = opponent.heldPokemon[nextMon];
						subState = 0;
						userState = 0;
						oMonFainted = false;
					}
				}
			} 
			else if(pMonFainted) {
				drawOMon();
				drawOBar();
				
				if(subState == 0) {
					drawPMon();
					drawPBar();
					
					drawText(pMon.name + " fainted!");
					
					if(gp.keyH.spacePressed) {
						gp.keyH.spacePressed = false;
						subState ++;
						time = System.currentTimeMillis();
					}
				}
				if(subState == 1) {
					drawPMon();
					drawPBar();
						
					if(opponentWon) {
						userState = 8;
					} else if(System.currentTimeMillis() - time > 400) {
						subState ++;
					}
				}
				if(subState == 2) {
					temp = drawSwitchScreen();
					
					if(temp != - 1 && temp != 6) {
						subState ++;
					}
				}
				if(subState == 3) {
					Pokemon t = gp.player.heldPokemon[temp];
					gp.player.heldPokemon[temp] = gp.player.heldPokemon[0];
					gp.player.heldPokemon[0] = t;
					pMon = gp.player.heldPokemon[0];
					subState ++;
				}
				if(subState == 4) {
					drawText("You're in charge, " + pMon.name);
					if(gp.keyH.spacePressed) {
						gp.keyH.spacePressed = false;
						subState = 0;
						userState = 0;
						pMonFainted = false;
					}
				}
			}
			else {
				System.out.println("bro this code isnt supposed to be reachable");
			}
		}
		if(userState == 7) {
			//post turn state, deal with status
			
			drawBackground();

			drawPMon();
			drawOMon();
			
			drawOBar();
			drawPBar();
			
			if(subState == 0) {
				random = Math.random();
				subState ++;
			}
			if(subState == 1) {
				if(pMon.primaryAilment != null) {
					if(drawPostTurnAilmentText(true)) subState ++;
				} else {
					subState ++;
				}
			}
			if(subState == 2) {
				if(pMon.primaryAilment != null) {
					dealWithPostTurnAilment(false);
				} 
				subState ++;
			}
			if(subState == 3) {
				if(oMon.primaryAilment != null) {
					if(drawPostTurnAilmentText(false)) subState ++;
				} else {
					subState ++;
				}
			}
			if(subState == 4) {
				if(oMon.primaryAilment != null) {
					dealWithPostTurnAilment(false);
				} 
				subState = 0;
				userState = 0;
			}
		}
		if(userState == 8) {
			// win/loss state
			
			drawBackground();
			
			if(playerWon) {
				drawPMon();
				drawPBar();
				
				drawOpponent();
				
				drawText("You beat " + opponent.name + "!");
				
				if(gp.keyH.spacePressed) {
					opponent.defeated = true;
					gp.gameState = gp.playState;
					gp.keyH.spacePressed = false;
				}
			} 
			else if(opponentWon) {
				drawOMon();
				drawOBar();
				
				drawPlayer();
				
				drawText("You lost to " + opponent.name + ".");
				
				if(gp.keyH.spacePressed) {
					gp.gameState = gp.playState;
					gp.keyH.spacePressed = false;
					//add some system to bring to previous pokemon center.
				}
				
			}
			else {
				System.out.println("you should not be able to get here lil bro");
			}
			
		}
		if(userState == 9) {
			drawBackground();
			
			if(subState == 0) {
				drawOpponent();
				drawBallBar(false);
				drawText("You are challenged by " + opponent.name + "!");
				
				if(gp.keyH.spacePressed) {
					gp.keyH.spacePressed = false;
					subState ++;
				}
			}
			if(subState == 1) {
				drawOpponent();
				drawBallBar(false);
				drawText(opponent.name + "sent out " + oMon.name + "!");
				
				if(gp.keyH.spacePressed) {
					gp.keyH.spacePressed = false;
					subState ++;
				}
			}
			if(subState == 2) {
				drawOMon();
				drawPlayer();
				drawBallBar(true);
				drawText("Go! " + pMon.name + "!");
				
				if(gp.keyH.spacePressed) {
					gp.keyH.spacePressed = false;
					subState = 0;
					userState = 0;
				}
			}
			
		}
		//if(!isDone) isDone = drawText("This is what movies is made of. This is what movies is made of. Two months away, come back, two free throws. I got that.");
	}
	
	public boolean processTurn(boolean isUser) {
		
		if(isUser) {
			if(playerMove.equals("run")) {
				return flee();
			} else if(playerMove.length() > 8 && playerMove.substring(0, 8).equals("switch: ")) {
				int num = Integer.parseInt(playerMove.substring(8));	
				return switchMon(num);
			} else {
				return useMove(playerMove, true);
			}
		} else {
			return useMove(opponentMove, false); // add flee, switch, and bag upon changed battle AI which allows non - fighting moves. 
		}
		
	}
	
	public boolean useMove(String move, boolean isUser) {
		

		drawOMon();
		drawOBar();
		drawPMon();
		drawPBar();
		
		Move m = new Move(move);
		Pokemon mon = isUser ? pMon : oMon; //super diddymax tertiary statement!

		if(subState == 0) {
			switch(m.target.trim()) {	
				case "user": target = isUser ? pMon : oMon; break;
				default: target = isUser ? oMon : pMon;
			}
			
			random = Math.random();
			subState ++;
		}
		
		if(subState == 1) {
			if(drawPreTurnAilmentText(isUser)) subState ++;
			else subState ++;
		}
		
		if(subState == 2) {
			boolean canMove = dealWithPreTurnAilment(isUser);
			if(canMove) {
				subState ++;
			} else {
				return true;
			}
		}
		
		if(subState == 3) {
			drawText(mon.name + " used " + move + "!");
			if(gp.keyH.spacePressed) {
				gp.keyH.spacePressed = false;
				subState ++;
			}
		} 
		
		if(subState == 4) {
			attackLanded = Math.random() < m.accuracy;
			if(m.accuracy == 0) attackLanded = true; //no move in the game actually has 0 accuracy, but moves that always hit have this value.
			if((int) (Math.random()*16) == 0) {
				isCrit = true;
			} else {
				isCrit = false;
			}
			if(m.power != 0) damageDone = calcDamage(mon, target, m, isCrit);
			newHP = target.hp - damageDone;
			if(newHP < 0) newHP = 0;
			subState ++;
		} 
		
		if(attackLanded) {
			
			if(subState == 5) {

				if(m.power != 0) {
					if(changeHP(newHP, target)) {
						subState ++;
					}
					checkFaint(target.equals(pMon)); // target.equals(pMon) is true if user faint needs to be checked
				}
				
				else subState ++;
			}
			
			if(subState == 6) {
				if(m.power != 0 && isCrit) {
					drawText("A critical hit!");
					if(gp.keyH.spacePressed) {
						gp.keyH.spacePressed = false;
						subState ++;
					}
				} else {
					subState ++;
				}
			}
			
			if(subState == 7) {
				if(m.power != 0) {
					double x = TypeChart.getEffectiveness(m, target);
					if(x == 1) {
						subState ++;
					} else {
						if(x == 2.0) {
							drawText("It was super effective!");
						}
						else if(x == 4.0) {
							drawText("It was super duper effective!");
						}
						else if (x == .5) {
							drawText("It was not very effective.");
						}
						else if (x == .25) {
							drawText("It was barely nothing.");
						}
						else if (x == 0) {
							drawText("It doesn't affect " + target.name + ".");
						}
						if(gp.keyH.spacePressed) {
							gp.keyH.spacePressed = false;
							subState ++;
						}
					}
				} else {
					subState ++;
				}
			}
			
			if(subState == 8) {
				
				switch(m.statusTarget) {
					case "user": target = isUser ? pMon : oMon; break;
					default: target = isUser ? oMon : pMon;
				}
				
				double effectiveChance = m.statusChance;
				
				if(effectiveChance == 0) {
					effectiveChance = 100;
				}
				
				if(Math.random() * 100 < effectiveChance) {
					
					if(m.atkC != 0 || m.spcAtkC != 0 || m.spdC != 0 || m.defC != 0 || m.spcDefC != 0) {
						
						statusHappening = true;
						
						//replace target with status target upon expansion of move data files
						if(m.atkC != 0) {
							target.atkM += m.atkC; 
						} 
						if(m.spcAtkC != 0) { 
							target.spcAtkM += m.spcAtkC;
						} 
						if(m.spdC != 0) { 
							target.spdM += m.spdC;
						} 
						if(m.defC != 0) { 
							target.defM += m.defC;
						} 
						if(m.spcDefC != 0) { 
							target.spcDefM += m.defC;
						} 
						
						subState ++;
						
					} else {
						subState = 14;
					}
					
				} else {
					subState = 14;
				}
			}
			
			if(subState == 9) {
				if(m.atkC == 0) subState ++;
				if(m.atkC < -1) {
					drawText(target.name + "'s attack fell sharply.");
				} else if(m.atkC == -1) {
					drawText(target.name + "'s attack fell.");
				} else if (m.atkC == 1) {
					drawText(target.name + "'s attack rose!");
				} else if(m.atkC > 1) {
					drawText(target.name + "'s attack rose sharply!");
				}
				if(gp.keyH.spacePressed) {
					gp.keyH.spacePressed = false;
					subState ++;
				}
			}
			if(subState == 10) {
				if(m.spcAtkC == 0) subState ++;
				if(m.spcAtkC < -1) {
					drawText(target.name + "'s special attack fell sharply.");
				} else if(m.spcAtkC == -1) {
					drawText(target.name + "'s special attack fell.");
				} else if (m.spcAtkC == 1) {
					drawText(target.name + "'s special attack rose!");
				} else if(m.spcAtkC > 1) {
					drawText(target.name + "'s special attack rose sharply!");
				}
				if(gp.keyH.spacePressed) {
					gp.keyH.spacePressed = false;
					subState ++;
				}
			}
			if(subState == 11) {
				if(m.defC == 0) subState ++;
				if(m.defC < -1) {
					drawText(target.name + "'s defense fell sharply.");
				} else if(m.defC == -1) {
					drawText(target.name + "'s defense fell.");
				} else if (m.defC == 1) {
					drawText(target.name + "'s defense rose!");
				} else if(m.defC > 1) {
					drawText(target.name + "'s defense rose sharply!");
				}
				if(gp.keyH.spacePressed) {
					gp.keyH.spacePressed = false;
					subState ++;
				}
			}
			if(subState == 12) {
				if(m.spcDefC == 0) subState ++;
				if(m.spcDefC < -1) {
					drawText(target.name + "'s special defense fell sharply.");
				} else if(m.spcDefC == -1) {
					drawText(target.name + "'s special defense fell.");
				} else if (m.spcDefC == 1) {
					drawText(target.name + "'s special defense rose!");
				} else if(m.spcDefC > 1) {
					drawText(target.name + "'s special defense rose sharply!");
				}
				if(gp.keyH.spacePressed) {
					gp.keyH.spacePressed = false;
					subState ++;
				}
			}
			if(subState == 13) {
				if(m.spdC == 0) subState ++;
				if(m.spdC < -1) {
					drawText(target.name + "'s speed fell sharply.");
				} else if(m.spdC == -1) {
					drawText(target.name + "'s speed fell.");
				} else if (m.spdC == 1) {
					drawText(target.name + "'s speed rose!");
				} else if(m.spdC > 1) {
					drawText(target.name + "'s speed rose sharply!");
				}
				if(gp.keyH.spacePressed) {
					gp.keyH.spacePressed = false;
					subState ++;
				}
			}
			
			if(subState == 14) {
				
				double effectiveChance = m.ailmentChance;
				
				if(effectiveChance == 0) {
					effectiveChance = 100;
				}
				
				
				target = isUser ? oMon : pMon; // as per ailment target so far, the target is always the opponent.
				
				if(Math.random() * 100 < effectiveChance) {
					
					if(m.ailment != null && !m.ailment.trim().equals("none")) {
						
						ailmentHappening = true;
						
						//once again switch definition of target in the futur.e
						switch(m.ailment) {
							case "poison": target.primaryAilment = "poison"; break;
							case "bad-poison": target.primaryAilment = "bad-poison"; target.turnsPoisoned = 1; break;
							case "freeze": target.primaryAilment = "freeze"; break;
							case "burn": target.primaryAilment = "burn"; break;
							case "sleep": target.primaryAilment = "sleep"; target.sleepGoal = (int) (Math.random() * 3) + 1; break; 
							case "paralysis": target.primaryAilment = "paralysis"; break;
							case "infatuation": target.volatileAilments.add("infatuation"); break;
							case "flinch": target.volatileAilments.add("infatuation"); break;
							case "confusion": target.volatileAilments.add("confusion"); target.confusionGoal = (int) (Math.random() * 4) + 1; break;
						}
						
					} 
				}
				
				subState ++;
			}
			
			if(subState == 15) {
				if(ailmentHappening) {
					switch (m.ailment) {
					    case "poison":
					        drawText(target.name + " was poisoned!");
					        break;
					    case "bad-poison":
					        drawText(target.name + " was badly poisoned!");
					        break;
					    case "freeze":
					        drawText(target.name + " was frozen solid!");
					        break;
					    case "burn":
					        drawText(target.name + " was burned!");
					        break;
					    case "sleep":
					        drawText(target.name + " fell asleep!");
					        break;
					    case "paralysis":
					        drawText(target.name + " is paralyzed! It may be unable to move!");
					        break;
					    case "infatuation":
					        drawText(target.name + " fell in love!");
					        break;
					    case "flinch":
					        drawText(target.name + " flinched and couldn't move!");
					        break;
					    case "confusion":
					        drawText(target.name + " became confused!");
					        break;
					    default:
					    	subState ++;
					    	break;
					}
	
					if(gp.keyH.spacePressed) {
						gp.keyH.spacePressed = false;
						subState ++;
					}
				} else {
					subState ++;
				}
			}
			
			if(subState ==  16) {
				
				target = isUser ? pMon : oMon; //after this, the target is always the user of the move.
				
				if(m.drain != 0) {
					if(m.drain > 0) {
						drawText(target.name + " regained health!");
					} else if(m.drain < 0) {
						drawText(target.name + " is damaged by recoil!");
					}
					if(gp.keyH.spacePressed) {
						gp.keyH.spacePressed = false;
						subState ++;
						newHP = (int) (target.hp + damageDone * m.drain);
						if(newHP > target.hlt) newHP = target.hlt;
					}
				} else {
					subState = 18;
				}
			}
			
			if(subState == 17) {
				if(m.drain != 0) {
					if(changeHP(newHP, target)) {
						checkFaint(target.equals(pMon));
						subState ++;
					}
				}
				else subState ++;
			}
			
			if(subState == 18) {
				if(m.recoil != 0) {
					if(m.recoil > 0) {
						drawText(target.name + " restored health!");
					} else if(m.recoil < 0) {
						drawText(target.name + " is damaged by recoil!");
					}
					if(gp.keyH.spacePressed) {
						gp.keyH.spacePressed = false;
						subState ++;
						newHP = (int) (target.hp + (target.hlt * m.recoil));
						if(newHP > target.hlt) newHP = target.hlt;
					}
				} else {
					subState = 20;
				}	
			}
			
			if(subState == 19) {
				if(m.recoil != 0) {
					if(changeHP(newHP, target)) {
						checkFaint(target.equals(pMon));
						subState ++;
					}
				}
				else subState ++;
			}
			
			if(subState == 20) {
				if(m.healing != 0) {
					if(m.healing > 0) {
						drawText(target.name + " restored health!");
					} else if(m.recoil < 0) {
						drawText(target.name + " is damaged by recoil!");
					}
					if(gp.keyH.spacePressed) {
						gp.keyH.spacePressed = false;
						subState ++;
						newHP = (int) (target.hp + (target.hlt * m.healing));
						if(newHP > target.hlt) newHP = target.hlt;
					}
				} else {
					subState = 22;
				}	
			}
			
			if(subState == 21) {
				if(m.healing != 0) {
					if(changeHP(newHP, target)) {
						checkFaint(target.equals(pMon));
						subState ++;
					}
				}
				else subState ++;
			}
			
		} else if(subState == 5) {
			drawText("The move missed.");
			if(gp.keyH.spacePressed) {
				gp.keyH.spacePressed = false;
				subState = 22;
			}
		}
		
		if(subState == 22) {
			for(Move i : mon.currentMoves) {
				if(i != null && i.name.equals(move)) {
					i.currentPP --;
				}
			}
			return true;
		}
		
		return false;
	}

	public boolean drawPreTurnAilmentText(boolean isUser) {
		Pokemon mon = isUser ? pMon : oMon;
		
		//make most of these ailments only freedze the player less than 100% of the time.
		
		if(mon.primaryAilment != null) {
			switch(mon.primaryAilment) {
				case "sleep": drawText(mon.name + " is sound asleep."); break;
				case "paralysis": if(random < .25) drawText(mon.name + " could not move in it's paralysis."); break;
				case "freeze": drawText(mon.name + " is frozen solid."); break;
			}
		}
		else if(!mon.volatileAilments.isEmpty()) {
			for(String i : mon.volatileAilments) {
				switch(i) {
					case "confusion": if(random < 1.0/3) drawText(mon.name + " hit itself in it's confusion."); break; 
					case "infatuation": if(random < .5) drawText(mon.name + " was immmobilized by love."); break; 
					case "flinch": /*drawText(mon.name + " flinched. It couldn't move"); Just say nothing for this one*/ break;
				}
			}
		} 
		else {
			return true;
		}
		
		if(gp.keyH.spacePressed) {
			gp.keyH.spacePressed = false;
			return true;
		}
		
		return false; 
	}
	
	public boolean dealWithPreTurnAilment(boolean isUser) {
		//returns false if unable to move.
		Pokemon mon = isUser ? pMon : oMon;
		
		if(mon.primaryAilment != null) {
			switch(mon.primaryAilment) {
				case "sleep": return false;
				case "paralysis": {
					if(random < .25) return false; 
					break;
				}
				case "freeze": return false;
			}
		}
		else if(!mon.volatileAilments.isEmpty()) {
			
			for(String i : mon.volatileAilments) {
				switch(i) {
					case "confusion": {
						if(random < 1.0 / 3) {
							mon.hp -= 50;  //remove different amount of health.
							return false; 
						}
						break;
					}
					case "infatuation": if(random < .5) return false; break; 
					case "flinch": return false; /*drawText(mon.name + " flinched. It couldn't move"); Just say nothing for this one*/ 
				}
			}
		} 
		
		checkFaint(isUser);
		
		return true;
	}
	
	public boolean drawPostTurnAilmentText(boolean isUser) {
		
		Pokemon mon = isUser ? pMon : oMon;
		
		switch(mon.primaryAilment) {
			case "poison": {
				drawText(mon.name + " is hurt by poison."); break;
			}
			case "bad-poison": {
				drawText(mon.name + " is hurt by poison."); break;
			}
			case "burn": {
				drawText(mon.name + " is hurt by its burn."); break;
			}
			case "freeze": {
				if(random < .2) {
					drawText(mon.name + " thawed out.");
				}
				break;
			}
			case "sleep": {
				mon.sleepCount ++;
				if(mon.sleepCount >= mon.sleepGoal) {
					drawText(mon.name + " woke up.");
				}
				break;
			}
		}
		for(String i : mon.volatileAilments) {
			switch(i) {
				case "confusion": {
					mon.confusionCount ++;
					if(mon.confusionCount >= mon.confusionGoal) {
						drawText(mon.name + " knocked out of it's confusion.");
					}
					break;
				}
			}
		}
		if(gp.keyH.spacePressed) {
			gp.keyH.spacePressed = false;
			return true;
		}
		return false;
	}
	
	public void dealWithPostTurnAilment(boolean isUser) {
		Pokemon mon = isUser ? pMon : oMon;
		
		switch(mon.primaryAilment) {
			case "poison": mon.hp -= mon.hlt / 8; break;
			case "bad-poison": mon.hp -= mon.turnsPoisoned * mon.hlt / 16; break;
			case "burn": mon.hp -=  mon.hlt / 16; break;
			case "freeze": if(random < .2) mon.primaryAilment = null; break;
			case "sleep": if(mon.sleepCount >= mon.sleepGoal) mon.primaryAilment = null; break;
		}
		
		for(String i : mon.volatileAilments) {
			switch(i) {
				case "confusion": if(mon.confusionCount >= mon.confusionGoal) mon.primaryAilment = null; break;
			}
		}
	}
	
	public void drawBallBar(boolean isUser) {
		BufferedImage bar = null, fullBall = null, faintedBall = null, unhealthyBall = null;
		
		try {
			bar = isUser ? ImageIO.read(getClass().getResourceAsStream("/battle/pBallbar.png")) : ImageIO.read(getClass().getResourceAsStream("/battle/oBallbar.png"));
			fullBall = ImageIO.read(getClass().getResourceAsStream("/battle/fullBall.png"));
			faintedBall = ImageIO.read(getClass().getResourceAsStream("/battle/faintedBall.png"));
			unhealthyBall = ImageIO.read(getClass().getResourceAsStream("/battle/unhealthyBall.png"));
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		int x, y;
		if(isUser) {
			x = 360;
			y = 340;
		} else {
			x = 0;
			y = 102;
		}
		
		g2.drawImage(bar, x, y, 408, 57, null);
		
		for(int i = 0; i < 6; i++) {
			
			Pokemon mon = isUser ? gp.player.heldPokemon[i] : opponent.heldPokemon[i];
					
			if(mon != null) {
				
				BufferedImage ball;
				if(mon.hp == 0) {
					ball = faintedBall;
				} else if(mon.primaryAilment != null) {
					ball = unhealthyBall;
				} else {
					ball = fullBall;
				}
				
				if(isUser) {
					g2.drawImage(ball, 696 - (i * 45), 340, 42, 42, null);
				} else {
					g2.drawImage(ball, 30 + (i * 45), 102, 42, 42, null);
				} 
				
			}
		}		
	}
	
	public void drawOpponent() {
		BufferedImage image = opponent.battleSprite;
		
		if(image != null) {
			int width = image.getWidth() * 4;
			int height = image.getHeight() * 4;
			
			g2.drawImage(image, 560 - width / 2, 280 - height, image.getWidth() * 4, image.getHeight() * 4, null); 
		}
	}
	
	public void drawPlayer() {
		BufferedImage image = gp.player.battleSprite;
		
		int width = image.getWidth() * 4;
		int height = image.getHeight() * 4;
		
		g2.drawImage(image, 160 - width / 2, 576 - height, image.getWidth() * 4, image.getHeight() * 4, null); 
	}
	
	public int chooseNextMon() {
		
		int ret = 0;
		double eMax = 0;
		
		for(int i = 1; i < 6; i++) {
			Pokemon mon = opponent.heldPokemon[i];
			if(mon != null && mon.hp != 0) {
				double e = TypeChart.getEffectiveness(mon, pMon);
				if(e > eMax) {
					eMax = e;
					ret = i;
					
				}
			}
		}
		
		return ret;
	}
	
	public void reset() {
		subState = 0; 
		userOption = 0;
		ailmentHappening = false;
		statusHappening = false;
	}
		
	public void checkFaint(boolean isUser) {
		Pokemon mon = isUser ? pMon : oMon;
		if(mon.hp <= 0) {
			mon.hp = 0;
			if(isUser) {
				pMonFainted = true;
				int numAlive = 0;
				for(int i = 0; i < 6; i++) {
					Pokemon m = gp.player.heldPokemon[i];
					if(m != null && m.hp != 0) {
						numAlive ++;
					}
				}
				if(numAlive == 0) {
					opponentWon = true;
					
				}
			}
			else {
				oMonFainted = true;
				int numAlive = 0;
				for(int i = 0; i < 6; i++) {
					Pokemon m = opponent.heldPokemon[i];
					if(m != null && m.hp != 0) {
						numAlive ++;
					}
				}
				if(numAlive == 0) {
					playerWon = true;
				}
			}
		}
	}

	public boolean switchMon(int num) {
		
		drawOMon();
		drawOBar();
		
		if(subState == 0) {
			drawText("Come back, " + pMon.name + "!");
			if(gp.keyH.spacePressed) {
				gp.keyH.spacePressed = false;
				subState ++;
			}
		}
		if (subState == 1) {
			
			Pokemon temp = gp.player.heldPokemon[0];
			gp.player.heldPokemon[0] = gp.player.heldPokemon[num];
			gp.player.heldPokemon[num] = temp;
			pMon = gp.player.heldPokemon[0];
			
			subState ++;
		} 
		if(subState == 2) {

			drawPMon();
			drawPBar();
			
			drawText("I choose you, " + pMon.name + "!");
			
			if(gp.keyH.spacePressed) {
				gp.keyH.spacePressed = false;
				return true;
			}
		}
		
		return false;
	}
	
	public void deployPokemon(int num) {
		//deal with tihs later
	}
	
	public boolean getFirstMover() {
		if(playerMove.equals("run")) {
			return true;
		} else if(playerMove.length() > 7 && playerMove.substring(0, 8).equals("switch: ")) {
			return true;
		} else {
			Move pMove = new Move(playerMove);
			Move oMove = new Move(opponentMove);
			
			int pPrty = pMove.priority;
			int oPrty = oMove.priority;
			
			int pSpd = pMon.getEffectiveStat("speed");
			int oSpd = oMon.getEffectiveStat("speed");
			
			if(pPrty > oPrty) {
				return true;
			} else if (oPrty > pPrty) {
				return false;
			} else {
				if(pSpd > oSpd) {
					return true;
				} else if (oSpd > pSpd) {
					return false;
				} else {
					return Math.random() < .5;
				}
			}
			
		}
	}
	
	public String getOppMove() {
		
		int mostDamage = 0, moveNum = 0, moveCount = 0;
	
		for(int i = 0; i < 4; i++) {
			
			Move move = oMon.currentMoves[i];
			if(move != null && move.power != -1) {
				moveCount ++;
				
				int dmg = calcDamage(oMon, pMon, move, false);
				if(dmg > mostDamage) {
					mostDamage = dmg;
					moveNum = i;
				}
			}
			
		}
		
		int rand = (int)(Math.random());
		
		if(rand == 0) {
			return oMon.currentMoves[(int) (Math.random() * moveCount)].name;
		} else {
			if(mostDamage != 0) {
				return oMon.currentMoves[moveNum].name;
			} else {
				return oMon.currentMoves[(int) (Math.random() * moveCount)].name;
			}
		}
		
	}
	
	public boolean flee() {
		
		drawPMon();
		drawOMon();
		
		drawOBar();
		drawPBar();
		
		if(subState == 0) {
			canFlee = canFlee();
			subState ++;
		}
		if(subState == 1) {
			if(canFlee) {
				drawText(pMon.name + " ran away!");
			} else {
				drawText(pMon.name + " was not able to run away");
			}
			if(gp.keyH.spacePressed) {
				gp.keyH.spacePressed = false;
				return true;
			}
		}
		return false;
	}
	
	public boolean canFlee() {
		
		//F = ((Your Pokémon's Speed * 32) / Wild Pokémon's Speed) + 30 * Run_Attempts
		
		double F = (1.0 * (pMon.spd * 32) / oMon.spd) + 30 * runAttempts;
		
		int rand = (int) (Math.random() * 256);

		if(F > rand) return true;
		return false;
	}

	public int calcDamage(Pokemon attacker, Pokemon defender, Move move, boolean isCrit) {
		//Damage = (((2 * Level / 5 + 2) * Power * A / D) / 50 + 2) × Modifier
		//A = attacker atk/spcAtk
		//D = defender def/spcDef
		//Modifier = STAB × TypeEffectiveness × Critical × Random Roll × Other
		//Other = atk bonuses, items, weather etc. Not for rn. 
		
		int A, D;
		if(move.attackType.equals("physical")) {
			A = attacker.getEffectiveStat("attack");
			D = defender.getEffectiveStat("defense");
		} else {
			A = attacker.getEffectiveStat("special attack");
			D = defender.getEffectiveStat("special defense");
		}
		
		int levelFactor = (2 * attacker.lvl) / 5 + 2;
	    int baseDamage = ((levelFactor * move.power * A) / D) / 50 + 2;

	    double modifier = 1.0;

	    // STAB
	    if (move.type.equals(attacker.primaryType) ||
	        (attacker.secondaryType != null && move.type.equals(attacker.secondaryType))) {
	        modifier *= 1.5;
	    }

	    // Critical hit
	    if (isCrit) modifier *= 1.5;  

	    // Random factor (between 0.85 and 1.00)
	    double roll = 0.85 + Math.random() * 0.15;
	    modifier *= roll;

	    // Type effectiveness
	    double typeEffectiveness = pokemon.TypeChart.getEffectiveness(move, defender);
	    modifier *= typeEffectiveness;

	    if (typeEffectiveness == 0) return 0;

	    int damage = (int) (baseDamage * modifier);
	    return Math.max(1, damage);
	}
	
	public int drawSwitchScreen() {
		BufferedImage unselected = null, selected = null, unselectedFirst = null, selectedFirst = null, empty = null, bg = null, male = null, female = null, bottomBar = null;
		try {
			unselected = ImageIO.read(getClass().getResourceAsStream("/battle/unselectedSlot.png"));
			selected = ImageIO.read(getClass().getResourceAsStream("/battle/selectedSlot.png"));
			unselectedFirst = ImageIO.read(getClass().getResourceAsStream("/battle/unselectedFirstSlot.png"));
			selectedFirst = ImageIO.read(getClass().getResourceAsStream("/battle/selectedFirstSlot.png"));
			empty = ImageIO.read(getClass().getResourceAsStream("/battle/emptySlot.png"));
			bg = ImageIO.read(getClass().getResourceAsStream("/battle/bg.png"));
			male = ImageIO.read(getClass().getResourceAsStream("/battle/male.png"));
			female = ImageIO.read(getClass().getResourceAsStream("/battle/female.png"));
			bottomBar = ImageIO.read(getClass().getResourceAsStream("/battle/partyTextBar.png"));
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		g2.drawImage(bg, 0, 0, 768, 576, null);
		g2.drawImage(bottomBar, 0, 486, 768, 90, null);

		int y = 29;
		int x = 3;
		
		for(int i = 0; i < 6; i++) {
			Pokemon mon = gp.player.heldPokemon[i];
			
			if(mon != null) {
				
				setOpacity(.7f);
				
				if(i ==  1) { 
					if(i == userOption) {
						g2.drawImage(selectedFirst, x, y, 378, 138, null);
					} else {
						g2.drawImage(unselectedFirst, x, y, 378, 138, null);
					}
				} {
					if(i == userOption) {
						g2.drawImage(selected, x, y, 378, 138, null);
					} else {
						g2.drawImage(unselected, x, y, 378, 138, null);
					}
				}
				
				setOpacity(1f);
				
				BufferedImage img = mon.icon;
				int width = img.getWidth() * 3;
				int height = img.getHeight() * 3;
				g2.drawImage(img, x + 64 - width / 2, y + 52 - height / 2, width, height, null);
				
				drawOutlinedString("Lv. " + mon.lvl, x + 24, y + 124, 3, 38, Color.white, Color.gray);
				drawOutlinedString(mon.hp + "/" + mon.hlt, x + 168, y + 124, 3, 38, Color.white, Color.gray);
				
				drawHPBar(x + 135, y + 72, mon);
				
				drawOutlinedString(mon.name, x + 120, y + 63, 4, 48, Color.white, Color.gray); 
				
				if (mon.gender.equals("male")) {
					g2.drawImage(male, x + 312, y + 33, 18, 30, null);
				} else if (mon.gender.equals("female")){
					g2.drawImage(female, x + 312, y + 33, 18, 30, null);
				}
				
			} else {
				
				setOpacity(.7f);

				g2.drawImage(empty, x, y, 378, 138, null);
				
				setOpacity(1f);
				
			}
			
			if(x == 387) {
				x = 3;
				y += 120;
			} else {
				x += 384;
				y += 24;
			}
			
		}
		
		drawOutlinedString("Choose a pokemon.", 32, 540, 4, 48, Color.DARK_GRAY, Color.gray);
		drawOutlinedString("Press r to exit.", 576, 540, 4, 36, Color.white, Color.gray);
		
		int currOption = userOption;
		
		if(gp.keyH.leftPressed || gp.keyH.rightPressed) {		
			if(userOption % 2 == 0) userOption ++;
			else userOption --;		
			gp.keyH.leftPressed = false;
			gp.keyH.rightPressed = false;
		}
		if (gp.keyH.downPressed) {
			if(userOption < 4) userOption += 2;
			else userOption -= 4;
			gp.keyH.downPressed = false;
		}
		if(gp.keyH.upPressed) {
			if(userOption < 2) userOption += 4;
			else userOption -= 2;
			gp.keyH.upPressed = false;
		}
		
		if(gp.player.heldPokemon[userOption] == null) {
			userOption = currOption;
		}
		
		if(gp.keyH.spacePressed) {
			gp.keyH.spacePressed = false;
			if(gp.player.heldPokemon[userOption].hp != 0) {
				switch(userOption) {
					case 0: System.out.println("You cant change into the same pokemon"); break;
					case 1: return 1;
					case 2: return 2;
					case 3: return 3;
					case 4: return 4;
					case 5: return 5;
				}
			} else {
				System.out.println("This pokemon has 0 hp. You cant switch into it.");
			}
		}
		if(gp.keyH.rPressed) {
			gp.keyH.rPressed = false;
			System.out.println("here");
			return 6;
		}
		
		return - 1;
	}
	
	public void drawSummary() {
		drawBackground();
		drawOMon();
		drawPMon();
		
		setOpacity(.5f);
		
		g2.setColor(new Color(20, 20, 20));
		g2.fillRect(0, 0, 768, 576);
		
		setOpacity(.1f);
	}
	
	public void setOpacity(float x) {
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, x));
	}
	
	public int drawBattleOptions() {
		BufferedImage fight = null, run = null, bag = null, pokemon = null, selectionCrosshair = null;
		
		try {
			fight = ImageIO.read(getClass().getResourceAsStream("/battle/fight.png"));
			run = ImageIO.read(getClass().getResourceAsStream("/battle/run.png"));
			bag = ImageIO.read(getClass().getResourceAsStream("/battle/bag.png"));
			pokemon = ImageIO.read(getClass().getResourceAsStream("/battle/pokemon.png"));
			selectionCrosshair = ImageIO.read(getClass().getResourceAsStream("/battle/selectionCrosshair.png"));
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		g2.drawImage(fight, 376, 432, 159, 55, null);
		g2.drawImage(pokemon, 588, 432, 159, 55, null);
		g2.drawImage(bag, 376, 496, 159, 55, null);
		g2.drawImage(run, 588, 496, 159, 55, null);
		
		if(gp.keyH.leftPressed || gp.keyH.rightPressed) {
			if(userOption % 2 == 0) userOption ++;
			else userOption --;
			gp.keyH.leftPressed = false;
			gp.keyH.rightPressed = false;
		}
		if (gp.keyH.downPressed || gp.keyH.upPressed) {
			if(userOption > 1) userOption -= 2;
			else userOption += 2;
			gp.keyH.downPressed = false;
			gp.keyH.upPressed = false;
		}
		if(gp.keyH.rPressed) {
			gp.keyH.rPressed = false;
			return 4;
		}
			
		switch(userOption) {
		case 0: g2.drawImage(selectionCrosshair, 376, 432, 159, 55, null); break;
		case 1: g2.drawImage(selectionCrosshair, 588, 432, 159, 55, null); break;
		case 2: g2.drawImage(selectionCrosshair, 376, 496, 159, 55, null); break;
		case 3: g2.drawImage(selectionCrosshair, 588, 496, 159, 55, null); break;
		}
		
		if(gp.keyH.spacePressed) {
			gp.keyH.spacePressed = false;
			switch(userOption) {
			case 0: return 0;
			case 1: return 1;
			case 2: return 2;
			case 3: return 3;
			}
		}
		
		return -1;
	}
	
	public int drawFightOptions() {
		
		BufferedImage selectionCrosshair = null;
		
		try {
			selectionCrosshair = ImageIO.read(getClass().getResourceAsStream("/battle/selectionCrosshair.png"));
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		drawMove(pMon.currentMoves[0], 376, 432);
		drawMove(pMon.currentMoves[1], 588, 432);
		drawMove(pMon.currentMoves[2], 376, 496);
		drawMove(pMon.currentMoves[3], 588, 496);
		
		if(gp.keyH.leftPressed || gp.keyH.rightPressed) {
			if(userOption % 2 == 0) userOption ++;
			else userOption --;
			gp.keyH.leftPressed = false;
			gp.keyH.rightPressed = false;
		}
		if (gp.keyH.downPressed || gp.keyH.upPressed) {
			if(userOption > 1) userOption -= 2;
			else userOption += 2;
			gp.keyH.downPressed = false;
			gp.keyH.upPressed = false;
		}
		if(gp.keyH.rPressed) {
			gp.keyH.rightPressed = false;
			return 4;
		}
		
		switch(userOption) {
		case 0: g2.drawImage(selectionCrosshair, 376, 432, 159, 55, null); break;
		case 1: g2.drawImage(selectionCrosshair, 588, 432, 159, 55, null); break;
		case 2: g2.drawImage(selectionCrosshair, 376, 496, 159, 55, null); break;
		case 3: g2.drawImage(selectionCrosshair, 588, 496, 159, 55, null); break;
		}
		
		if(gp.keyH.spacePressed) {
			gp.keyH.spacePressed = false;
			switch(userOption) {
			case 0: return 0;
			case 1: return 1;
			case 2: return 2;
			case 3: return 3;
			}
		}
		
		return -1;
	}
	
	public void drawMove(Move move, int x, int y) {
		
		
		g2.drawImage(fetchMoveBackground(move), x, y, 159, 55, null);
		
		if(move != null) {

			
			g2.setFont(gp.ui.b2w2Font.deriveFont(Font.PLAIN, 28));
			
			FontMetrics fm = g2.getFontMetrics();
			
			int width = fm.stringWidth(move.name.trim());
			
			drawOutlinedString(move.name.substring(0, 1).toUpperCase() + move.name.substring(1), x + 80 - width/2, y + 24, 1, 28, Color.white, Color.black);
			
			g2.drawImage(fetchMoveTypeImage(move), x + 28, y + 30, 48, 18, null);
			
			drawOutlinedString("PP" , x + 80, y + 46, 1, 24, Color.white, Color.black);
			drawOutlinedString(move.currentPP + "/" + move.pp, x + 104, y + 46, 1, 24, Color.white, Color.black);
		}
			
		
	}
	
	public BufferedImage fetchMoveTypeImage(Move move) {
		String type;
		if(move != null) {
			type = move.type;
		} else {
			return null;
		}
		
		try {
			return ImageIO.read(getClass().getResourceAsStream("/interfaces/" + type + ".png"));
		} catch (IOException e) {
			System.out.println("/interfaces/" + type + ".png doesn't exist.");
			e.printStackTrace();
		}
		
		return null; // should never happen.
	}
	
	public BufferedImage fetchMoveBackground(Move move) {
		
		String type;
		if(move != null) {
			type = move.type;
		} else {
			type = "noType";
		}
		
		try {
			return ImageIO.read(getClass().getResourceAsStream("/battle/" + type + "Button.png"));
		} catch (IOException e) {
			System.out.println("/battle/" + type + "Button.png doesn't exist.");
			e.printStackTrace();
		}
		
		return null; // should never happen.
	}
	
	public void drawBackground() {
		BufferedImage background = null;
		try {	
			switch(battlefield) {
			case "grass": background = ImageIO.read(getClass().getResourceAsStream("/battle/grassBG.png")); break;
			case "water": background = ImageIO.read(getClass().getResourceAsStream("/battle/waterBG.png")); break;
			case "ice": background = ImageIO.read(getClass().getResourceAsStream("/battle/iceBG.png"));  break;
			case "puddle": background = ImageIO.read(getClass().getResourceAsStream("/battle/puddleBG.png")); break;
			case "fall": background = ImageIO.read(getClass().getResourceAsStream("/battle/fallBG.png")); break;
			case "snow": background = ImageIO.read(getClass().getResourceAsStream("/battle/snowBG.png"));  break;			
			}
			
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		g2.drawImage(background, 0, 0, 768, 576, null);
	}
	
	public void drawOMon() {
		
		BufferedImage image = oMon.front[(gp.animationInt / 10) % oMon.front.length];
		
		int width = image.getWidth() * 4;
		int height = image.getHeight() * 4;
		
		g2.drawImage(image, 560 - width / 2, 280 - height, image.getWidth() * 4, image.getHeight() * 4, null); 
	}
	
	public void drawPMon() {
		
		BufferedImage image = pMon.back[(gp.animationInt / 10) % pMon.back.length];
		
		int width = image.getWidth() * 4;
		int height = image.getHeight() * 4;
		
		g2.drawImage(image, 160 - width / 2, 576 - height, width, height, null); 
	}
	
	public void drawOBar() {
		BufferedImage oBar = null, status = null;
		
		try { 
			oBar = ImageIO.read(getClass().getResourceAsStream("/battle/w2oBar.png"));
			if(oMon.primaryAilment != null) status = ImageIO.read(getClass().getResourceAsStream("/battle/" + oMon.primaryAilment + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		g2.drawImage(oBar, 20, 60, 330, 63, null);
		
		g2.setColor(Color.white);
		g2.setFont(gp.ui.b2w2Font.deriveFont(Font.BOLD, 38));
		
		drawOutlinedString(oMon.name, 60, 84);
		drawOutlinedString(oMon.lvl + "", 300, 84);
		
		g2.setFont(gp.ui.b2w2Font.deriveFont(Font.BOLD, 30));
		drawOutlinedString(oMon.hp + " / " + oMon.hlt, 200, 124);
		
		drawHPBar(false);
		
		g2.drawImage(status, 28, 88, 57, 18, null);
	}
	
	public void drawPBar() {
		BufferedImage pBar = null, status = null;
		try { 
			pBar = ImageIO.read(getClass().getResourceAsStream("/battle/w2pBar.png"));
			if(pMon.primaryAilment != null) status = ImageIO.read(getClass().getResourceAsStream("/battle/" + pMon.primaryAilment + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		g2.drawImage(pBar, 396, 340, 360, 80, null);
		
		g2.setColor(Color.white);
		g2.setFont(gp.ui.b2w2Font.deriveFont(Font.BOLD, 38));
		
		
		drawOutlinedString(pMon.name, 440, 362);
		drawOutlinedString(pMon.lvl + "", 660, 362);
		
		g2.setFont(gp.ui.b2w2Font.deriveFont(Font.BOLD, 30));
		drawOutlinedString(pMon.hp + " / " + pMon.hlt, 604, 402);
		
		drawXPBar();
		drawHPBar(true);
		
		g2.drawImage(status, 436, 366, 57, 18, null);
		
		
		/*int numBoosts = 0;
		
		if(pMon.atkM != 0) {	
			g2.setColor(Color.white);
			g2.fillRoundRect(452 + 88 * numBoosts, 340, 80, 20, 2, 2);
			g2.setColor(Color.red);
			g2.drawRoundRect(452 + 88 * numBoosts, 340, 80, 20, 2, 2);
			
			double num;
			if(pMon.atkM >= 0) num = (2 + pMon.atkM) / 2.0;
			else num = 2.0 / (2 - pMon.atkM);
			
			g2.drawString(num + "x Atk", 460 + 88 * numBoosts, 352);
			
			numBoosts ++;
		}
		
		if(pMon.spcAtkM != 0) {
			g2.setColor(Color.white);
			g2.fillRoundRect(452 + 88 * numBoosts, 340, 80, 20, 2, 2);
			g2.setColor(Color.red);
			g2.drawRoundRect(452 + 88 * numBoosts, 340, 80, 20, 2, 2);
			
			double num;
			if(pMon.spcAtkM >= 0) num = (2 + pMon.spcAtkM) / 2.0;
			else num = 2.0 / (2 - pMon.spcAtkM);
			
			g2.drawString(num + "x SpcAtk", 460 + 88 * numBoosts, 352);
			
			numBoosts ++;
		}
		
		if(pMon.defM != 0) {
			g2.setColor(Color.white);
			g2.fillRoundRect(452 + 88 * numBoosts, 340, 80, 20, 2, 2);
			g2.setColor(Color.red);
			g2.drawRoundRect(452 + 88 * numBoosts, 340, 80, 20, 2, 2);
			
			double num;
			if(pMon.defM >= 0) num = (2 + pMon.defM) / 2.0;
			else num = 2.0 / (2 - pMon.defM);
			
			g2.drawString(num + "x Def", 460 + 88 * numBoosts, 352);
			
			numBoosts ++;
		}
		
		if(pMon.spcDefM != 0) {
			g2.setColor(Color.white);
			g2.fillRoundRect(452 + 88 * numBoosts, 340, 80, 20, 2, 2);
			g2.setColor(Color.red);
			g2.drawRoundRect(452 + 88 * numBoosts, 340, 80, 20, 2, 2);
			
			double num;
			if(pMon.spcDefM >= 0) num = (2 + pMon.spcDefM) / 2.0;
			else num = 2.0 / (2 - pMon.spcDefM);
			
			g2.drawString(num + "x SpcDef", 460 + 88 * numBoosts, 352);
			
			numBoosts ++;
		}
		
		if(pMon.spdM != 0) {
			
			g2.setColor(Color.white);
			g2.fillRoundRect(452 + 88 * numBoosts, 340, 80, 20, 2, 2);
			g2.setColor(Color.red);
			g2.drawRoundRect(452 + 88 * numBoosts, 340, 80, 20, 2, 2);
			
			double num;
			if(pMon.spdM >= 0) num = (2 + pMon.spdM) / 2.0;
			else num = 2.0 / (2 - pMon.spdM);
			
			g2.drawString(num + "x Spd", 460 + 88 * numBoosts, 352);
			
			numBoosts ++;
		} Add this feature later*/
		
	}
	
	public boolean drawText(String text) {
		
		if(charIndex > text.length() || !currentText.substring(0, charIndex).equals(text.substring(0, charIndex))) {
			charIndex = 0;
			currentText = "";
		}
		
	    g2.setColor(new Color(0, 0, 0, 180));
	    g2.fillRect(0, 426, 768, 132);

	    if (charIndex < text.length()) {
	        if (gp.animationInt % 2 == 0) {
	            currentText += text.charAt(charIndex);
	            charIndex++;
	        }
	    }

	    FontMetrics fm = g2.getFontMetrics();
	    int maxWidth = 600; 
	    String[] words = currentText.split(" ");
	    String line = "";
	    int y = 472;
	    
	    for (String word : words) {
	        String testLine = line.isEmpty() ? word : line + " " + word;
	        if (fm.stringWidth(testLine) > maxWidth) {
	            drawOutlinedString(line, 32, y, 1, 40, new Color(255, 255, 255, 180), new Color(128, 128, 128, 180));
	            y += fm.getHeight() + 16;
	            line = word;
	        } else {
	            line = testLine;
	        }
	    }
	    if (!line.isEmpty()) {
	        drawOutlinedString(line, 32, y, 1, 40, new Color(255, 255, 255, 180), new Color(128, 128, 128, 180));
	    }

	    
	    if (gp.keyH.spacePressed) {
	        if(charIndex < text.length()) {
	            currentText = text;
	            charIndex = text.length();
	            gp.keyH.spacePressed = false;
	        }
	    }

	    if (charIndex >= text.length()) {
            return true;
	    }
	    
	    return false;
	}


	
	public void drawOutlinedString(String text, int x, int y) {
	    g2.setColor(Color.black);
	    for (int dx = -2; dx <= 2; dx++) {
	        for (int dy = -2; dy <= 2; dy++) {
	            if (dx != 0 || dy != 0) {
	                g2.drawString(text, x + dx, y + dy);
	            }
	        }
	    }
	    g2.setColor(Color.white);
	    g2.drawString(text, x, y);
	}
	
	public void drawOutlinedString(String text, int x, int y, int outlineSize, int fontSize, Color primaryColor, Color outlineColor) {
		g2.setFont(gp.ui.b2w2Font.deriveFont(Font.PLAIN, fontSize));
		g2.setColor(outlineColor);
	    for (int dx = 0; dx <= outlineSize; dx++) {
	        for (int dy = 0; dy <= outlineSize; dy++) {
	            if (dx != 0 || dy != 0) {
	                g2.drawString(text, x + dx, y + dy);
	            }
	        }
	    }
	    g2.setColor(primaryColor);
	    g2.drawString(text, x, y);
	}
	
	public void drawXPBar() {
		
		int currLevelXP = (int) (pMon.xp - Pokemon.calcXP(pMon.lvl, pMon.growthRate));
		int totLevelXP = (int) ((Pokemon.calcXP(pMon.lvl + 1, pMon.growthRate)) - (Pokemon.calcXP(pMon.lvl, pMon.growthRate)));
	
		double xpRatio = (1.0 * currLevelXP / totLevelXP);
		
		//normally 80 sections, I made 85 to fill whole bar
		//g2.fillRect(479, 410, (255), 7);
		
		for(int i = 0; i < 85; i++) {
			if(xpRatio * 85 * 3 > i) {
				if(i % 2 == 0) {
					g2.setColor(new Color(0, 186, 243));
				} else {
					g2.setColor(new Color(0, 121, 154));
				}
				g2.fillRect(479 + 3 * i, 410, 3, 7);
			} else {
				break;
			}
		}
		
	}
	
	public void drawHPBar(boolean isUser) {
		//used for batlte interface
		int x, y;
		double hpRatio;
		
		if(isUser) {
			x = 555;
			y = 369;
			hpRatio = (1.0 * pMon.hp / pMon.hlt);
		} else {
			x = 140;
			y = 90;
			hpRatio = (1.0 * oMon.hp / oMon.hlt);
		}
		
		if(hpRatio > .5) g2.setColor(new Color(0, 138, 40));
		else if (hpRatio > .2) g2.setColor(new Color(154, 97, 16));
		else g2.setColor(new Color(146, 32, 48));
		g2.fillRect(x, y, (int) (hpRatio * 153), 10);
		
		if(hpRatio > .5) g2.setColor(new Color(0, 251, 73));
		else if(hpRatio > .2) g2.setColor(new Color(243, 178, 0));
		else g2.setColor(new Color(251, 48, 65));
		g2.fillRect(x, y + 3, (int) (hpRatio * 153), 4);
		
		g2.setColor(new Color(154, 146, 154)); 
		g2.fillRect(x + (int) (hpRatio * 153), y, 153 - (int)(hpRatio * 153), 10);
		g2.setColor(new Color(243, 243, 243)); // middle
		g2.fillRect(x + (int) (hpRatio * 153), y + 3, 153 - (int)(hpRatio * 153), 4);
	}
	
	public void drawHPBar(int x, int y, Pokemon mon) {
		//used in party interface
		
		BufferedImage hp = null;
		try {
			hp = ImageIO.read(getClass().getResourceAsStream("/battle/partyHPBar.png"));
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		g2.drawImage(hp, x, y, 192, 18, null);
		
		double hpRatio = 1.0 * mon.hp / mon.hlt;
		
		if(hpRatio > .5) {
			g2.setColor(new Color(0, 251, 73));
		} else if(hpRatio > .2) {
			g2.setColor(new Color(243, 178, 0));
		} else {
			g2.setColor(new Color(251, 48, 65));
		}
		
		g2.fillRect(x + 45, y + 6, (int) (hpRatio * 144), 6);
	}
	
	public boolean changeHP(int newHP, Pokemon mon) {
	
		int currHP = mon.hp;
		
		int distance = currHP - newHP;
		int spd;
		
		if(Math.abs(distance) < 15) {
			spd = 4
					;
		} else if(Math.abs(distance) < 50) {
			spd = 2;
		} else {
			spd = 1;
		}
		
		if(gp.animationInt % spd == 0) {
			if(distance > 0) mon.hp --;
			else mon.hp ++;
		}
		
		if(gp.keyH.spacePressed) {
			mon.hp = newHP;
		}
		
		if(currHP == newHP || currHP == 0) return true;
		else return false;
	}
	
} 