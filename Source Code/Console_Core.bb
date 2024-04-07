


;[Block]
Global ConsoleOpen%, ConsoleInput$
Global ConsoleScroll#, ConsoleScrollDragging%
Global ConsoleMouseMem%
Global ConsoleReissue.ConsoleMsg = Null
Global ConsoleR%, ConsoleG%, ConsoleB%
;[End Block]

;[Block]
Type ConsoleMsg
	Field Txt$
	Field IsCommand%
	Field R%, G%, B%
End Type
;[End Block]

Function CreateConsoleMsg%(Txt$, R% = -1, G% = -1, B% = -1, IsCommand% = False)
	Local c.ConsoleMsg
	
	c.ConsoleMsg = New ConsoleMsg
	Insert c Before First ConsoleMsg
	
	c\Txt = Txt
	c\IsCommand = IsCommand
	
	c\R = R
	c\G = G
	c\B = B
	
	If c\R < 0 Then c\R = ConsoleR
	If c\G < 0 Then c\G = ConsoleG
	If c\B < 0 Then c\B = ConsoleB
End Function

Function CreateConsoleMultiMsg%(Txt$, R% = -1, G% = -1, B% = -1, IsCommand% = False)
	While Instr(Txt, "\n") <> 0 
		CreateConsoleMsg(Left(Txt, Instr(Txt, "\n") - 1), R, G, B, IsCommand)
		Txt = Right(Txt, Len(Txt) - Instr(Txt, "\n") - 1)
	Wend
	CreateConsoleMsg(Txt, R, G, B, IsCommand)
End Function

;[Block]
Type Cheats
	Field GodMode%
	Field NoBlink%
	Field NoTarget%
	Field NoClip%, NoClipSpeed#
	Field InfiniteStamina%
	Field SuperMan%, SuperManTimer#
	Field DebugHUD%
End Type
;[End Block]

Global chs.Cheats

Function ClearCheats%()
	chs\GodMode = False
	chs\NoBlink = False
	chs\NoTarget = False
	chs\NoClip = False
	chs\InfiniteStamina = False
	chs\SuperMan = False
	chs\SuperManTimer = 0.0
	chs\DebugHUD = 0
End Function

Function InitCheats%()
	chs\GodMode = True
	chs\NoBlink = True
	chs\NoTarget = True
	chs\NoClip = True
	chs\InfiniteStamina = True
	chs\SuperMan = False
	chs\SuperManTimer = 0.0
	chs\DebugHUD = Rand(4)
End Function

Function UpdateConsole%(Section% = 0)
	CatchErrors("UpdateConsole()")
	
	If (Not opt\CanOpenConsole)
		ConsoleOpen = False
		Return
	EndIf
	
	If ConsoleOpen
		Local ev.Events, e.Events, e2.Events, r.Rooms, it.Items, n.NPCs, snd.Sound, cm.ConsoleMsg, itt.ItemTemplates
		Local Tex%, Tex2%, InBar%, InBox%, MouseScroll#, Temp%, i%
		Local Args$, StrTemp$, StrTemp2$, StrTemp3$, StrTemp4$
		Local x%, y%, Width%, Height%
		Local CoordEx% = 15 * MenuScale
		
		ConsoleR = 255 : ConsoleG = 255 : ConsoleB = 255
		
		x = 0
		y = opt\GraphicHeight - 300 * MenuScale
		Width = opt\GraphicWidth
		Height = 270 * MenuScale
		
		Local ConsoleHeight% = 0
		Local ScrollBarHeight% = 0
		
		For cm.ConsoleMsg = Each ConsoleMsg
			ConsoleHeight = ConsoleHeight + CoordEx
		Next
		ScrollBarHeight = (Float(Height) / Float(ConsoleHeight)) * Height
		If ScrollBarHeight > Height Then ScrollBarHeight = Height
		If ConsoleHeight < Height Then ConsoleHeight = Height
		
		InBar = MouseOn(x + Width - (26 * MenuScale), y, 26 * MenuScale, Height)
		InBox = MouseOn(x + Width - (23 * MenuScale), y + Height - ScrollBarHeight + (ConsoleScroll * ScrollBarHeight / Height), 20 * MenuScale, ScrollBarHeight)
		
		If (Not mo\MouseDown1)
			ConsoleScrollDragging = False
		ElseIf ConsoleScrollDragging
			ConsoleScroll = ConsoleScroll + ((MousePosY - ConsoleMouseMem) * Height / ScrollBarHeight)
			ConsoleMouseMem = MousePosY
		EndIf
		
		If (Not ConsoleScrollDragging)
			If mo\MouseHit1
				If InBox
					ConsoleScrollDragging = True
					ConsoleMouseMem = MousePosY
				ElseIf InBar
					ConsoleScroll = ConsoleScroll + ((MousePosY - (y + Height)) * ConsoleHeight / Height + (Height / 2))
					ConsoleScroll = ConsoleScroll / 2
				EndIf
			EndIf
		EndIf
		
		MouseScroll = MouseZSpeed()
		If MouseScroll = 1
			ConsoleScroll = ConsoleScroll - CoordEx
		ElseIf MouseScroll= -1
			ConsoleScroll = ConsoleScroll + CoordEx
		EndIf
		
		Local ReissuePos%
		
		If KeyHit(200)
			ReissuePos = 0
			If ConsoleReissue = Null
				ConsoleReissue = First ConsoleMsg
				
				While ConsoleReissue <> Null
					If ConsoleReissue\IsCommand Then Exit
					ReissuePos = ReissuePos - CoordEx
					ConsoleReissue = After ConsoleReissue
				Wend
			Else
				cm.ConsoleMsg = First ConsoleMsg
				While cm <> Null
					If cm = ConsoleReissue Then Exit
					ReissuePos = ReissuePos - CoordEx
					cm = After cm
				Wend
				ConsoleReissue = After ConsoleReissue
				ReissuePos = ReissuePos - CoordEx
				
				While True
					If ConsoleReissue = Null
						ConsoleReissue = First ConsoleMsg
						ReissuePos = 0
					EndIf
					
					If ConsoleReissue\IsCommand Then Exit
					ReissuePos = ReissuePos - CoordEx
					ConsoleReissue = After ConsoleReissue
				Wend
			EndIf
			
			If ConsoleReissue <> Null
				ConsoleInput = ConsoleReissue\Txt
				ConsoleScroll = ReissuePos + (Height / 2)
			EndIf
		EndIf
		
		If KeyHit(208)
			ReissuePos = (-ConsoleHeight) + CoordEx
			If ConsoleReissue = Null
				ConsoleReissue = Last ConsoleMsg
				
				While ConsoleReissue <> Null
					If ConsoleReissue\IsCommand Then Exit
					ReissuePos = ReissuePos + CoordEx
					ConsoleReissue = Before ConsoleReissue
				Wend
			Else
				cm.ConsoleMsg = Last ConsoleMsg
				While cm <> Null
					If cm = ConsoleReissue Then Exit
					ReissuePos = ReissuePos + CoordEx
					cm = Before cm
				Wend
				ConsoleReissue = Before ConsoleReissue
				ReissuePos = ReissuePos + CoordEx
				
				While True
					If ConsoleReissue = Null
						ConsoleReissue = Last ConsoleMsg
						ReissuePos = (-ConsoleHeight) + CoordEx
					EndIf
					
					If ConsoleReissue\IsCommand Then Exit
					ReissuePos = ReissuePos + CoordEx
					ConsoleReissue = Before ConsoleReissue
				Wend
			EndIf
			
			If ConsoleReissue <> Null
				ConsoleInput = ConsoleReissue\Txt
				ConsoleScroll = ReissuePos + (Height / 2)
			EndIf
		EndIf
		
		If ConsoleScroll < (-ConsoleHeight) + Height Then ConsoleScroll = (-ConsoleHeight) + Height
		If ConsoleScroll > 0 Then ConsoleScroll = 0
		
		SelectedInputBox = 2
		
		Local OldConsoleInput$ = ConsoleInput
		
		ConsoleInput = UpdateMenuInputBox(x, y + Height, Width, 30 * MenuScale, ConsoleInput, Font_Console, 2)
		If OldConsoleInput <> ConsoleInput Then ConsoleReissue = Null
		ConsoleInput = Left(ConsoleInput, 100)
		
		If KeyHit(28) And ConsoleInput <> ""
			ConsoleReissue = Null
			ConsoleScroll = 0
			CreateConsoleMsg(ConsoleInput, 255, 255, 0, True)
			If Instr(ConsoleInput, " ") <> 0
				StrTemp = Lower(Left(ConsoleInput, Instr(ConsoleInput, " ") - 1))
			Else
				StrTemp = Lower(ConsoleInput)
			EndIf
			
			Select(Section)
				Case 0 ; ~ Story Mode (Main Game)
					Select(Lower(StrTemp))
							;[Block]
						Case "help"
							;[Block]
							If Instr(ConsoleInput, " ") <> 0
								StrTemp = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
							Else
								StrTemp = ""
							EndIf
							ConsoleR = 0 : ConsoleG = 255 : ConsoleB = 255
							
							Select(StrTemp)
								Case "1", ""
									;[Block]
									CreateConsoleMsg(GetLocalString("console", "help_1.1"))
									CreateConsoleMsg("******************************")
									CreateConsoleMsg("- ending")
									CreateConsoleMsg("- notarget")
									CreateConsoleMsg("- godmode")
									CreateConsoleMsg("- noclip")
									CreateConsoleMsg("- noclipspeed")
									CreateConsoleMsg("- infinitestamina")
									CreateConsoleMsg("- noblink")
									CreateConsoleMsg("- asd")
									CreateConsoleMsg("- revive")
									CreateConsoleMsg("- heal")
									CreateConsoleMsg("- wireframe")
									CreateConsoleMsg("- halloween")
									CreateConsoleMsg("- newyear")
									CreateConsoleMsg("- sanic")
									CreateConsoleMsg("- weed")
									CreateConsoleMsg("- money")
									CreateConsoleMsg("- debughud")
									CreateConsoleMsg("******************************")
									CreateConsoleMsg(GetLocalString("console", "help_1.2"))
									CreateConsoleMsg(GetLocalString("console", "help.command"))
									CreateConsoleMsg("******************************")
									;[End Block]
								Case "2"
									;[Block]
									CreateConsoleMsg(GetLocalString("console", "help_2.1"))
									CreateConsoleMsg("******************************")
									CreateConsoleMsg("- reset096")
									CreateConsoleMsg("- reset372")
									CreateConsoleMsg("- 106retreat")
									CreateConsoleMsg("- disable173")
									CreateConsoleMsg("- enable173")
									CreateConsoleMsg("- disable106")
									CreateConsoleMsg("- enable106")
									CreateConsoleMsg("- disable049")
									CreateConsoleMsg("- enable049")
									CreateConsoleMsg("- disable966")
									CreateConsoleMsg("- enable966")
									CreateConsoleMsg("- doorcontrol")
									CreateConsoleMsg("- unlockcheckpoints")
									CreateConsoleMsg("- unlockexits")
									CreateConsoleMsg("- disablenuke")
									CreateConsoleMsg("- resetfunds")
									CreateConsoleMsg("******************************")
									CreateConsoleMsg(GetLocalString("console", "help_2.2"))
									CreateConsoleMsg(GetLocalString("console", "help.command"))
									CreateConsoleMsg("******************************")
									;[End Block]
								Case "3"
									;[Block]
									CreateConsoleMsg(GetLocalString("console", "help_3.1"))
									CreateConsoleMsg("******************************")
									CreateConsoleMsg("- camerafog [near] [far]")
									CreateConsoleMsg("- spawn [npc type] [state]")
									CreateConsoleMsg("- injure [value]")
									CreateConsoleMsg("- infect [value]")
									CreateConsoleMsg("- crystal [value]")
									CreateConsoleMsg("- teleport [room name]")
									CreateConsoleMsg("- spawnitem [item name]")
									CreateConsoleMsg("- giveachievement [ID / All]")
									CreateConsoleMsg("- codes")
									CreateConsoleMsg("_ weaponcheats")
									CreateConsoleMsg("- loadzone")
									CreateConsoleMsg("******************************")
									CreateConsoleMsg(GetLocalString("console", "help.command"))
									CreateConsoleMsg("******************************")
									;[End Block]
								Case "camerafog"
									;[Block]
									CreateConsoleMsg(Format(GetLocalString("console", "help.title"), "camerafog"))
									CreateConsoleMsg("******************************")
									CreateConsoleMultiMsg(GetLocalString("console", "help.camerafog"))
									CreateConsoleMsg("******************************")
									;[End Block]
								Case "noclip", "fly"
									;[Block]
									CreateConsoleMsg(Format(GetLocalString("console", "help.title"), "noclip"))
									CreateConsoleMsg("******************************")
									CreateConsoleMultiMsg(GetLocalString("console", "help.noclip"))
									CreateConsoleMsg("******************************")
									;[End Block]
								Case "noblink", "nb"
									;[Block]
									CreateConsoleMsg(Format(GetLocalString("console", "help.title"), "noblink"))
									CreateConsoleMsg("******************************")
									CreateConsoleMultiMsg(GetLocalString("console", "help.noblink"))
									CreateConsoleMsg("******************************")
									;[End Block]
								Case "godmode", "god"
									;[Block]
									CreateConsoleMsg(Format(GetLocalString("console", "help.title"), "god"))
									CreateConsoleMsg("******************************")
									CreateConsoleMultiMsg(GetLocalString("console", "help.god"))
									CreateConsoleMsg("******************************")
									;[End Block]
								Case "infinitestamina", "is"
									;[Block]
									CreateConsoleMsg(Format(GetLocalString("console", "help.title"), "infinitestamina"))
									CreateConsoleMsg("******************************")
									CreateConsoleMultiMsg(GetLocalString("console", "help.is"))
									CreateConsoleMsg("******************************")
								Case "notarget", "nt"
									;[Block]
									CreateConsoleMsg(Format(GetLocalString("console", "help.title"), "notarget"))
									CreateConsoleMsg("******************************")
									CreateConsoleMultiMsg(GetLocalString("console", "help.nt"))
									CreateConsoleMsg("******************************")
									;[End Block]
								Case "wireframe", "wf"
									;[Block]
									CreateConsoleMsg(Format(GetLocalString("console", "help.title"), "wireframe"))
									CreateConsoleMsg("******************************")
									CreateConsoleMultiMsg(GetLocalString("console", "help.wf"))
									CreateConsoleMsg("******************************")
									;[End Block]
								Case "spawnitem", "si", "giveitem"
									;[Block]
									CreateConsoleMsg(Format(GetLocalString("console", "help.title"), "spawnitem"))
									CreateConsoleMsg("******************************")
									CreateConsoleMultiMsg(GetLocalString("console", "help.si"))
									CreateConsoleMsg("******************************")
									;[End Block]
								Case "spawn", "s"
									;[Block]
									CreateConsoleMsg(Format(GetLocalString("console", "help.title"), "spawn"))
									CreateConsoleMsg("******************************")
									CreateConsoleMultiMsg(GetLocalString("console", "help.s"))
									CreateConsoleMsg("******************************")
									;[End Block]
								Case "reset372" 
									;[Block]
									CreateConsoleMsg(Format(GetLocalString("console", "help.title"), "reset372"))
									CreateConsoleMsg("******************************")
									CreateConsoleMultiMsg(GetLocalString("console", "help.r372"))
									CreateConsoleMsg("******************************")
									;[End Block]
								Case "106retreat" 
									;[Block]
									CreateConsoleMsg(Format(GetLocalString("console", "help.title"), "106retreat"))
									CreateConsoleMsg("******************************")
									CreateConsoleMultiMsg(GetLocalString("console", "help.106r"))
									CreateConsoleMsg("******************************")
									;[End Block]
								Case "disable106"
									;[Block]
									CreateConsoleMsg(Format(GetLocalString("console", "help.title"), "disable106"))
									CreateConsoleMsg("******************************")
									CreateConsoleMultiMsg(GetLocalString("console", "help.d106"))
									CreateConsoleMsg("******************************")
									;[End Block]
								Case "enable106"
									;[Block]
									CreateConsoleMsg(Format(GetLocalString("console", "help.title"), "enable106"))
									CreateConsoleMsg("******************************")
									CreateConsoleMultiMsg(GetLocalString("console", "help.e106"))
									CreateConsoleMsg("******************************")
									;[End Block]
								Case "disable173"
									;[Block]
									CreateConsoleMsg(Format(GetLocalString("console", "help.title"), "disable173"))
									CreateConsoleMsg("******************************")
									CreateConsoleMultiMsg(GetLocalString("console", "help.d173"))
									CreateConsoleMsg("******************************")
									;[End Block]
								Case "enable173"
									;[Block]
									CreateConsoleMsg(Format(GetLocalString("console", "help.title"), "enable173"))
									CreateConsoleMsg("******************************")
									CreateConsoleMultiMsg(GetLocalString("console", "help.e173"))
									CreateConsoleMsg("******************************")
									;[End Block]
								Case "reset096" 
									;[Block]
									CreateConsoleMsg(Format(GetLocalString("console", "help.title"), "reset096"))
									CreateConsoleMsg("******************************")
									CreateConsoleMultiMsg(GetLocalString("console", "help.r096"))
									CreateConsoleMsg("******************************")
									;[End Block]
								Case "doorcontrol" 
									;[Block]
									CreateConsoleMsg(Format(GetLocalString("console", "help.title"), "doorcontrol"))
									CreateConsoleMsg("******************************")
									CreateConsoleMultiMsg(GetLocalString("console", "help.dc"))
									CreateConsoleMsg("******************************")
									;[End Block]
								Case "asd"
									;[Block]
									CreateConsoleMsg(Format(GetLocalString("console", "help.title"), "asd"))
									CreateConsoleMsg("******************************")
									CreateConsoleMultiMsg(GetLocalString("console", "help.asd"))
									CreateConsoleMsg("******************************")
									;[End Block]
								Case "unlockcheckpoints" 
									;[Block]
									CreateConsoleMsg(Format(GetLocalString("console", "help.title"), "unlockcheckpoints"))
									CreateConsoleMsg("******************************")
									CreateConsoleMultiMsg(GetLocalString("console", "help.uc"))
									CreateConsoleMsg("******************************")
									;[End Block]
								Case "disable049"
									;[Block]
									CreateConsoleMsg(Format(GetLocalString("console", "help.title"), "disable049"))
									CreateConsoleMsg("******************************")
									CreateConsoleMultiMsg(GetLocalString("console", "help.d049"))
									CreateConsoleMsg("******************************")
									;[End Block]
								Case "enable049"
									;[Block]
									CreateConsoleMsg(Format(GetLocalString("console", "help.title"), "enable049"))
									CreateConsoleMsg("******************************")
									CreateConsoleMultiMsg(GetLocalString("console", "help.e049"))
									CreateConsoleMsg("******************************")
									;[End Block]
								Case "disable966"
									;[Block]
									CreateConsoleMsg(Format(GetLocalString("console", "help.title"), "disable966"))
									CreateConsoleMsg("******************************")
									CreateConsoleMultiMsg(GetLocalString("console", "help.d966"))
									CreateConsoleMsg("******************************")
									;[End Block]
								Case "enable966"
									;[Block]
									CreateConsoleMsg(Format(GetLocalString("console", "help.title"), "enable966"))
									CreateConsoleMsg("******************************")
									CreateConsoleMultiMsg(GetLocalString("console", "help.e966"))
									CreateConsoleMsg("******************************")
									;[End Block]
								Case "revive", "undead", "resurrect"
									;[Block]
									CreateConsoleMsg(Format(GetLocalString("console", "help.title"), "revive"))
									CreateConsoleMsg("******************************")
									CreateConsoleMultiMsg(GetLocalString("console", "help.revive"))
									CreateConsoleMsg("******************************")
									;[End Block]
								Case "teleport"
									;[Block]
									CreateConsoleMsg(Format(GetLocalString("console", "help.title"), "teleport"))
									CreateConsoleMsg("******************************")
									CreateConsoleMultiMsg(GetLocalString("console", "help.teleport"))
									CreateConsoleMsg("******************************")
									;[End Block]
								Case "stopsound", "stfu"
									;[Block]
									CreateConsoleMsg(Format(GetLocalString("console", "help.title"), "stopsound"))
									CreateConsoleMsg("******************************")
									CreateConsoleMultiMsg(GetLocalString("console", "help.stfu"))
									CreateConsoleMsg("******************************")
									;[End Block]
								Case "weed", "scp-420-j", "420j", "scp420-j", "scp-420j", "420"
									;[Block]
									CreateConsoleMsg(Format(GetLocalString("console", "help.title"), "weed"))
									CreateConsoleMsg("******************************")
									CreateConsoleMultiMsg(GetLocalString("console", "help.weed"))
									CreateConsoleMsg("******************************")
									;[End Block]
								Case "infect"
									;[Block]
									CreateConsoleMsg(Format(GetLocalString("console", "help.title"), "infect"))
									CreateConsoleMsg("******************************")
									CreateConsoleMultiMsg(GetLocalString("console", "help.infect"))
									CreateConsoleMsg("******************************")
									;[End Block]
								Case "crystal" 
									;[Block]
									CreateConsoleMsg(Format(GetLocalString("console", "help.title"), "crystal"))
									CreateConsoleMsg("******************************")
									CreateConsoleMultiMsg(GetLocalString("console", "help.crystal"))
									CreateConsoleMsg("******************************")
									;[End Block]
								Case "resetfunds"
									;[Block]
									CreateConsoleMsg(Format(GetLocalString("console", "help.title"), "resetfunds"))
									CreateConsoleMsg("******************************")
									CreateConsoleMultiMsg(GetLocalString("console", "help.rf"))
									CreateConsoleMsg("******************************")
									;[End Block]
								Case "giveachievement"
									;[Block]
									CreateConsoleMsg(Format(GetLocalString("console", "help.title"), "giveachievement"))
									CreateConsoleMsg("******************************")
									CreateConsoleMultiMsg(GetLocalString("console", "help.ac"))
									CreateConsoleMsg("******************************")
									;[End Block]
								Case "codes" 
									;[Block]
									CreateConsoleMsg(Format(GetLocalString("console", "help.title"), "codes"))
									CreateConsoleMsg("******************************")
									CreateConsoleMultiMsg(GetLocalString("console", "help.codes"))
									CreateConsoleMsg("******************************")
									;[End Block]
								Case "weaponcheats" 
									;[Block]
									CreateConsoleMsg(Format(GetLocalString("console", "help.title"), "weaponcheats"))
									CreateConsoleMsg("******************************")
									CreateConsoleMultiMsg(GetLocalString("console", "help.weaponcheats"))
									CreateConsoleMsg("******************************")
									;[End Block]
								Case "loadzone" 
									;[Block]
									CreateConsoleMsg(Format(GetLocalString("console", "help.title"), "loadzone"))
									CreateConsoleMsg("******************************")
									CreateConsoleMultiMsg(GetLocalString("console", "help.loadzone"))
									CreateConsoleMsg("******************************")
									;[End Block]
								Default
									;[Block]
									CreateConsoleMsg(GetLocalString("console", "help.no"), 255, 150, 0)
									;[End Block]
							End Select
							;[End Block]
						Case "ending"
							;[Block]
							StrTemp = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
							
							Select(StrTemp)
								Case "A"
									;[Block]
									me\SelectedEnding = Rand(Ending_A1, Ending_A2)
									;[End Block]
								Case "B"
									;[Block]
									me\SelectedEnding = Rand(Ending_B1, Ending_B2)
									;[End Block]
								Default
									;[Block]
									me\SelectedEnding = Rand(Ending_A1, Ending_B2)
									;[End Block]
							End Select
							
							me\Health = 0
							;[End Block]
						Case "noclipspeed"
							;[Block]
							StrTemp = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
							
							chs\NoClipSpeed = Float(StrTemp)
							
							CreateConsoleMsg(Format(GetLocalString("console", "fly.speed"), StrTemp))
							;[End Block]
						Case "injure", "damage"
							;[Block]
							StrTemp = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
							
							DamagePlayer(Int(StrTemp))
							
							CreateConsoleMsg(Format(GetLocalString("console", "inj"), StrTemp))
							;[End Block]
						Case "cls", "clear"
							;[Block]
							ClearConsole()
							;[End Block]
						Case "infect"
							;[Block]
							StrTemp = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
							
							I_008\Timer = Float(StrTemp)
							
							CreateConsoleMsg(Format(GetLocalString("console", "008"), StrTemp))
							;[End Block]
						Case "crystal"
							;[Block]
							StrTemp = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
							
							I_409\Timer = Float(StrTemp)
							
							CreateConsoleMsg(Format(GetLocalString("console", "409"), StrTemp))
							;[End Block]
						Case "heal"
							;[Block]
							ResetNegativeStats()
							CreateConsoleMsg(GetLocalString("console", "heal"))
							;[End Block]
						Case "teleport", "tp"
							;[Block]
							StrTemp = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
							Temp = FindRoomID(StrTemp)
							
							For r.Rooms = Each Rooms
								If r\RoomTemplate\RoomID = Temp
									If r\RoomCenter <> 0
										TeleportEntity(me\Collider, EntityX(r\RoomCenter, True), EntityY(r\OBJ) + 0.5, EntityZ(r\RoomCenter, True), 0.3, True)
									Else
										TeleportEntity(me\Collider, EntityX(r\OBJ), EntityY(r\OBJ) + 0.5, EntityZ(r\OBJ))
									EndIf
									TeleportToRoom(r)
									CreateConsoleMsg(Format(GetLocalString("console", "tp.success"), StrTemp))
									Exit
								EndIf
							Next
							
							If PlayerRoom\RoomTemplate\RoomID <> Temp Then CreateConsoleMsg(GetLocalString("console", "tp.failed"), 255, 0, 0)
							;[End Block]
						Case "spawnitem", "si", "giveitem", "gi"
							;[Block]
							StrTemp = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
							Temp = False 
							For itt.ItemTemplates = Each ItemTemplates
								If Lower(itt\Name) = StrTemp
									Temp = True
									CreateConsoleMsg(Format(GetLocalString("console", "si.success"), itt\DisplayName))
									it.Items = CreateItem(itt\Name, itt\TempName, EntityX(me\Collider), EntityY(Camera, True), EntityZ(me\Collider))
									EntityType(it\Collider, HIT_ITEM)
									Exit
								ElseIf Lower(itt\DisplayName) = StrTemp
									Temp = True
									CreateConsoleMsg(Format(GetLocalString("console", "si.success"), itt\DisplayName))
									it.Items = CreateItem(itt\Name, itt\TempName, EntityX(me\Collider), EntityY(Camera, True), EntityZ(me\Collider))
									EntityType(it\Collider, HIT_ITEM)
									Exit
								ElseIf Lower(itt\TempName) = StrTemp
									Temp = True
									CreateConsoleMsg(Format(GetLocalString("console", "si.success"), itt\DisplayName))
									it.Items = CreateItem(itt\Name, itt\TempName, EntityX(me\Collider), EntityY(Camera, True), EntityZ(me\Collider))
									EntityType(it\Collider, HIT_ITEM)
									Exit
								EndIf
							Next
							
							If (Not Temp) Then CreateConsoleMsg(GetLocalString("console", "si.failed"), 255, 0, 0)
							;[End Block]
						Case "wireframe", "wf"
							;[Block]
							StrTemp = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
							
							Select(StrTemp)
								Case "on", "1", "true"
									;[Block]
									WireFrameState = True
									;[End Block]
								Case "off", "0", "false"
									;[Block]
									WireFrameState = False
									;[End Block]
								Default
									;[Block]
									WireFrameState = (Not WireFrameState)
									;[End Block]
							End Select
							
							If WireFrameState
								CreateConsoleMsg(GetLocalString("console", "wf.on"))
							Else
								CreateConsoleMsg(GetLocalString("console", "wf.off"))
							EndIf
							
							WireFrame(WireFrameState)
							;[End Block]
						Case "reset096", "r096"
							;[Block]
							For n.NPCs = Each NPCs
								If n\NPCType = NPCType096
									n\State = 0.0
									StopStream_Strict(n\SoundCHN) : n\SoundCHN = 0 : n\SoundCHN_IsStream = False
									If n\SoundCHN2 <> 0 Then StopStream_Strict(n\SoundCHN2) : n\SoundCHN2 = 0 : n\SoundCHN2_IsStream = False
									Exit
								EndIf
							Next
							CreateConsoleMsg(GetLocalString("console", "r096"))
							;[End Block]
						Case "reset372", "r372"
							;[Block]
							For n.NPCs = Each NPCs
								If n\NPCType = NPCType372
									RemoveNPC(n)
									CreateEvent(e_cont1_372, r_cont1_372, 0, 0.0)
									Exit
								EndIf
							Next
							CreateConsoleMsg(GetLocalString("console", "r372"))
							;[End Block]
						Case "disable173", "dis173"
							;[Block]
							n_I\Curr173\Idle = 3 ; ~ This phenominal comment is brought to you by PolyFox. His absolute wisdom in this fatigue of knowledge brought about a new era of SCP-173 state checks.
							HideEntity(n_I\Curr173\OBJ)
							HideEntity(n_I\Curr173\OBJ2)
							HideEntity(n_I\Curr173\Collider)
							CreateConsoleMsg(GetLocalString("console", "dis173"))
							;[End Block]
						Case "enable173", "en173"
							;[Block]
							n_I\Curr173\Idle = 0
							ShowEntity(n_I\Curr173\OBJ)
							ShowEntity(n_I\Curr173\OBJ2)
							ShowEntity(n_I\Curr173\Collider)
							CreateConsoleMsg(GetLocalString("console", "en173"))
							;[End Block]
						Case "disable106", "dis106"
							;[Block]
							n_I\Curr106\Idle = 1
							n_I\Curr106\State = 100000.0
							n_I\Curr106\Contained = True
							HideEntity(n_I\Curr106\Collider)
							HideEntity(n_I\Curr106\OBJ)
							HideEntity(n_I\Curr106\OBJ2)
							CreateConsoleMsg(GetLocalString("console", "dis106"))
							;[End Block]
						Case "enable106", "en106"
							;[Block]
							n_I\Curr106\Idle = 0
							n_I\Curr106\Contained = False
							ShowEntity(n_I\Curr106\Collider)
							ShowEntity(n_I\Curr106\OBJ)
							CreateConsoleMsg(GetLocalString("console", "en106"))
							;[End Block]
						Case "disable966", "dis966"
							;[Block]
							For n.NPCs = Each NPCs
								If n\NPCType = NPCType966
									n\State = -1.0
									HideEntity(n\Collider)
									HideEntity(n\OBJ)
								EndIf
							Next
							CreateConsoleMsg(GetLocalString("console", "dis966"))
							;[End Block]
						Case "enable966", "en966"
							;[Block]
							For n.NPCs = Each NPCs
								If n\NPCType = NPCType966
									n\State = 0.0
									ShowEntity(n\Collider)
									If wi\NightVision > 0 Then ShowEntity(n\OBJ)
								EndIf
							Next
							CreateConsoleMsg(GetLocalString("console", "en966"))
							;[End Block]
						Case "disable049", "dis049"
							;[Block]
							If n_I\Curr049 <> Null
								n_I\Curr049\Idle = 1
								HideEntity(n_I\Curr049\Collider)
								HideEntity(n_I\Curr049\OBJ)
							EndIf
							CreateConsoleMsg(GetLocalString("console", "dis049"))
							;[End Block]
						Case "enable049", "en049"
							;[Block]
							If n_I\Curr049 <> Null
								n_I\Curr049\Idle = 0
								ShowEntity(n_I\Curr049\Collider)
								ShowEntity(n_I\Curr049\OBJ)
							EndIf
							CreateConsoleMsg(GetLocalString("console", "en049"))
							;[End Block]
						Case "106retreat", "106r"
							;[Block]
							If n_I\Curr106\State <= 0.0
								n_I\Curr106\State = Rnd(22000.0, 27000.0)
								PositionEntity(n_I\Curr106\Collider, 0.0, 500.0, 0.0)
								ResetEntity(n_I\Curr106\Collider)
								CreateConsoleMsg(GetLocalString("console", "106r"))
							Else
								CreateConsoleMsg(GetLocalString("console", "106r.failed"), 255, 150, 0)
							EndIf
							;[End Block]
						Case "halloween"
							;[Block]
							n_I\IsHalloween = (Not n_I\IsHalloween)
							If n_I\IsHalloween
								Tex = LoadTexture_Strict("GFX\NPCs\scp_173_H.png", 1)
								If opt\Atmosphere Then TextureBlend(Tex, 5)
								EntityTexture(n_I\Curr173\OBJ, Tex)
								EntityTexture(n_I\Curr173\OBJ2, Tex)
								DeleteSingleTextureEntryFromCache(Tex)
								CreateConsoleMsg(GetLocalString("console", "halloween.on"))
							Else
								If n_I\IsNewYear Then n_I\IsNewYear = (Not n_I\IsNewYear)
								If n_I\IsAprilFools Then n_I\IsAprilFools = (Not n_I\IsAprilFools)
								Tex2 = LoadTexture_Strict("GFX\NPCs\scp_173.png", 1)
								If opt\Atmosphere Then TextureBlend(Tex2, 5)
								EntityTexture(n_I\Curr173\OBJ, Tex2)
								EntityTexture(n_I\Curr173\OBJ2, Tex2)
								DeleteSingleTextureEntryFromCache(Tex2)
								CreateConsoleMsg(GetLocalString("console", "halloween.off"))
							EndIf
							;[End Block]
						Case "newyear" 
							;[Block]
							n_I\IsNewYear = (Not n_I\IsNewYear)
							If n_I\IsNewYear
								Tex = LoadTexture_Strict("GFX\NPCs\scp_173_NY.png", 1)
								If opt\Atmosphere Then TextureBlend(Tex, 5)
								EntityTexture(n_I\Curr173\OBJ, Tex)
								EntityTexture(n_I\Curr173\OBJ2, Tex)
								DeleteSingleTextureEntryFromCache(Tex)
								CreateConsoleMsg(GetLocalString("console", "newyear.on"))
							Else
								If n_I\IsHalloween Then n_I\IsHalloween = (Not n_I\IsHalloween)
								If n_I\IsAprilFools Then n_I\IsAprilFools = (Not n_I\IsAprilFools)
								Tex2 = LoadTexture_Strict("GFX\NPCs\scp_173.png", 1)
								If opt\Atmosphere Then TextureBlend(Tex2, 5)
								EntityTexture(n_I\Curr173\OBJ, Tex2)
								EntityTexture(n_I\Curr173\OBJ2, Tex2)
								DeleteSingleTextureEntryFromCache(Tex2)
								CreateConsoleMsg(GetLocalString("console", "newyear.off"))
							EndIf
							;[End Block]
						Case "joke" 
							;[Block]
							n_I\IsAprilFools = (Not n_I\IsAprilFools)
							If n_I\IsAprilFools
								Tex = LoadTexture_Strict("GFX\NPCs\scp_173_J.png", 1)
								If opt\Atmosphere Then TextureBlend(Tex, 5)
								EntityTexture(n_I\Curr173\OBJ, Tex)
								EntityTexture(n_I\Curr173\OBJ2, Tex)
								DeleteSingleTextureEntryFromCache(Tex)
								CreateConsoleMsg(GetLocalString("console", "aprilfools.on"))
							Else
								If n_I\IsHalloween Then n_I\IsHalloween = (Not n_I\IsHalloween)
								If n_I\IsNewYear Then n_I\IsNewYear = (Not n_I\IsNewYear)
								Tex2 = LoadTexture_Strict("GFX\NPCs\scp_173.png", 1)
								If opt\Atmosphere Then TextureBlend(Tex2, 5)
								EntityTexture(n_I\Curr173\OBJ, Tex2)
								EntityTexture(n_I\Curr173\OBJ2, Tex2)
								DeleteSingleTextureEntryFromCache(Tex2)
								CreateConsoleMsg(GetLocalString("console", "aprilfools.off"))
							EndIf
							;[End Block]
						Case "sanic"
							;[Block]
							chs\SuperMan = (Not chs\SuperMan)
							If chs\SuperMan
								CreateConsoleMsg(GetLocalString("console", "sanic.on"))
							Else
								CreateConsoleMsg(GetLocalString("console", "sanic.off"))
							EndIf
							;[End Block]
						Case "scp-420-j", "420", "weed", "scp420-j", "scp-420j", "420j"
							;[Block]
							For i = 1 To 20
								If Rand(2) = 1
									StrTemp = "Some SCP-420-J"
									StrTemp2 = "scp420j"
								Else
									StrTemp = "Joint"
									StrTemp2 = "joint"
								EndIf
								it.Items = CreateItem(StrTemp, StrTemp2, EntityX(me\Collider, True) + Cos((360.0 / 20.0) * i) * Rnd(0.3, 0.5), EntityY(Camera, True), EntityZ(me\Collider, True) + Sin((360.0 / 20.0) * i) * Rnd(0.3, 0.5))
								EntityType(it\Collider, HIT_ITEM)
							Next
							PlaySound_Strict(LoadTempSound("SFX\Music\SCPs\SCP_420_J.ogg"))
							;[End Block]
						Case "godmode", "god"
							;[Block]
							StrTemp = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
							
							Select(StrTemp)
								Case "on", "1", "true"
									;[Block]
									chs\GodMode = True
									;[End Block]
								Case "off", "0", "false"
									;[Block]
									chs\GodMode = False
									;[End Block]
								Default
									;[Block]
									chs\GodMode = (Not chs\GodMode)
									;[End Block]
							End Select
							If chs\GodMode
								CreateConsoleMsg(GetLocalString("console", "god.on"))
							Else
								CreateConsoleMsg(GetLocalString("console", "god.off"))
							EndIf
							;[End Block]
						Case "revive", "undead", "resurrect"
							;[Block]
							ResetNegativeStats(True)
							;[End Block]
						Case "noclip", "fly"
							;[Block]
							StrTemp = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
							
							Select(StrTemp)
								Case "on", "1", "true"
									;[Block]
									chs\NoClip = True
									me\Playable = True
									;[End Block]
								Case "off", "0", "false"
									;[Block]
									chs\NoClip = False
									RotateEntity(me\Collider, 0.0, EntityYaw(me\Collider), 0.0)
									;[End Block]
								Default
									;[Block]
									chs\NoClip = (Not chs\NoClip)
									If (Not chs\NoClip)
										RotateEntity(me\Collider, 0.0, EntityYaw(me\Collider), 0.0)
									Else
										me\Playable = True
									EndIf
									;[End Block]
							End Select
							
							If chs\NoClip
								CreateConsoleMsg(GetLocalString("console", "fly.on"))
							Else
								CreateConsoleMsg(GetLocalString("console", "fly.off"))
							EndIf
							
							me\DropSpeed = 0.0
							;[End Block]
						Case "noblink", "nb"
							;[Block]
							StrTemp = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
							
							Select(StrTemp)
								Case "on", "1", "true"
									;[Block]
									chs\NoBlink = True
									;[End Block]
								Case "off", "0", "false"
									;[Block]
									chs\NoBlink = False
									;[End Block]
								Default
									;[Block]
									chs\NoBlink = (Not chs\NoBlink)
									;[End Block]
							End Select
							If chs\NoBlink
								CreateConsoleMsg(GetLocalString("console", "nb.on"))
							Else
								CreateConsoleMsg(GetLocalString("console", "nb.off"))
							EndIf
							;[End Block]
						Case "debughud", "dbh"
							;[Block]
							If Instr(ConsoleInput, " ") <> 0
								StrTemp = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
							Else
								StrTemp = ""
							EndIf
							
							Select(StrTemp)
								Case "game", "1"
									;[Block]
									chs\DebugHUD = 1
									;[End Block]
								Case "player", "me", "2"
									;[Block]
									chs\DebugHUD = 2
									;[End Block]
								Case "scps", "scp", "3"
									;[Block]
									chs\DebugHUD = 3
									;[End Block]
								Case "weapons", "wpn", "4"
									;[Block]
									chs\DebugHUD = 4
									;[End Block]
								Case "off", "false", "0"
									;[Block]
									chs\DebugHUD = 0
									;[End Block]
								Default
									;[Block]
									CreateConsoleMsg(GetLocalString("console", "debug.cate"), 255, 150, 0)
									;[End Block]
							End Select
							;[End Block]
						Case "stopsound", "stfu"
							;[Block]
							KillSounds()
							
							For e.Events = Each Events
								If e\EventID = e_cont1_173
									For i = 0 To 2
										If e\room\NPC[i] <> Null Then RemoveNPC(e\room\NPC[i])
										If i < 2 Then FreeEntity(e\room\Objects[i]) : e\room\Objects[i] = 0
									Next
									If n_I\Curr173\Idle = 1 Then n_I\Curr173\Idle = 0
									PositionEntity(n_I\Curr173\Collider, 0.0, 0.0, 0.0)
									ResetEntity(n_I\Curr173\Collider)
									RemoveEvent(e)
									Exit
								EndIf
							Next
							CreateConsoleMsg(GetLocalString("console", "stfu"))
							;[End Block]
						Case "camerafog", "cf"
							;[Block]
							StrTemp = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
							
							If opt\DebugMode = 1
								opt\CameraFogFar = StrTemp
							Else
								opt\CameraFogFar = Clamp(StrTemp, 6.0, 17.0)
							EndIf
							CreateConsoleMsg(Format(GetLocalString("console", "fog"), opt\CameraFogFar, "{0}"))
							;[End Block]
						Case "spawn", "s"
							;[Block]
							Args = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
							StrTemp = Piece(Args, 1)
							StrTemp2 = Piece(Args, 2)
							
						; ~ Hacky fix for when the user doesn't input a second parameter.
							If StrTemp <> StrTemp2
								ConsoleSpawnNPC(StrTemp, StrTemp2)
							Else
								ConsoleSpawnNPC(StrTemp)
							EndIf
							;[End Block]
						Case "infinitestamina", "infstam", "is"
							;[Block]
							StrTemp = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
							
							Select(StrTemp)
								Case "on", "1", "true"
									;[Block]
									chs\InfiniteStamina = True
									;[End Block]
								Case "off", "0", "false"
									;[Block]
									chs\InfiniteStamina = False
									;[End Block]
								Default
									;[Block]
									chs\InfiniteStamina = (Not chs\InfiniteStamina)
									;[End Block]
							End Select
							
							If chs\InfiniteStamina
								CreateConsoleMsg(GetLocalString("console", "is.on"))
							Else
								CreateConsoleMsg(GetLocalString("console", "is.off"))
							EndIf
							;[End Block]
						Case "money", "rich"
							;[Block]
							For i = 1 To 20
								If Rand(2) = 1
									StrTemp = "Quarter"
									StrTemp2 = "25ct"
								Else
									StrTemp = "Coin"
									StrTemp2 = "coin"
								EndIf
								it.Items = CreateItem(StrTemp, StrTemp2, EntityX(me\Collider, True) + Cos((360.0 / 20.0) * i) * Rnd(0.3, 0.5), EntityY(Camera, True), EntityZ(me\Collider, True) + Sin((360.0 / 20.0) * i) * Rnd(0.3, 0.5))
								EntityType(it\Collider, HIT_ITEM)
							Next
							;[End Block]
						Case "doorcontrol"
							;[Block]
							StrTemp = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
							
							Select(StrTemp)
								Case "on", "1", "true"
									;[Block]
									RemoteDoorOn = True
									;[End Block]
								Case "off", "0", "false"
									;[Block]
									RemoteDoorOn = False
									;[End Block]
								Default
									;[Block]
									RemoteDoorOn = (Not RemoteDoorOn)
									;[End Block]
							End Select
							
							If RemoteDoorOn
								CreateConsoleMsg(GetLocalString("console", "door.on"))
							Else
								CreateConsoleMsg(GetLocalString("console", "door.off"))
							EndIf
							
							For e2.Events = Each Events
								If e2\EventID = e_room2c_ec
									UpdateLever(e2\room\RoomLevers[2]\OBJ)
									RotateEntity(e2\room\RoomLevers[2]\OBJ, -80.0 + (160.0 * RemoteDoorOn), EntityYaw(e2\room\RoomLevers[2]\OBJ), 0.0)
									Exit
								EndIf
							Next
							;[End Block]
						Case "unlockcheckpoints"
							;[Block]
							For e2.Events = Each Events
								If e2\EventID = e_room2_sl
									e2\EventState3 = 0.0
									UpdateLever(e2\room\RoomLevers[0]\OBJ)
									RotateEntity(e2\room\RoomLevers[0]\OBJ, -80.0, EntityYaw(e2\room\RoomLevers[0]\OBJ), 0.0)
								ElseIf e2\EventID = e_cont2_008
									e2\EventState = 2.0
									UpdateLever(e2\room\Objects[1])
									RotateEntity(e2\room\Objects[1], -80.0, EntityYaw(e2\room\Objects[1]), 30.0)
								EndIf
							Next
							
							CreateConsoleMsg(GetLocalString("console", "uc"))
							;[End Block]
						Case "disablenuke"
							;[Block]
							For e2.Events = Each Events
								If e2\EventID = e_room2_nuke
									e2\EventState = 0.0
									UpdateLever(e2\room\RoomLevers[0]\OBJ)
									UpdateLever(e2\room\RoomLevers[1]\OBJ)
									RotateEntity(e2\room\RoomLevers[0]\OBJ, -80.0, EntityYaw(e2\room\RoomLevers[0]\OBJ), 0.0)
									RotateEntity(e2\room\RoomLevers[1]\OBJ, -80.0, EntityYaw(e2\room\RoomLevers[1]\OBJ), 0.0)
									Exit
								EndIf
							Next
							;[End Block]
						Case "unlockexits"
							;[Block]
							StrTemp = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
							
							Select(StrTemp)
								Case "a"
									;[Block]
									For e.Events = Each Events
										If e\EventID = e_gate_a_entrance
											e\EventState3 = 1.0
											e\room\RoomDoors[1]\Open = True
											Exit
										EndIf
									Next
									CreateConsoleMsg(GetLocalString("console", "ue.a"))
									;[End Block]
								Case "b"
									;[Block]
									For e.Events = Each Events
										If e\EventID = e_gate_b_entrance
											e\EventState3 = 1.0
											e\room\RoomDoors[1]\Open = True
											Exit
										EndIf
									Next
									CreateConsoleMsg(GetLocalString("console", "ue.b"))
									;[End Block]
								Default
									;[Block]
									For e.Events = Each Events
										If e\EventID = e_gate_b_entrance Lor e\EventID = e_gate_a_entrance
											e\EventState3 = 1.0
											e\room\RoomDoors[1]\Open = True
										EndIf
									Next
									CreateConsoleMsg(GetLocalString("console", "ue"))
									;[End Block]
							End Select
							RemoteDoorOn = True
							;[End Block]
						Case "kill", "suicide"
							;[Block]
							me\Health = 0
							Select(Rand(4))
								Case 1
									;[Block]
									msg\DeathMsg = GetLocalString("death", "kill_1")
									;[End Block]
								Case 2
									;[Block]
									msg\DeathMsg = Format(GetLocalString("death", "kill_2"), Occupation)
									;[End Block]
								Case 3
									;[Block]
									msg\DeathMsg = GetLocalString("death", "kill_3")
									;[End Block]
								Case 4
									;[Block]
									msg\DeathMsg = Format(GetLocalString("death", "kill_4"), Occupation)
									;[End Block]
							End Select
							;[End Block]
						Case "tele"
							;[Block]
							Args = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
							StrTemp = Piece(Args, 1, " ")
							StrTemp2 = Piece(Args, 2, " ")
							StrTemp3 = Piece(Args, 3, " ")
							PositionEntity(me\Collider, Float(StrTemp), Float(StrTemp2), Float(StrTemp3))
							PositionEntity(Camera, Float(StrTemp), Float(StrTemp2), Float(StrTemp3))
							ResetEntity(me\Collider)
							ResetEntity(Camera)
							CreateConsoleMsg(Format(Format(Format(GetLocalString("console", "tele"), EntityX(me\Collider), "{0}"), EntityY(me\Collider), "{1}"), EntityZ(me\Collider), "{2}"))
							;[End Block]
						Case "asd"
							;[Block]
							chs\NoBlink = True
							chs\NoTarget = True
							chs\NoClip = True
							chs\GodMode = True
							chs\InfiniteStamina = True
							
							opt\CameraFogFar = 17.0
							
							KillSounds()
							
							For e.Events = Each Events
								If e\EventID = e_cont1_173
									For i = 0 To 2
										If e\room\NPC[i] <> Null Then RemoveNPC(e\room\NPC[i])
										If i < 2 Then FreeEntity(e\room\Objects[i]) : e\room\Objects[i] = 0
									Next
									If n_I\Curr173\Idle = 1 Then n_I\Curr173\Idle = 0
									PositionEntity(n_I\Curr173\Collider, 0.0, 0.0, 0.0)
									ResetEntity(n_I\Curr173\Collider)
									RemoveEvent(e)
									Exit
								EndIf
							Next
							CreateConsoleMsg(GetLocalString("console", "stfu"))
							;[End Block]
						Case "notarget", "nt"
							;[Block]
							StrTemp = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
							
							Select(StrTemp)
								Case "on", "1", "true"
									;[Block]
									chs\NoTarget = True
									;[End Block]
								Case "off", "0", "false"
									;[Block]
									chs\NoTarget = False
									;[End Block]
								Default
									;[Block]
									chs\NoTarget = (Not chs\NoTarget)
									;[End Block]
							End Select
							
							If (Not chs\NoTarget)
								CreateConsoleMsg(GetLocalString("console", "nt.off"))
							Else
								CreateConsoleMsg(GetLocalString("console", "nt.on"))
							EndIf
							;[End Block]
						Case "spawnpumpkin", "pumpkin"
							;[Block]
							CreateConsoleMsg(GetLocalString("console", "pumpkin"))
							;[End Block]
						Case "teleport173"
							;[Block]
							PositionEntity(n_I\Curr173\Collider, EntityX(me\Collider), EntityY(me\Collider) + 0.2, EntityZ(me\Collider))
							ResetEntity(n_I\Curr173\Collider)
							;[End Block]
						Case "seteventstate"
							;[Block]
							Args = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
							StrTemp = Piece(Args, 1, " ")
							StrTemp2 = Piece(Args, 2, " ")
							StrTemp3 = Piece(Args, 3, " ")
							StrTemp4 = Piece(Args, 4, " ")
							
							Local PL_Room_Found% = False
							
							If StrTemp = "" Lor StrTemp2 = "" Lor StrTemp3 = "" Lor StrTemp4 = ""
								CreateConsoleMsg(GetLocalString("console", "ses.failed"), 255, 150, 0)
							Else
								For e.Events = Each Events
									If PlayerRoom = e\room
										If Lower(StrTemp) <> "keep" Then e\EventState = Float(StrTemp)
										If Lower(StrTemp2) <> "keep" Then e\EventState2 = Float(StrTemp2)
										If Lower(StrTemp3) <> "keep" Then e\EventState3 = Float(StrTemp3)
										If Lower(StrTemp4) <> "keep" Then e\EventState4 = Float(StrTemp4)
										CreateConsoleMsg(Format(Format(Format(Format(GetLocalString("console", "ses.success"), e\EventState, "{0}"), e\EventState2, "{1}"), e\EventState3, "{2}"), e\EventState4, "{3}"))
										PL_Room_Found = True
										Exit
									EndIf
								Next
								If (Not PL_Room_Found) Then CreateConsoleMsg(GetLocalString("console", "ses.failed.apply"), 255, 150, 0)
							EndIf
							;[End Block]
						Case "giveachievement"
							;[Block]
							If Instr(ConsoleInput, " ") <> 0
								StrTemp = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
							Else
								StrTemp = ""
							EndIf
							
							If StrTemp = "all"
								For i = 0 To MaxAchievements - 2 Step 2
									achv\Achievement[i] = True
									achv\Achievement[i + 1] = True
								Next
								CreateConsoleMsg(GetLocalString("console", "ga.all"))
							EndIf
							
							If Int(StrTemp) >= 0 And Int(StrTemp) < MaxAchievements And StrTemp <> "all"
								achv\Achievement[Int(StrTemp)] = True
								CreateConsoleMsg(Format(GetLocalString("console", "ga.success"), achv\AchievementStrings[Int(StrTemp)]))
							ElseIf StrTemp <> "all"
								CreateConsoleMsg(Format(GetLocalString("console", "ga.failed"), Int(StrTemp)), 255, 0, 0)
							EndIf
							;[End Block]
						Case "427state"
							;[Block]
							StrTemp = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
							
							I_427\Timer = 70.0 * Float(StrTemp)
							;[End Block]
						Case "teleport106"
							;[Block]
							n_I\Curr106\State = 0.0
							n_I\Curr106\Idle = 0
							;[End Block]
						Case "jorge"
							;[Block]
							CreateConsoleMsg(GetLocalString("console", "jorge"))
							;[End Block]
						Case "resetfunds"
							;[Block]
							me\Funds = Rand(6)
							CreateConsoleMsg(GetLocalString("console", "funds"))
							;[End Block]
						Case "codes"
							;[Block]
							CreateConsoleMsg(GetLocalString("console", "codes_1"))
							CreateConsoleMsg("")
							CreateConsoleMsg(Format(GetLocalString("console", "codes_2"), CODE_DR_MAYNARD))
							CreateConsoleMsg(Format(GetLocalString("console", "codes_3"), CODE_DR_HARP))
							CreateConsoleMsg(Format(GetLocalString("console", "codes_4"), CODE_DR_L))
							CreateConsoleMsg(Format(GetLocalString("console", "codes_5"), CODE_O5_COUNCIL))
							CreateConsoleMsg(Format(GetLocalString("console", "codes_6"), CODE_MAINTENANCE_TUNNELS))
							CreateConsoleMsg(Format(GetLocalString("console", "codes_7"), CODE_CONT1_035))
							CreateConsoleMsg("")
							CreateConsoleMsg(GetLocalString("console", "codes_8"))
							;[End Block]
						Case "weaponcheats", "wch"
							;[Block]
							Local wep.Weapons
							
							For wep = Each Weapons
								If wep\ID = wep_I\Using
									wep\CurrentAmmo = wep\MaximumCurrentAmmo
									me\HeldAmmo[wep\Caliber] = 270
								EndIf
							Next
							;[End Block]
						Case "loadzone", "changezone", "lz", "cz"
							;[Block]
							StrTemp = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
							
							If Int(StrTemp) > 0 And Int(StrTemp) < MaxMapZones
								If Int(StrTemp) = szl\CurrentZone
									CreateConsoleMsg(Format(GetLocalString("console", "loadzone_1"), Int(StrTemp)), 255, 0, 0)
								Else
									ChangeZone(Int(StrTemp))
								EndIf
							Else
								CreateConsoleMsg(Format(GetLocalString("console", "loadzone_2"), Int(StrTemp)), 255, 0, 0)
							EndIf
							;[End Block]
						Default
							;[Block]
							CreateConsoleMsg(GetLocalString("console", "notfound"), 255, 0, 0)
							;[End Block]
					End Select
					;[End Block]
				Case 1 ; ~ Main Menu
					;[Block]
					Select(Lower(StrTemp))
						Case "help"
							;[Block]
							If Instr(ConsoleInput, " ") <> 0
								StrTemp = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
							Else
								StrTemp = ""
							EndIf
							ConsoleR = 0 : ConsoleG = 255 : ConsoleB = 255
							
							Select(StrTemp)
								Case "1", ""
									;[Block]
									CreateConsoleMsg(GetLocalString("console", "help_1.1"))
									CreateConsoleMsg("******************************")
									CreateConsoleMsg("- loadzone")
									CreateConsoleMsg("******************************")
									CreateConsoleMsg(GetLocalString("console", "help_1.2"))
									CreateConsoleMsg(GetLocalString("console", "help.command"))
									CreateConsoleMsg("******************************")
									;[End Block]
								Case "stopsound", "stfu"
									;[Block]
									CreateConsoleMsg(Format(GetLocalString("console", "help.title"), "stopsound"))
									CreateConsoleMsg("******************************")
									CreateConsoleMultiMsg(GetLocalString("console", "help.stfu"))
									CreateConsoleMsg("******************************")
									;[End Block]
								Case "loadzone" 
									;[Block]
									CreateConsoleMsg(Format(GetLocalString("console", "help.title"), "loadzone"))
									CreateConsoleMsg("******************************")
									CreateConsoleMultiMsg(GetLocalString("console", "help.loadzone"))
									CreateConsoleMsg("******************************")
									;[End Block]
								Default
									;[Block]
									CreateConsoleMsg(GetLocalString("console", "help.no"), 255, 150, 0)
									;[End Block]
							End Select
							;[End Block]
						Case "stopsound", "stfu"
							;[Block]
							KillSounds()
							CreateConsoleMsg(GetLocalString("console", "stfu"))
							;[End Block]
						Case "loadzone", "changezone", "lz", "cz"
							;[Block]
							StrTemp = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
							
							If Int(StrTemp) > 0 And Int(StrTemp) < MaxMapZones
								CurrSave = New Save
								If CurrSave\Name = "" Then CurrSave\Name = ConvertToANSI(GetLocalString("save", "untitled"))
								If RandomSeed = "" Then RandomSeed = MilliSec
								SeedRnd(GenerateSeedNumber(RandomSeed))
								szl\CurrentZone = Int(StrTemp)
								InitNewGame()
								MainMenuOpen = False
							Else
								CreateConsoleMsg(Format(GetLocalString("console", "loadzone_2"), Int(StrTemp)), 255, 0, 0)
							EndIf
							;[End Block]
						Default
							;[Block]
							CreateConsoleMsg(GetLocalString("console", "notfound"), 255, 0, 0)
							;[End Block]
					End Select
					;[End Block]
				Case 2 ; ~ Mission Mode
					;[Block]
					Select(Lower(StrTemp))
						Case "help"
							;[Block]
							If Instr(ConsoleInput, " ") <> 0
								StrTemp = Lower(Right(ConsoleInput, Len(ConsoleInput) - Instr(ConsoleInput, " ")))
							Else
								StrTemp = ""
							EndIf
							ConsoleR = 0 : ConsoleG = 255 : ConsoleB = 255
							
							Select(StrTemp)
								Case "1", ""
									;[Block]
									CreateConsoleMsg(GetLocalString("console", "help_1.1"))
									CreateConsoleMsg("******************************")
									
									CreateConsoleMsg("******************************")
									CreateConsoleMsg(GetLocalString("console", "help_1.2"))
									CreateConsoleMsg(GetLocalString("console", "help.command"))
									CreateConsoleMsg("******************************")
									;[End Block]
									
								Default
									;[Block]
									CreateConsoleMsg(GetLocalString("console", "help.no"), 255, 150, 0)
									;[End Block]
							End Select
							;[End Block]
						Default
							;[Block]
							CreateConsoleMsg(GetLocalString("console", "notfound"), 255, 0, 0)
							;[End Block]
					End Select
					;[End Block]
			End Select
			ConsoleInput = ""
		EndIf
		
		Local Count% = 0
		
		For cm.ConsoleMsg = Each ConsoleMsg
			Count = Count + 1
			If Count > 1000 Then Delete(cm)
		Next
	EndIf
	
	CatchErrors("Uncaught: UpdateConsole()")
End Function

Function RenderConsole%()
	If (Not opt\CanOpenConsole) Then Return
	
	CatchErrors("RenderConsole()")
	
	If ConsoleOpen
		Local cm.ConsoleMsg
		Local InBar%, InBox%
		Local x%, y%, Width%, Height%
		Local TempStr$
		Local CoordEx% = 26 * MenuScale
		
		SetFontEx(fo\FontID[Font_Console])
		
		x = 0
		y = opt\GraphicHeight - 300 * MenuScale
		Width = opt\GraphicWidth
		Height = 270 * MenuScale
		
		RenderFrame(x, y, Width, Height + (30 * MenuScale))
		
		Local ConsoleHeight% = 0
		Local ScrollBarHeight% = 0
		
		For cm.ConsoleMsg = Each ConsoleMsg
			ConsoleHeight = ConsoleHeight + (15 * MenuScale)
		Next
		ScrollBarHeight = (Float(Height) / Float(ConsoleHeight)) * Height
		If ScrollBarHeight > Height Then ScrollBarHeight = Height
		If ConsoleHeight < Height Then ConsoleHeight = Height
		
		Color(50, 50, 50)
		InBar = MouseOn(x + Width - CoordEx, y, CoordEx, Height)
		If InBar Then Color(70, 70, 70)
		Rect(x + Width - CoordEx, y, CoordEx, Height)
		
		Color(120, 120, 120)
		InBox = MouseOn(x + Width - (23 * MenuScale), y + Height - ScrollBarHeight + (ConsoleScroll * ScrollBarHeight / Height), 20 * MenuScale, ScrollBarHeight)
		If InBox Then Color(200, 200, 200)
		If ConsoleScrollDragging Then Color(255, 255, 255)
		Rect(x + Width - (23 * MenuScale), y + Height - ScrollBarHeight + (ConsoleScroll * ScrollBarHeight / Height), 20 * MenuScale, ScrollBarHeight)
		
		Color(255, 255, 255)
		
		Local TempY# = y + Height - (25.0 * MenuScale) - ConsoleScroll
		Local Count% = 0
		
		For cm.ConsoleMsg = Each ConsoleMsg
			Count = Count + 1
			If Count > 1000
				Delete(cm)
			Else
				If TempY >= y And TempY < y + Height - (20 * MenuScale)
					If cm = ConsoleReissue
						Color(cm\R / 4, cm\G / 4, cm\B / 4)
						Rect(x, TempY - (2 * MenuScale), Width - (30 * MenuScale), 24 * MenuScale, True)
					EndIf
					Color(cm\R, cm\G, cm\B)
					If cm\IsCommand
						TempStr = "> " + cm\Txt
					Else
						TempStr = cm\Txt
					EndIf
					TextEx(x + (20 * MenuScale), TempY, TempStr)
				EndIf
				TempY = TempY - (15.0 * MenuScale)
			EndIf
		Next
		Color(255, 255, 255)
		
		RenderMenuInputBoxes()
		
		RenderCursor()
	EndIf
	SetFontEx(fo\FontID[Font_Default])
	
	CatchErrors("Uncaught: RenderConsole()")
End Function

Function ClearConsole%(Section% = 0)
	Local c.ConsoleMsg
	
	For c.ConsoleMsg = Each ConsoleMsg
		Delete(c)
	Next
	
	ConsoleR = 0 : ConsoleG = 255 : ConsoleB = 255
	
	Select(Section)
		Case 0 ; ~ Story Mode (Main Game)
			;[Block]
			CreateConsoleMsg("")
			CreateConsoleMsg("Console commands: ")
			CreateConsoleMsg(" - help [Page]")
			CreateConsoleMsg(" - teleport [Room Name]")
			CreateConsoleMsg(" - godmode [On / O$ff]")
			CreateConsoleMsg(" - noclip [On / Off]")
			CreateConsoleMsg(" - infinitestamina [On / Off]")
			CreateConsoleMsg(" - noblink [On / Off]")
			CreateConsoleMsg(" - notarget [On / Off]")
			CreateConsoleMsg(" - noclipspeed [X] (Default = 2.0)")
			CreateConsoleMsg(" - wireframe [On / Off]")
			CreateConsoleMsg(" - debughud [Category]")
			CreateConsoleMsg(" - camerafog [Near] [Far]")
			CreateConsoleMsg(" - heal")
			CreateConsoleMsg(" - revive")
			CreateConsoleMsg(" - asd")
			CreateConsoleMsg(" - spawnitem [Item Name]")
			CreateConsoleMsg(" - 106retreat")
			CreateConsoleMsg(" - disable173 / enable173")
			CreateConsoleMsg(" - disable106 / enable106")
			CreateConsoleMsg(" - spawn [NPC Type]")
			;[End Block]
		Case 1 ; ~ Main Menu
			;[Block]
			CreateConsoleMsg("")
			CreateConsoleMsg("Console commands: ")
			CreateConsoleMsg(" - help [Page]")
			CreateConsoleMsg(" - stopsound")
			CreateConsoleMsg(" - loadzone [Zone ID]")
			;[End Block]
		Case 2 ; ~ Mission Mode
			;[Block]
			CreateConsoleMsg("")
			CreateConsoleMsg("Console commands: ")
			CreateConsoleMsg(" - help [Page]")
			;[End Block]
	End Select
	
End Function

Function OpenConsoleOnError%(ConsoleMsg$)
	If MenuOpen Lor ConsoleOpen Lor (Not opt\ConsoleOpening) Lor (Not opt\CanOpenConsole) Then Return
	If (MilliSec Mod 1500) < 800
		If ConsoleMsg <> "" Then CreateConsoleMsg(ConsoleMsg)
		ConsoleOpen = True
	EndIf
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D TSS