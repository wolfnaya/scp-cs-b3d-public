Function RenderHUD%()
	If me\Health = 0 Lor me\FallTimer < 0.0 Lor (Not me\Playable) Lor (Not me\ShowHUD) Then Return
	
	Local x%, y%, Width%, Height%, WalkIconID%, BlinkIconID%
	Local i%
	Local PlayerPosY# = EntityY(me\Collider)
	Local IconColoredRectSize% = 36 * MenuScale
	Local IconColoredRectSpaceX% = 53 * MenuScale
	Local IconColoredRectSpaceY% = 3 * MenuScale
	Local IconRectSize% = 32 * MenuScale
	Local IconRectSpace% = 51 * MenuScale
	Local IconSpace% = 50 * MenuScale
	Local ySpace% = 40 * MenuScale
	
	Width = 200 * MenuScale
	Height = 20 * MenuScale
	x = 80 * MenuScale
	y = opt\GraphicHeight - (15 * MenuScale)
	
	Color(255, 255, 255)
	y = y - ySpace
	If me\Stamina <= 25.0
		RenderBar(x, y, Width, Height, me\Stamina, 100.0, 50, 0, 0)
	Else
		RenderBar(x, y, Width, Height, me\Stamina, 100.0, 50, 50, 50)
	EndIf
	Color(255, 255, 255)
	If (PlayerRoom\RoomTemplate\RoomID = r_dimension_106 And (PlayerPosY < 2000.0 * RoomScale Lor PlayerPosY > 2608.0 * RoomScale)) Lor I_714\Using > 0 Lor me\Health < 50 Lor me\StaminaEffect > 1.0 Lor wi\HazmatSuit = 1 Lor wi\BallisticVest = 2 Lor I_409\Timer >= 55.0 Lor I_1025\State[0] > 0.0
		Color(200, 0, 0)
		Rect(x - IconColoredRectSpaceX, y - IconColoredRectSpaceY, IconColoredRectSize, IconColoredRectSize)
	ElseIf chs\InfiniteStamina Lor me\StaminaEffect < 1.0 Lor wi\GasMask >= 3 Lor wi\HazmatSuit >= 3
		Color(0, 200, 0)
		Rect(x - IconColoredRectSpaceX, y - IconColoredRectSpaceY, IconColoredRectSize, IconColoredRectSize)
	EndIf
	Color(255, 255, 255)
	Rect(x - IconRectSpace, y, IconRectSize, IconRectSize, False)
	If me\Crouch
		WalkIconID = 2
	ElseIf (KeyDown(key\SPRINT) And (Not InvOpen) And OtherOpen = Null) And me\CurrSpeed > 0.0 And (Not chs\NoClip) And me\Stamina > 0.0
		WalkIconID = 1
	Else
		WalkIconID = 0
	EndIf
	DrawBlock(t\IconID[WalkIconID], x - IconSpace, y + 1)
	
	Color(255, 255, 255)
	y = y - ySpace
	If me\BlinkTimer < 150.0
		RenderBar(x, y, Width, Height, me\BlinkTimer, me\BLINKFREQ, 100, 0, 0)
	Else
		RenderBar(x, y, Width, Height, me\BlinkTimer, me\BLINKFREQ)
	EndIf
	Color(255, 255, 255)
	If me\BlurTimer > 550.0 Lor me\BlinkEffect > 1.0 Lor me\LightFlash > 0.0 Lor ((SecondaryLightOn < 0.3 Lor me\EyeIrritation > 0.0) And wi\NightVision = 0)
		Color(200, 0, 0)
		Rect(x - IconColoredRectSpaceX, y - IconColoredRectSpaceY, IconColoredRectSize, IconColoredRectSize)
	ElseIf me\BlinkEffect < 1.0 Lor chs\NoBlink
		Color(0, 200, 0)
		Rect(x - IconColoredRectSpaceX, y - IconColoredRectSpaceY, IconColoredRectSize, IconColoredRectSize)
	EndIf
	Color(255, 255, 255)
	Rect(x - IconRectSpace, y, IconRectSize, IconRectSize, False)
	If me\BlinkTimer < 0.0
		BlinkIconID = 4
	Else
		BlinkIconID = 3
	EndIf
	DrawBlock(t\IconID[BlinkIconID], x - IconSpace, y + 1)
	
	If (I_714\Using > 0 And Remove714Timer < 500.0) Lor (wi\HazmatSuit > 0 And RemoveHazmatTimer < 500.0)
		Color(255, 255, 255)
		y = y - ySpace
		If wi\HazmatSuit > 0
			If RemoveHazmatTimer < 125.0
				RenderBar( x, y, Width, Height, RemoveHazmatTimer, 500.0, 100, 0, 0)
			Else
				RenderBar(x, y, Width, Height, RemoveHazmatTimer, 500.0)
			EndIf
		Else
			If Remove714Timer < 125.0
				RenderBar(x, y, Width, Height, Remove714Timer, 500.0, 100, 0, 0)
			Else
				RenderBar(x, y, Width, Height, Remove714Timer, 500.0)
			EndIf
		EndIf
		Color(255, 255, 255)
		If wi\HazmatSuit = 4
			Color(0, 200, 0)
			Rect(x - IconColoredRectSpaceX, y - IconColoredRectSpaceY, IconColoredRectSize, IconColoredRectSize)
		ElseIf I_714\Using = 1
			Color(200, 0, 0)
			Rect(x - IconColoredRectSpaceX, y - IconColoredRectSpaceY, IconColoredRectSize, IconColoredRectSize)
		EndIf
		Color(255, 255, 255)
		Rect(x - IconRectSpace, y, IconRectSize, IconRectSize, False)
		DrawBlock(t\IconID[Icon_Shield], x - IconSpace, y + 1)
	EndIf
	If I_268\Using > 1
		Color(255, 255, 255)
		y = y - ySpace
		If I_268\Timer < 150.0
			RenderBar(x, y, Width, Height, I_268\Timer, 600.0, 100, 0, 0)
		Else
			RenderBar(x, y, Width, Height, I_268\Timer, 600.0)
		EndIf
		Color(255, 255, 255)
		If I_268\Timer =< 0.0
			Color(150, 150, 0)
			Rect(x - IconColoredRectSpaceX, y - IconColoredRectSpaceY, IconColoredRectSize, IconColoredRectSize)
		ElseIf I_714\Using > 0
			Color(200, 0, 0)
			Rect(x - IconColoredRectSpaceX, y - IconColoredRectSpaceY, IconColoredRectSize, IconColoredRectSize)
		ElseIf I_268\Using = 3
			Color(0, 200, 0)
			Rect(x - IconColoredRectSpaceX, y - IconColoredRectSpaceY, IconColoredRectSize, IconColoredRectSize)
		EndIf
		Color(255, 255, 255)
		Rect(x - IconRectSpace, y, IconRectSize, IconRectSize, False)
		DrawBlock(t\IconID[Icon_SCP_268], x - IconSpace, y + 1)
	EndIf
End Function

Function RenderWeaponsHUD%()
	Local wep.Weapons, x%, y%
	Local Ammo$, CaliberName$, FireModeName$
	
	x = opt\GraphicWidth - 120
	y = opt\GraphicHeight - 60
	
	For wep.Weapons = Each Weapons
		If wep\ID = wep_I\Using
			
			; ~ Determining Names
			;[Block]
			Select(wep\Caliber)
				Case Cal_5_7X28mm
					CaliberName = "5.7X28mm"
				Case Cal_9mm
					CaliberName = "9x19mm"
				Case Cal_10mm
					CaliberName = "10mm"
				Case Cal_45ACP
					CaliberName = ".45ACP"
				Case Cal_5_56X45mm
					CaliberName = "5.56x45mm"
				Case Cal_7_62X39mm
					CaliberName = "7.62x39mm"
				Case Cal_12X70mm
					CaliberName = "12x70mm"
				Case Cal_GAUSS
					CaliberName = "EMR-P Bolts"
			End Select
			
			Select(wep\FireMode)
				Case FireMode_Single
					If wep\WeaponType <> WeaponType_Shotgun
						FireModeName = "Semi-Auto"
					ElseIf wep\WeaponType = WeaponType_Shotgun
						FireModeName = "Pump-Action"
					EndIf
				Case FireMode_Auto
					FireModeName = "Auto"
			End Select
			;[End Block]
			
			; ~ Actual Rendering
			;[Block]
			If wep\WeaponType <> WeaponType_Melee And wep\ID <> Wep_EMRP And (Not SelectedDifficulty\Realism)
				SetFont(fo\FontID[Font_Default_Medium])
				Color 200, 200, 200
				TextEx(x, y, FireModeName, True, True)
			EndIf
			If wep\Caliber <> -1
				SetFont(fo\FontID[Font_Default])
				y = y - 30 * MenuScale
				Color 100, 100, 100
				TextEx(x, y, CaliberName + "/" + me\HeldAmmo[wep\Caliber], True, True)
			EndIf
			If (Not SelectedDifficulty\Realism)
				If wep\CurrentAmmo <= wep\MaximumCurrentAmmo
					Ammo = wep\CurrentAmmo + "/" + wep\MaximumCurrentAmmo
				Else
					SetFont(fo\FontID[Font_Default])
					Ammo = wep\MaximumCurrentAmmo + "+1/" + wep\MaximumCurrentAmmo
				EndIf
				Color 0, 100, 50
			Else
				If wep\CurrentAmmo > wep\MaximumCurrentAmmo
					Ammo = GetLocalString("weapons", "ammo_more_than_full")
				ElseIf wep\CurrentAmmo <= wep\MaximumCurrentAmmo And wep\CurrentAmmo > wep\MaximumCurrentAmmo / 1.2
					Ammo = GetLocalString("weapons", "ammo_full")
				ElseIf wep\CurrentAmmo <= wep\MaximumCurrentAmmo / 1.2 And wep\CurrentAmmo > wep\MaximumCurrentAmmo / 1.5
					Ammo = GetLocalString("weapons", "ammo_almost_full")
				ElseIf wep\CurrentAmmo <= wep\MaximumCurrentAmmo / 1.5 And wep\CurrentAmmo > wep\MaximumCurrentAmmo / 2
					Ammo = GetLocalString("weapons", "ammo_more_than_half")
				ElseIf wep\CurrentAmmo <= wep\MaximumCurrentAmmo / 2 And wep\CurrentAmmo > wep\MaximumCurrentAmmo / 2.3
					Ammo = GetLocalString("weapons", "ammo_half")
				ElseIf wep\CurrentAmmo <= wep\MaximumCurrentAmmo / 2.3 And wep\CurrentAmmo > wep\MaximumCurrentAmmo / 3
					Ammo = GetLocalString("weapons", "ammo_almost_half")
				ElseIf wep\CurrentAmmo <= wep\MaximumCurrentAmmo / 3 And wep\CurrentAmmo > wep\MaximumCurrentAmmo / 5
					Ammo = GetLocalString("weapons", "ammo_less_than_half")
				ElseIf wep\CurrentAmmo <= wep\MaximumCurrentAmmo / 5 And wep\CurrentAmmo > 0
					Ammo = GetLocalString("weapons", "ammo_almost_empty")
				ElseIf wep\CurrentAmmo = 0
					Ammo = GetLocalString("weapons", "ammo_empty")
				EndIf
				Color 220, 220, 220
			EndIf
			SetFont(fo\FontID[Font_Default_Medium])
			y = y - 30 * MenuScale
			TextEx(x, y, Ammo, True, True)
			If (Not SelectedDifficulty\Realism)
				y = y - 30 * MenuScale
				Color 200, 200, 200
				TextEx(x, y, wep\Name, True, True)
			EndIf
			;[End Block]
		EndIf
	Next
	Color 255, 255, 255
	SetFont(fo\FontID[Font_Default])
	
End Function

Function RenderDebugHUD%()
	Local ev.Events, wep.Weapons
	Local x%, y%, i%
	
	x = 20 * MenuScale
	y = 40 * MenuScale
	
	Color(255, 255, 255)
	SetFontEx(fo\FontID[Font_Console])
	
	Select(chs\DebugHUD)
		Case 1
			;[Block]
			TextEx(x, y, Format(GetLocalString("misc", "room"), PlayerRoom\RoomTemplate\RoomID))
			TextEx(x, y + (20 * MenuScale), Format(Format(Format(GetLocalString("console", "debug_1.xyz"), Floor(EntityX(PlayerRoom\OBJ) / 8.0 + 0.5), "{0}"), Floor(EntityZ(PlayerRoom\OBJ) / 8.0 + 0.5), "{1}"), PlayerRoom\Angle, "{2}"))
			For ev.Events = Each Events
				If ev\room = PlayerRoom
					TextEx(x, y + (40 * MenuScale), Format(GetLocalString("console", "debug_1.event_new"), ev\EventID))
					TextEx(x, y + (60 * MenuScale), Format(GetLocalString("console", "debug_1.state_1"), ev\EventState))
					TextEx(x, y + (80 * MenuScale), Format(GetLocalString("console", "debug_1.state_2"), ev\EventState2))
					TextEx(x, y + (100 * MenuScale), Format(GetLocalString("console", "debug_1.state_3"), ev\EventState3))
					TextEx(x, y + (120 * MenuScale), Format(GetLocalString("console", "debug_1.state_4"), ev\EventState4))
					TextEx(x, y + (140 * MenuScale), Format(GetLocalString("console", "debug_1.str"), ev\EventStr))
					Exit
				EndIf
			Next
			TextEx(x, y + (200 * MenuScale), Format(Format(Format(GetLocalString("console", "debug_1.currroom"), PlayerRoom\x, "{0}"), PlayerRoom\y, "{1}"), PlayerRoom\z, "{2}"))
			
			If sc_I\SelectedMonitor = Null
				TextEx(x, y + (240 * MenuScale), Format(GetLocalString("console", "debug_1.currmon"), "Null"))
			EndIf
			
			If SelectedItem <> Null
				TextEx(x, y + (280 * MenuScale), Format(GetLocalString("console", "debug_1.curritem"), SelectedItem\ItemTemplate\Name))
			ElseIf d_I\ClosestButton = 0
				TextEx(x, y + (280 * MenuScale), Format(GetLocalString("console", "debug_1.currbtn"), "Null"))
			EndIf
			
			TextEx(x, y + (320 * MenuScale), Format(GetLocalString("console", "debug_1.currflo"), InFacility))
			TextEx(x, y + (340 * MenuScale), Format(GetLocalString("console", "debug_1.roomflo"), ToMTElevatorFloor))
			If PlayerInsideMTElevator
				TextEx(x, y + (360 * MenuScale), Format(GetLocalString("console", "debug_1.inelev"), "True"))
			Else
				TextEx(x, y + (360 * MenuScale), Format(GetLocalString("console", "debug_1.inelev"), "False"))
			EndIf
			
			TextEx(x, y + (400 * MenuScale), Format(Format(GetLocalString("console", "debug_1.time"), CurrentDate(), "{0}"), CurrentTime(), "{1}"))
			TextEx(x, y + (420 * MenuScale), Format(Format(GetLocalString("console", "debug_1.vidmem"), ((TotalVidMem() / 1024) - (AvailVidMem() / 1024)), "{0}"), (TotalVidMem() / 1024), "{1}"))
			TextEx(x, y + (440 * MenuScale), Format(Format(GetLocalString("console", "debug_1.glomem"), ((TotalPhys() / 1024) - (AvailPhys() / 1024)), "{0}"), (TotalPhys() / 1024), "{1}"))
			TextEx(x, y + (460 * MenuScale), Format(GetLocalString("console", "debug_1.triamo"), CurrTrisAmount))
			TextEx(x, y + (480 * MenuScale), Format(GetLocalString("console", "debug_1.acttex"), ActiveTextures()))
			;[End Block]
		Case 2
			;[Block]
			TFormPoint(EntityX(me\Collider), EntityY(me\Collider), EntityZ(me\Collider), 0, PlayerRoom\OBJ)
			TextEx(x, y, Format(Format(Format(GetLocalString("console", "debug_2.ppos"), FloatToString(TFormedX(), 1), "{0}"), FloatToString(TFormedY(), 1), "{1}"), FloatToString(TFormedZ(), 1), "{2}"))
			TextEx(x, y + (20 * MenuScale), Format(Format(Format(GetLocalString("console", "debug_2.pcampos"), FloatToString(EntityX(Camera), 1), "{0}"), FloatToString(EntityY(Camera), 1), "{1}"), FloatToString(EntityZ(Camera), 1), "{2}"))
			TextEx(x, y + (40 * MenuScale), Format(Format(Format(GetLocalString("console", "debug_2.prot"), FloatToString(EntityPitch(me\Collider), 1), "{0}"), FloatToString(EntityYaw(me\Collider), 1), "{1}"), FloatToString(EntityRoll(me\Collider), 1), "{2}"))
			
			TextEx(x, y + (80 * MenuScale), Format(GetLocalString("console", "debug_2.health"), me\Health))
			TextEx(x, y + (100 * MenuScale), Format(GetLocalString("console", "debug_2.bloodloss"), me\Bloodloss))
			
			TextEx(x, y + (140 * MenuScale), Format(GetLocalString("console", "debug_2.blur"), me\BlurTimer))
			TextEx(x, y + (160 * MenuScale), Format(GetLocalString("console", "debug_2.blink"), me\LightBlink))
			TextEx(x, y + (180 * MenuScale), Format(GetLocalString("console", "debug_2.flash"), me\LightFlash))
			
			TextEx(x, y + (220 * MenuScale), Format(GetLocalString("console", "debug_2.freq"), me\BLINKFREQ))
			TextEx(x, y + (240 * MenuScale), Format(GetLocalString("console", "debug_2.timer"), me\BlinkTimer))
			TextEx(x, y + (260 * MenuScale), Format(GetLocalString("console", "debug_2.effect"), me\BlinkEffect))
			TextEx(x, y + (280 * MenuScale), Format(GetLocalString("console", "debug_2.efftim"), me\BlinkEffectTimer))
			TextEx(x, y + (300 * MenuScale), Format(GetLocalString("console", "debug_2.eyeirr"), me\EyeIrritation))
			TextEx(x, y + (320 * MenuScale), Format(GetLocalString("console", "debug_2.eyestuck"), me\EyeStuck))
			
			TextEx(x, y + (360 * MenuScale), Format(GetLocalString("console", "debug_2.stamina"), me\Stamina))
			TextEx(x, y + (380 * MenuScale), Format(GetLocalString("console", "debug_2.stameff"), me\StaminaEffect))
			TextEx(x, y + (400 * MenuScale), Format(GetLocalString("console", "debug_2.stamtimer"), me\StaminaEffectTimer))
			
			TextEx(x, y + (440 * MenuScale), Format(GetLocalString("console", "debug_2.deaf"), me\DeafTimer))
			TextEx(x, y + (460 * MenuScale), Format(GetLocalString("console", "debug_2.sanity"), me\Sanity))
			
			x = x + (700 * MenuScale)
			
			If me\Health = 0
				TextEx(x, y, Format(GetLocalString("console", "debug_2.terminated"), "True"))
			Else
				TextEx(x, y, Format(GetLocalString("console", "debug_2.terminated"), "False"))
			EndIf
			
			TextEx(x, y + (20 * MenuScale), Format(GetLocalString("console", "debug_2.death"), me\DeathTimer))
			TextEx(x, y + (40 * MenuScale), Format(GetLocalString("console", "debug_2.fall"), me\FallTimer))
			
			TextEx(x, y + (80 * MenuScale), Format(GetLocalString("console", "debug_2.heal"), me\HealTimer))
			
			TextEx(x, y + (120 * MenuScale), Format(GetLocalString("console", "debug_2.heartbeat"), me\HeartBeatTimer))
			
			TextEx(x, y + (160 * MenuScale), Format(GetLocalString("console", "debug_2.explosion"), me\ExplosionTimer))
			
			TextEx(x, y + (200 * MenuScale), Format(GetLocalString("console", "debug_2.speed"), me\CurrSpeed))
			
			TextEx(x, y + (240 * MenuScale), Format(GetLocalString("console", "debug_2.camshake"), me\CameraShakeTimer))
			TextEx(x, y + (260 * MenuScale), Format(GetLocalString("console", "debug_2.camzoom"), me\CurrCameraZoom))
			
			TextEx(x, y + (300 * MenuScale), Format(GetLocalString("console", "debug_2.vomit"), me\VomitTimer))
			
			If me\Playable
				TextEx(x, y + (340 * MenuScale), Format(GetLocalString("console", "debug_2.playable"), "True"))
			Else
				TextEx(x, y + (340 * MenuScale), Format(GetLocalString("console", "debug_2.playable"), "False"))
			EndIf
			
			TextEx(x, y + (380 * MenuScale), Format(GetLocalString("console", "debug_2.refitems"), me\RefinedItems))
			TextEx(x, y + (400 * MenuScale), Format(GetLocalString("console", "debug_2.funds"), me\Funds))
			TextEx(x, y + (420 * MenuScale), Format(GetLocalString("console", "debug_2.escape"), EscapeTimer))
			;[End Block]
		Case 3
			;[Block]
			If n_I\Curr049 <> Null
				TextEx(x, y, Format(Format(Format(GetLocalString("console", "debug_3.049pos"), FloatToString(EntityX(n_I\Curr049\OBJ), 2), "{0}"), FloatToString(EntityY(n_I\Curr049\OBJ), 2), "{1}"), FloatToString(EntityZ(n_I\Curr049\OBJ), 2), "{2}"))
				TextEx(x, y + (20 * MenuScale), Format(GetLocalString("console", "debug_3.049idle"), n_I\Curr049\Idle))
				TextEx(x, y + (40 * MenuScale), Format(GetLocalString("console", "debug_3.049state"), n_I\Curr049\State))
			EndIf
			If n_I\Curr096 <> Null
				TextEx(x, y + (60 * MenuScale), Format(Format(Format(GetLocalString("console", "debug_3.096pos"), FloatToString(EntityX(n_I\Curr096\OBJ), 2), "{0}"), FloatToString(EntityY(n_I\Curr096\OBJ), 2), "{1}"), FloatToString(EntityZ(n_I\Curr096\OBJ), 2), "{2}"))
				TextEx(x, y + (80 * MenuScale), Format(GetLocalString("console", "debug_3.096idle"), n_I\Curr096\Idle))
				TextEx(x, y + (100 * MenuScale), Format(GetLocalString("console", "debug_3.096state"), n_I\Curr096\State))
			EndIf
			TextEx(x, y + (120 * MenuScale), Format(Format(Format(GetLocalString("console", "debug_3.106pos"), FloatToString(EntityX(n_I\Curr106\OBJ), 2), "{0}"), FloatToString(EntityY(n_I\Curr106\OBJ), 2), "{1}"), FloatToString(EntityZ(n_I\Curr106\OBJ), 2), "{2}"))
			TextEx(x, y + (140 * MenuScale), Format(GetLocalString("console", "debug_3.106idle"), n_I\Curr106\Idle))
			TextEx(x, y + (160 * MenuScale), Format(GetLocalString("console", "debug_3.106state"), n_I\Curr106\State))
			
			TextEx(x, y + (180 * MenuScale), Format(Format(Format(GetLocalString("console", "debug_3.173pos"), FloatToString(EntityX(n_I\Curr173\OBJ), 2), "{0}"), FloatToString(EntityY(n_I\Curr173\OBJ), 2), "{1}"), FloatToString(EntityZ(n_I\Curr173\OBJ), 2), "{2}"))
			TextEx(x, y + (200 * MenuScale), Format(GetLocalString("console", "debug_3.173idle"), n_I\Curr173\Idle))
			TextEx(x, y + (220 * MenuScale), Format(GetLocalString("console", "debug_3.173state"), n_I\Curr173\State))
			
			TextEx(x, y + (260 * MenuScale), Format(GetLocalString("console", "debug_3.pill"), I_500\Taken))
			
			TextEx(x, y + (300 * MenuScale), Format(GetLocalString("console", "debug_3.008"), I_008\Timer))
			TextEx(x, y + (320 * MenuScale), Format(GetLocalString("console", "debug_3.409"), I_409\Timer))
			TextEx(x, y + (340 * MenuScale), Format(GetLocalString("console", "debug_3.427"), Int(I_427\Timer / 70.0)))
			For i = 0 To 7
				TextEx(x, y + ((360 + (20 * i)) * MenuScale), Format(Format(GetLocalString("console", "debug_3.1025"), i, "{0}"), I_1025\State[i], "{1}"))
			Next
			
			If I_005\ChanceToSpawn = 1
				TextEx(x, y + (540 * MenuScale), GetLocalString("console", "debug_3.005.chamber"))
			ElseIf I_005\ChanceToSpawn = 2
				TextEx(x, y + (540 * MenuScale), GetLocalString("console", "debug_3.005.409"))
			Else
				TextEx(x, y + (540 * MenuScale), GetLocalString("console", "debug_3.005.maynard"))
			EndIf
			;[End Block]
		Case 4
			;[Block]
			For wep.Weapons = Each Weapons
				If wep\ID = wep_I\Using
					TextEx(x, y + (20 * MenuScale), Format(GetLocalString("console", "debug_4.weapon"), wep\Name))
					TextEx(x, y + (40 * MenuScale), Format(GetLocalString("console", "debug_4.weapon_temperature"), wep\Temperature + "/" + wep\MaximumTemperature))
				EndIf
			Next
			;[End Block]
	End Select
	SetFontEx(fo\FontID[Font_Default])
End Function

Function RenderInteractions()
	Local Temp%, PitchValue#, YawValue#, x%, y%, i%
	Local ArrowText$, FrameWidth%, FrameHeight%
	
	FrameWidth = 64
	FrameHeight = 64
	
	If (Not (MenuOpen Lor InvOpen Lor ConsoleOpen Lor I_294\Using Lor OtherOpen <> Null Lor d_I\SelectedDoor <> Null Lor SelectedScreen <> Null Lor me\Health = 0))
		Local CoordEx% = 32 * MenuScale
		
		If d_I\ClosestButton <> 0
			Temp = CreatePivot()
			PositionEntity(Temp, EntityX(Camera), EntityY(Camera), EntityZ(Camera))
			PointEntity(Temp, d_I\ClosestButton)
			YawValue = WrapAngle(EntityYaw(Camera) - EntityYaw(Temp))
			If YawValue > 90.0 And YawValue <= 180.0 Then YawValue = 90.0
			If YawValue > 180.0 And YawValue < 270.0 Then YawValue = 270.0
			PitchValue = WrapAngle(EntityPitch(Camera) - EntityPitch(Temp))
			If PitchValue > 90.0 And PitchValue <= 180.0 Then PitchValue = 90.0
			If PitchValue > 180.0 And PitchValue < 270.0 Then PitchValue = 270.0
			
			FreeEntity(Temp) : Temp = 0
			
			RenderFrame(mo\Viewport_Center_X + Sin(YawValue) * (opt\GraphicWidth / 3) - CoordEx, mo\Viewport_Center_Y - Sin(PitchValue) * (opt\GraphicHeight / 3) - CoordEx, FrameWidth, FrameHeight)
			SetFont(fo\FontID[Font_Default_Big])
			TextEx((mo\Viewport_Center_X + Sin(YawValue) * (opt\GraphicWidth / 3) - CoordEx) + (FrameWidth / 2), (mo\Viewport_Center_Y - Sin(PitchValue) * (opt\GraphicHeight / 3) - CoordEx) + (FrameHeight / 2), key\Name[key\INTERACT], True, True)
			SetFont(fo\FontID[Font_Default])
		EndIf
		
		If ClosestItem <> Null
			YawValue = -DeltaYaw(Camera, ClosestItem\Collider)
			If YawValue > 90.0 And YawValue <= 180.0 Then YawValue = 90.0
			If YawValue > 180.0 And YawValue < 270.0 Then YawValue = 270.0
			PitchValue = -DeltaPitch(Camera, ClosestItem\Collider)
			If PitchValue > 90.0 And PitchValue <= 180.0 Then PitchValue = 90.0
			If PitchValue > 180.0 And PitchValue < 270.0 Then PitchValue = 270.0
			
			RenderFrame(mo\Viewport_Center_X + Sin(YawValue) * (opt\GraphicWidth / 3) - CoordEx, mo\Viewport_Center_Y - Sin(PitchValue) * (opt\GraphicHeight / 3) - CoordEx, FrameWidth, FrameHeight)
			SetFont(fo\FontID[Font_Default_Big])
			TextEx((mo\Viewport_Center_X + Sin(YawValue) * (opt\GraphicWidth / 3) - CoordEx) + (FrameWidth / 2), (mo\Viewport_Center_Y - Sin(PitchValue) * (opt\GraphicHeight / 3) - CoordEx) + (FrameHeight / 2), key\Name[key\INTERACT], True, True)
			SetFont(fo\FontID[Font_Default])
		EndIf
		
		If DrawHandIcon
			RenderFrame(mo\Viewport_Center_X - CoordEx, mo\Viewport_Center_Y - CoordEx, FrameWidth, FrameHeight)
			SetFont(fo\FontID[Font_Default_Big])
			TextEx((mo\Viewport_Center_X - CoordEx) + (FrameWidth / 2), (mo\Viewport_Center_Y - CoordEx) + (FrameHeight / 2), key\Name[key\INTERACT], True, True)
			SetFont(fo\FontID[Font_Default])
		EndIf
		
		SetFont(fo\FontID[Font_Default_Medium])
		
		For i = 0 To 3
			x = mo\Viewport_Center_X - CoordEx
			y = mo\Viewport_Center_Y - CoordEx
			If DrawArrowIcon[i]
				Select(i)
					Case 0
						;[Block]
						y = y - (69 * MenuScale)
						ArrowText = "▲"
						;[End Block]
					Case 1
						;[Block]
						x = x + (69 * MenuScale)
						ArrowText = ">"
						;[End Block]
					Case 2
						;[Block]
						y = y + (69 * MenuScale)
						ArrowText = "▼"
						;[End Block]
					Case 3
						;[Block]
						x = x - (69 * MenuScale)
						ArrowText = "<"
						;[End Block]
				End Select
				RenderFrame(x, y, FrameWidth, FrameHeight)
				TextEx(x + (FrameWidth / 2), y + (FrameHeight / 2), ArrowText, True, True)
			EndIf
		Next
	EndIf
	
	SetFont(fo\FontID[Font_Default])
End Function

Function RenderGUI%()
	CatchErrors("RenderGUI()")
	
	Local e.Events, it.Items, a_it.Items
	Local Temp%, x%, y%, z%, i%, YawValue#, PitchValue#
	Local x1#, x2#, x3#, y1#, y2#, y3#, z2#, ProjY#, Scale#, Pvt%
	Local n%, xTemp%, yTemp%, StrTemp$
	Local Width%, Height%
	Local SqrValue#
	Local RID% = PlayerRoom\RoomTemplate\RoomID
	
	If MenuOpen Lor InvOpen Lor ConsoleOpen Lor OtherOpen <> Null Lor d_I\SelectedDoor <> Null Lor cra\EndingTimer > 0.0
		ShowPointer()
	Else
		HidePointer()
	EndIf
	
	If wep_I\HUDTimer > 0.0 Then RenderWeaponsHUD()
	
	If RID = r_dimension_106
		For e.Events = Each Events
			If e\room = PlayerRoom
				If (wi\NightVision > 0 Lor wi\SCRAMBLE > 0) And e\EventState2 <> PD_FakeTunnelRoom
					If e\Img = 0
						e\Img = LoadImage_Strict("GFX\Overlays\scp_106_face_overlay.png")
						e\Img = ScaleImage2(e\Img, MenuScale, MenuScale)
					Else
						DrawBlock(e\Img, mo\Viewport_Center_X - (Rand(310, 390) * MenuScale), mo\Viewport_Center_Y - (Rand(290, 310) * MenuScale))
					EndIf
				Else
					If e\EventState2 = PD_ThroneRoom
						If me\BlinkTimer > -16.0 And me\BlinkTimer < -6.0
							If e\Img2 = 0
								e\Img2 = LoadImage_Strict("GFX\Overlays\kneel_mortal_overlay.png")
								e\Img2 = ScaleImage2(e\Img2, MenuScale, MenuScale)
							Else
								DrawBlock(e\Img2, mo\Viewport_Center_X - (Rand(310, 390) * MenuScale), mo\Viewport_Center_Y - (Rand(290, 310) * MenuScale))
							EndIf
						EndIf
					EndIf
				EndIf
				Exit
			EndIf
		Next
	EndIf
	
	If I_294\Using Then Render294()
	If (opt\HUDEnabled And me\ShowHUD)
		RenderTasks()
		RenderInteractions()
		RenderHUD()
	EndIf
	If chs\DebugHUD <> 0 Then RenderDebugHUD()
	
	If SelectedScreen <> Null Then DrawBlock(SelectedScreen\Img, mo\Viewport_Center_X - 512.0, mo\Viewport_Center_Y - 384.0) ; ~ 1024x768
	
	Local PrevInvOpen% = InvOpen, MouseSlot% = 66
	Local ShouldDrawHUD% = True
	
	If d_I\SelectedDoor <> Null
		If SelectedItem <> Null
			If SelectedItem\ItemTemplate\TempName = "scp005" Then ShouldDrawHUD = False
		EndIf
		If ShouldDrawHUD
			
			Local ButtonPosX# = EntityX(d_I\ClosestButton, True)
			Local ButtonPosY# = EntityY(d_I\ClosestButton, True)
			Local ButtonPosZ# = EntityZ(d_I\ClosestButton, True)
			
			CameraProject(Camera, ButtonPosX, ButtonPosY + (MeshHeight(d_I\ButtonModelID[BUTTON_DEFAULT_MODEL]) * 0.015), ButtonPosZ)
			ProjY = ProjectedY()
			CameraProject(Camera, ButtonPosX, ButtonPosY - (MeshHeight(d_I\ButtonModelID[BUTTON_DEFAULT_MODEL]) * 0.015), ButtonPosZ)
			Scale = (ProjectedY() - ProjY) / (462.0 * MenuScale)
			
			x = mo\Viewport_Center_X - ImageWidth(t\ImageID[Img_KeyPad_HUD]) * (Scale / 2)
			y = mo\Viewport_Center_Y - ImageHeight(t\ImageID[Img_KeyPad_HUD]) * (Scale / 2)
			
			If d_I\SelectedDoor\DoorType <> ELEVATOR_DOOR Then
				
				SetFontEx(fo\FontID[Font_Digital])
				Color(255, 255, 255)
				If msg\KeyPadMsg <> ""
					If (msg\KeyPadTimer Mod 70.0) < 35.0 Then TextEx(mo\Viewport_Center_X, y + (124 * MenuScale * Scale), msg\KeyPadMsg, True, True)
				Else
					TextEx(mo\Viewport_Center_X, y + (70 * MenuScale * Scale), GetLocalString("msg", "accesscode"), True, True)
					SetFontEx(fo\FontID[Font_Digital_Big])
					TextEx(mo\Viewport_Center_X, y + (124 * MenuScale * Scale), msg\KeyPadInput, True, True)
				EndIf
				
				x = x + (44 * MenuScale * Scale)
				y = y + (249 * MenuScale * Scale)
				
				For n = 0 To 3
					For i = 0 To 2
						xTemp = x + ((58.5 * MenuScale * Scale) * n)
						yTemp = y + ((67 * MenuScale * Scale) * i)
						
						If MouseOn(xTemp, yTemp, 54 * MenuScale * Scale, 65 * MenuScale * Scale)
							Color(255, 0, 0)
							Rect(xTemp, yTemp, 54 * MenuScale * Scale, 65 * MenuScale * Scale, False)
						EndIf
						
					Next
				Next
				
			Else ; ~ Elevator pannel buttons
				
				Color 255,0,0
				
				x = x + 121*Scale;120*Scale
				
				y = y + 259*Scale
				
				If RectsOverlap(x+1,y+10,82*Scale,82*Scale,MousePosX,MousePosY,0,0)
					Oval x-5,y+5,98*Scale,98*Scale,False
					;Rect x+1,y+10,82*Scale,82*Scale,False
				EndIf
				
				y = y + 131*Scale
				
				If RectsOverlap(x+1,y+7.5,82*Scale,82*Scale,MousePosX,MousePosY,0,0)
					Oval x-5,y,98*Scale,98*Scale,False
					;Rect x+1,y+7.5,82*Scale,82*Scale,False
				EndIf
				
				y = y + 130*Scale
				
				If RectsOverlap(x,y,82*Scale,82*Scale,MousePosX,MousePosY,0,0)
					Oval x-5,y-10,98*Scale,98*Scale,False
					;Rect x,y,82*Scale,82*Scale,False
				EndIf
			EndIf
			RenderCursor()
		EndIf
	EndIf
	
	Local InvImgSize% = (64 * MenuScale) / 2
	
	RenderInventory()
	
	If (Not InvOpen)
		If SelectedItem <> Null
			Select(SelectedItem\ItemTemplate\TempName)
				Case "key0", "key1", "key2", "key3", "key4", "key5", "key6", "keyomni", "scp860", "hand", "hand2", "hand3", "25ct", "scp005", "key", "coin", "mastercard"
					;[Block]
					DrawBlock(SelectedItem\ItemTemplate\InvImg, mo\Viewport_Center_X - InvImgSize, mo\Viewport_Center_Y - InvImgSize)
					;[End Block]
				Case "firstaid", "finefirstaid", "firstaid2"
					;[Block]
					If (me\Bloodloss <> 0.0 Lor me\Health < 100) And wi\HazmatSuit = 0
						DrawBlock(SelectedItem\ItemTemplate\InvImg, mo\Viewport_Center_X - InvImgSize, mo\Viewport_Center_Y - InvImgSize)
						
						Width = 300 * MenuScale
						Height = 20 * MenuScale
						x = mo\Viewport_Center_X - (Width / 2)
						y = mo\Viewport_Center_Y + (80 * MenuScale)
						
						RenderBar(x, y, Width, Height, SelectedItem\State, 100.0, 200, 200, 200)
					EndIf
					;[End Block]
				Case "paper", "oldpaper"
					;[Block]
					If SelectedItem\ItemTemplate\Img = 0
						Select(SelectedItem\ItemTemplate\Name)
							Case "Burnt Note" 
								;[Block]
								SelectedItem\ItemTemplate\Img = LoadImage_Strict(SelectedItem\ItemTemplate\ImgPath)
								SelectedItem\ItemTemplate\Img = ScaleImage2(SelectedItem\ItemTemplate\Img, MenuScale, MenuScale)
								SetBuffer(ImageBuffer(SelectedItem\ItemTemplate\Img))
								Color(0, 0, 0)
								SetFontEx(fo\FontID[Font_Default])
								TextEx(277 * MenuScale, 469 * MenuScale, CODE_DR_MAYNARD, True, True)
								SetBuffer(BackBuffer())
								;[End Block]
							Case "Unknown Note"
								;[Block]
								SelectedItem\ItemTemplate\Img = LoadImage_Strict(SelectedItem\ItemTemplate\ImgPath)
								SelectedItem\ItemTemplate\Img = ScaleImage2(SelectedItem\ItemTemplate\Img, MenuScale, MenuScale)
								
								SetBuffer(ImageBuffer(SelectedItem\ItemTemplate\Img))
								Color(50, 50, 50)
								SetFontEx(fo\FontID[Font_Journal])
								TextEx(300 * MenuScale, 295 * MenuScale, CODE_O5_COUNCIL, True, True)
								SetBuffer(BackBuffer())
								;[End Block]
							Case "Document SCP-372"
								;[Block]
								SelectedItem\ItemTemplate\Img = LoadImage_Strict(SelectedItem\ItemTemplate\ImgPath)
								SelectedItem\ItemTemplate\Img = ScaleImage2(SelectedItem\ItemTemplate\Img, MenuScale, MenuScale)
								
								SetBuffer(ImageBuffer(SelectedItem\ItemTemplate\Img))
								Color(37, 45, 137)
								SetFontEx(fo\FontID[Font_Journal])
								TextEx(383 * MenuScale, 734 * MenuScale, CODE_MAINTENANCE_TUNNELS, True, True)
								SetBuffer(BackBuffer())
								;[End Block]
							Default 
								;[Block]
								SelectedItem\ItemTemplate\Img = LoadImage_Strict(SelectedItem\ItemTemplate\ImgPath)
								SelectedItem\ItemTemplate\Img = ScaleImage2(SelectedItem\ItemTemplate\Img, MenuScale, MenuScale)
								;[End Block]
						End Select
						SelectedItem\ItemTemplate\ImgWidth = ImageWidth(SelectedItem\ItemTemplate\Img) / 2
						SelectedItem\ItemTemplate\ImgHeight = ImageHeight(SelectedItem\ItemTemplate\Img) / 2
					EndIf
					DrawBlock(SelectedItem\ItemTemplate\Img, mo\Viewport_Center_X - SelectedItem\ItemTemplate\ImgWidth, mo\Viewport_Center_Y - SelectedItem\ItemTemplate\ImgHeight)
					;[End Block]
				Case "scp1025"
					;[Block]
					If SelectedItem\ItemTemplate\Img = 0
						SelectedItem\ItemTemplate\Img = LoadImage_Strict(ItemHUDTexturePath + "page_1025(" + (Int(SelectedItem\State) + 1) + ").png")
						SelectedItem\ItemTemplate\Img = ScaleImage2(SelectedItem\ItemTemplate\Img, MenuScale, MenuScale)
						SelectedItem\ItemTemplate\ImgWidth = ImageWidth(SelectedItem\ItemTemplate\Img) / 2
						SelectedItem\ItemTemplate\ImgHeight = ImageHeight(SelectedItem\ItemTemplate\Img) / 2
					EndIf
					DrawBlock(SelectedItem\ItemTemplate\Img, mo\Viewport_Center_X - SelectedItem\ItemTemplate\ImgWidth, mo\Viewport_Center_Y - SelectedItem\ItemTemplate\ImgHeight)
					;[End Block]
				Case "radio", "18vradio", "fineradio", "veryfineradio"
					;[Block]
					; ~ RadioState[5] = Has the "use the number keys" -message been shown yet (True / False)
					; ~ RadioState[6] = A timer for the "code channel"
					; ~ RadioState[7] = Another timer for the "code channel"
					
					If SelectedItem\ItemTemplate\Img = 0
						SelectedItem\ItemTemplate\Img = LoadImage_Strict(SelectedItem\ItemTemplate\ImgPath)
						SelectedItem\ItemTemplate\Img = ScaleImage2(SelectedItem\ItemTemplate\Img, MenuScale, MenuScale)
						SelectedItem\ItemTemplate\ImgWidth = ImageWidth(SelectedItem\ItemTemplate\Img)
						SelectedItem\ItemTemplate\ImgHeight = ImageHeight(SelectedItem\ItemTemplate\Img)
						MaskImage(SelectedItem\ItemTemplate\Img, 255, 0, 255)
					EndIf
					
					StrTemp = ""
					
					x = opt\GraphicWidth - SelectedItem\ItemTemplate\ImgWidth
					y = opt\GraphicHeight - SelectedItem\ItemTemplate\ImgHeight
					
					DrawImage(SelectedItem\ItemTemplate\Img, x, y)
					
					If SelectedItem\State > 0.0 Lor (SelectedItem\ItemTemplate\TempName = "fineradio" Lor SelectedItem\ItemTemplate\TempName = "veryfineradio")
						If RID <> r_dimension_106 And CoffinDistance >= 8.0
							Select(Int(SelectedItem\State2))
								Case 0
									;[Block]
									If opt\UserTrackMode = 0
										StrTemp = Format(GetLocalString("radio", "usertrack"), GetLocalString("radio", "notenable"))
									ElseIf UserTrackMusicAmount < 1
										StrTemp = Format(GetLocalString("radio", "usertrack"), GetLocalString("radio", "nofound"))
									Else
										If ChannelPlaying(RadioCHN[0]) Then StrTemp = Format(GetLocalString("radio", "usertrack"), Upper(UserTrackName[RadioState[0]]))
									EndIf
									;[End Block]
								Case 1
									;[Block]
									StrTemp = GetLocalString("radio", "warn")
									;[End Block]
								Case 2
									;[Block]
									StrTemp = GetLocalString("radio", "onsite")
									;[End Block]
								Case 3
									;[Block]
									StrTemp = GetLocalString("radio", "emergency")
									;[End Block]
							End Select
							
							x = x + (66 * MenuScale)
							y = y + (419 * MenuScale)
							
							; ~ Battery
							Color(30, 30, 30)
							If SelectedItem\ItemTemplate\TempName = "radio" Lor SelectedItem\ItemTemplate\TempName = "18vradio"
								For i = 0 To 4
									Rect(x, y + ((8 * i) * MenuScale), (43 * MenuScale) - ((i * 6) * MenuScale), 4 * MenuScale, Ceil(SelectedItem\State / 20.0) > 4 - i )
								Next
							EndIf
							
							SetFontEx(fo\FontID[Font_Digital])
							TextEx(x + (60 * MenuScale), y, GetLocalString("radio", "chn"))
							
							If SelectedItem\ItemTemplate\TempName = "veryfineradio"
								StrTemp = ""
								For i = 0 To Rand(5, 30)
									StrTemp = StrTemp + Chr(Rand(100))
								Next
								
								SetFontEx(fo\FontID[Font_Digital_Big])
								TextEx(x + (97 * MenuScale), y + (16 * MenuScale), Rand(0, 9), True, True)
							Else
								SetFontEx(fo\FontID[Font_Digital_Big])
								TextEx(x + (97 * MenuScale), y + (16 * MenuScale), Int(SelectedItem\State2 + 1.0), True, True)
							EndIf
							
							SetFontEx(fo\FontID[Font_Digital])
							If StrTemp <> ""
								StrTemp = Right(Left(StrTemp, (Int(MilliSec / 300) Mod Len(StrTemp))), 10)
								TextEx(x - (28 * MenuScale), y + (33 * MenuScale), "          " + StrTemp + "          ")
							EndIf
							SetFontEx(fo\FontID[Font_Default])
						EndIf
					EndIf
					;[End Block]
				Case "nav", "nav300", "nav310", "navulti"
					;[Block]
					If SelectedItem\ItemTemplate\Img = 0
						SelectedItem\ItemTemplate\Img = LoadImage_Strict(SelectedItem\ItemTemplate\ImgPath)
						SelectedItem\ItemTemplate\Img = ScaleImage2(SelectedItem\ItemTemplate\Img, MenuScale, MenuScale)
						SelectedItem\ItemTemplate\ImgWidth = ImageWidth(SelectedItem\ItemTemplate\Img) / 2
						SelectedItem\ItemTemplate\ImgHeight = ImageHeight(SelectedItem\ItemTemplate\Img) / 2
						MaskImage(SelectedItem\ItemTemplate\Img, 255, 0, 255)
					EndIf
					
					Local NavType%
					
					Select(SelectedItem\ItemTemplate\TempName)
						Case "nav300"
							;[Block]
							NavType = 300
							;[End Block]
						Case "nav310"
							;[Block]
							NavType = 310
							;[End Block]
						Case "navulti"
							;[Block]
							NavType = 999
							;[End Block]
						Default
							;[Block]
							NavType = 1
							;[End Block]
					End Select
					
					x = opt\GraphicWidth - SelectedItem\ItemTemplate\ImgWidth + (20 * MenuScale)
					y = opt\GraphicHeight - SelectedItem\ItemTemplate\ImgHeight - (85 * MenuScale)
					
					DrawImage(SelectedItem\ItemTemplate\Img, x - SelectedItem\ItemTemplate\ImgWidth, y - SelectedItem\ItemTemplate\ImgHeight + (85 * MenuScale))
					
					SetFontEx(fo\FontID[Font_Digital])
					
					Local Offline% = (NavType = 300 Lor NavType = 1)
					Local NAV_WIDTH% = 287 * MenuScale
					Local NAV_HEIGHT% = 256 * MenuScale
					Local RectSize% = 24 * MenuScale
					Local RectSizeHalf% = RectSize / 2
					
					If (Not PlayerInReachableRoom(True)) Lor InFacility <> NullFloor
						If (MilliSec Mod 800) < 200
							Color(200, 0, 0)
							TextEx(x, y + (NAV_HEIGHT / 2) - (80 * MenuScale), GetLocalString("msg", "nav.error"), True)
							TextEx(x, y + (NAV_HEIGHT / 2) - (60 * MenuScale), GetLocalString("msg", "nav.locunknown"), True)
						EndIf
						Return
					Else
						If (SelectedItem\State > 0.0 Lor NavType = 300 Lor NavType = 999) And (Rnd(CoffinDistance + 15.0) > 1.0 Lor RID <> r_cont1_895)
							Local ColliderX# = EntityX(me\Collider)
							Local ColliderZ# = EntityZ(me\Collider)
							Local PlayerX% = Floor(ColliderX / RoomSpacing + 0.5)
							Local PlayerZ% = Floor(ColliderZ / RoomSpacing + 0.5)
							
							SetBuffer(ImageBuffer(t\ImageID[Img_NAV_Background]))
							
							Local xx% = x - SelectedItem\ItemTemplate\ImgWidth
							Local yy% = y - SelectedItem\ItemTemplate\ImgHeight + (85 * MenuScale)
							
							DrawImage(SelectedItem\ItemTemplate\Img, xx, yy)
							
							x = x - (12 * MenuScale) + ((ColliderX - 4.0) Mod RoomSpacing) * (3 * MenuScale)
							y = y + (12 * MenuScale) - ((ColliderZ - 4.0) Mod RoomSpacing) * (3 * MenuScale)
							For x2 = Max(1.0, PlayerX - 6) To Min(MapGridSize - 1, PlayerX + 6)
								For z2 = Max(1.0, PlayerZ - 6) To Min(MapGridSize - 1, PlayerZ + 6)
									If CoffinDistance > 16.0 Lor Rnd(16.0) < CoffinDistance
										If CurrMapGrid\Grid[x2 + (z2 * MapGridSize)] > MapGrid_NoTile And (CurrMapGrid\Found[x2 + (z2 * MapGridSize)] > MapGrid_NoTile Lor (Not Offline))
											Local DrawX% = x + (PlayerX - x2) * RectSize, DrawY% = y - (PlayerZ - z2) * RectSize
											
											Color(30, 30, 30)
											If CurrMapGrid\Grid[(x2 + 1) + (z2 * MapGridSize)] = MapGrid_NoTile Then Rect(DrawX - RectSizeHalf, DrawY - RectSizeHalf, 1, RectSize)
											If CurrMapGrid\Grid[(x2 - 1) + (z2 * MapGridSize)] = MapGrid_NoTile Then Rect(DrawX + RectSizeHalf, DrawY - RectSizeHalf, 1, RectSize)
											
											If CurrMapGrid\Grid[x2 + ((z2 - 1) * MapGridSize)] = MapGrid_NoTile Then Rect(DrawX - RectSizeHalf, DrawY - RectSizeHalf, RectSize, 1)
											If CurrMapGrid\Grid[x2 + ((z2 + 1) * MapGridSize)] = MapGrid_NoTile Then Rect(DrawX - RectSizeHalf, DrawY + RectSizeHalf, RectSize, 1)
										EndIf
									EndIf
								Next
							Next
							
							SetBuffer(BackBuffer())
							DrawBlockRect(t\ImageID[Img_NAV_Background], xx + (80 * MenuScale), yy + (70 * MenuScale), xx + (80 * MenuScale), yy + (70 * MenuScale), 270 * MenuScale, 230 * MenuScale)
							If Offline
								Color(100, 0, 0)
							Else
								Color(30, 30, 30)
							EndIf
							Rect(xx + (80 * MenuScale), yy + (70 * MenuScale), 270 * MenuScale, 230 * MenuScale, False)
							
							x = opt\GraphicWidth - SelectedItem\ItemTemplate\ImgWidth + (20 * MenuScale)
							y = opt\GraphicHeight - SelectedItem\ItemTemplate\ImgHeight - (85 * MenuScale)
							
							If Offline
								Color(100, 0, 0)
							Else
								Color(30, 30, 30)
							EndIf
							If (MilliSec Mod 800) < 200 ; ~ TODO: FIND THE WAY TO GET RID OF MILLISECS
								If Offline Then TextEx(x - (NAV_WIDTH / 2) + (10 * MenuScale), y - (NAV_HEIGHT / 2) + (10 * MenuScale), GetLocalString("msg", "nav.data"))
								
								YawValue = EntityYaw(me\Collider) - 90.0
								x1 = x + Cos(YawValue) * (6.0 * MenuScale) : y1 = y - Sin(YawValue) * (6.0 * MenuScale)
								x2 = x + Cos(YawValue - 140.0) * (5.0 * MenuScale) : y2 = y - Sin(YawValue - 140.0) * (5.0 * MenuScale)
								x3 = x + Cos(YawValue + 140.0) * (5.0 * MenuScale) : y3 = y - Sin(YawValue + 140.0) * (5.0 * MenuScale)
								
								Line(x1, y1, x2, y2)
								Line(x1, y1, x3, y3)
								Line(x2, y2, x3, y3)
							EndIf
							
							Local SCPs_Found% = 0, Dist#
							
							If NavType = 999 And (MilliSec Mod 600) < 400
								Local np.NPCs
								
								For np.NPCs = Each NPCs
									If np\NPCType = NPCType173 Lor np\NPCType = NPCType106 Lor np\NPCType = NPCType096 Lor np\NPCType = NPCType049 Lor np\NPCType = NPCType066
										If (Not np\HideFromNVG)
											Dist = EntityDistanceSquared(Camera, np\Collider)
											If Dist < 900.0
												SqrValue = Sqr(Dist)
												Color(100, 0, 0)
												Oval(x - (SqrValue * (1.5 * MenuScale)), y - (SqrValue * (1.5 * MenuScale)), SqrValue * (3 * MenuScale), SqrValue * (3 * MenuScale), False)
												TextEx(x - (NAV_WIDTH / 2) + (10 * MenuScale), y - (NAV_HEIGHT / 2) + (30 * MenuScale) + ((20 * SCPs_Found) * MenuScale), np\NVGName)
												SCPs_Found = SCPs_Found + 1
											EndIf
										EndIf
									EndIf
								Next
								If RID = r_cont1_895
									If CoffinDistance < 8.0
										Dist = Rnd(4.0, 8.0)
										Color(100, 0, 0)
										Oval(x - (Dist * (1.5 * MenuScale)), y - (Dist * (1.5 * MenuScale)), Dist * (3 * MenuScale), Dist * (3 * MenuScale), False)
										TextEx(x - (NAV_WIDTH / 2) + (10 * MenuScale), y - (NAV_HEIGHT / 2) + (30 * MenuScale) + ((20 * SCPs_Found) * MenuScale), "SCP-895")
									EndIf
								EndIf
							EndIf
							
							Color(30, 30, 30)
							If SelectedItem\State > 0.0 And (NavType = 1 Lor NavType = 310)
								xTemp = x - (NAV_WIDTH / 2) + (196 * MenuScale)
								yTemp = y - (NAV_HEIGHT / 2) + (10 * MenuScale)
								
								If Offline Then Color(100, 0, 0)
								Rect(xTemp, yTemp, 80 * MenuScale, 20 * MenuScale, False)
								
								; ~ Battery
								If SelectedItem\State <= 20.0
									Color(100, 0, 0)
								Else
									Color(30, 30, 30)
								EndIf
								For i = 1 To Min(Ceil(SelectedItem\State / 10.0), 10.0)
									Rect(xTemp + ((i * 8) * MenuScale) - (6 * MenuScale), yTemp + (4 * MenuScale), 4 * MenuScale, 12 * MenuScale)
								Next
								SetFontEx(fo\FontID[Font_Digital])
							EndIf
						EndIf
					EndIf
					;[End Block]
				Case "badge", "burntbadge"
					;[Block]
					If SelectedItem\ItemTemplate\Img = 0
						SelectedItem\ItemTemplate\Img = LoadImage_Strict(SelectedItem\ItemTemplate\ImgPath)
						SelectedItem\ItemTemplate\Img = ScaleImage2(SelectedItem\ItemTemplate\Img, MenuScale, MenuScale)
						SelectedItem\ItemTemplate\ImgWidth = ImageWidth(SelectedItem\ItemTemplate\Img) / 2
						SelectedItem\ItemTemplate\ImgHeight = ImageHeight(SelectedItem\ItemTemplate\Img) / 2
					EndIf
					DrawBlock(SelectedItem\ItemTemplate\Img, mo\Viewport_Center_X - SelectedItem\ItemTemplate\ImgWidth, mo\Viewport_Center_Y - SelectedItem\ItemTemplate\ImgHeight)
					;[End Block]
				Case "oldbadge", "ticket"
					;[Block]
					If SelectedItem\ItemTemplate\Img = 0
						SelectedItem\ItemTemplate\Img = LoadImage_Strict(SelectedItem\ItemTemplate\ImgPath)
						SelectedItem\ItemTemplate\Img = ScaleImage2(SelectedItem\ItemTemplate\Img, MenuScale, MenuScale)
						SelectedItem\ItemTemplate\ImgWidth = ImageWidth(SelectedItem\ItemTemplate\Img) / 2
						SelectedItem\ItemTemplate\ImgHeight = ImageHeight(SelectedItem\ItemTemplate\Img) / 2
						MaskImage(SelectedItem\ItemTemplate\Img, 255, 0, 255)
					EndIf
					DrawImage(SelectedItem\ItemTemplate\Img, mo\Viewport_Center_X - SelectedItem\ItemTemplate\ImgWidth, mo\Viewport_Center_Y - SelectedItem\ItemTemplate\ImgHeight)
					;[End Block]
			End Select
		EndIf
	EndIf
	
	CatchErrors("Uncaught: RenderGUI()")
End Function

Function UpdateGUI%()
	CatchErrors("UpdateGUI()")
	
	Local e.Events, it.Items, r.Rooms
	Local Temp%, x%, y%, z%, i%
	Local x2#, ProjY#, Scale#, Pvt%
	Local n%, xTemp%, yTemp%, StrTemp$, np.NPCs, elev.Elevator
	Local RID% = PlayerRoom\RoomTemplate\RoomID
	
	If (Not MenuOpen) And (d_I\SelectedDoor = Null) And (Not ConsoleOpen) And (Not SandBoxMenuOpen) And (Not I_294\Using) And (SelectedScreen = Null) And cra\EndingTimer = 0.0 And me\KillAnimTimer >= 0 Then ToggleWeapons()
	
	wep_I\SlotTimer = Max(wep_I\SlotTimer - fps\Factor[0], 0.0)
	wep_I\HUDTimer = Max(wep_I\HUDTimer - fps\Factor[0], 0.0)
	
	UpdateTasks()
	
	If RID = r_dimension_106
		For e.Events = Each Events
			If e\room = PlayerRoom
				If (wi\NightVision > 0 Lor wi\SCRAMBLE > 0) And e\EventState2 <> PD_FakeTunnelRoom
					If e\Img2 <> 0
						StopChannel(e\SoundCHN)
						FreeImage(e\Img2) : e\Img2 = 0
					EndIf
					
					If e\Img = 0
						StopChannel(e\SoundCHN) : e\SoundCHN = 0
						Select(Rand(5))
							Case 1
								;[Block]
								PlaySound_Strict(HorrorSFX[1])
								;[End Block]
							Case 2
								;[Block]
								PlaySound_Strict(HorrorSFX[2])
								;[End Block]
							Case 3
								;[Block]
								PlaySound_Strict(HorrorSFX[9])
								;[End Block]
							Case 4
								;[Block]
								PlaySound_Strict(HorrorSFX[10])
								;[End Block]
							Case 5
								;[Block]
								PlaySound_Strict(HorrorSFX[12])
								;[End Block]
						End Select
						e\Img = LoadImage_Strict("GFX\Overlays\scp_106_face_overlay.png")
						e\Img = ScaleImage2(e\Img, MenuScale, MenuScale)
					Else
						wi\IsNVGBlinking = True
						If Rand(30) = 1
							If (Not ChannelPlaying(e\SoundCHN)) Then e\SoundCHN = PlaySound_Strict(DripSFX[Rand(0, 3)])
						EndIf
					EndIf
				Else
					If e\Img <> 0
						StopChannel(e\SoundCHN)
						FreeImage(e\Img) : e\Img = 0
					EndIf
					
					If e\EventState2 = PD_ThroneRoom
						If me\BlinkTimer > -16.0 And me\BlinkTimer < -6.0
							If e\Img2 = 0
								StopChannel(e\SoundCHN) : e\SoundCHN = 0
								PlaySound_Strict(e\Sound2, True)
								e\Img2 = LoadImage_Strict("GFX\Overlays\kneel_mortal_overlay.png")
								e\Img2 = ScaleImage2(e\Img2, MenuScale, MenuScale)
							Else
								If (Not ChannelPlaying(e\SoundCHN))
									e\SoundCHN = PlaySound_Strict(e\Sound)
									ChannelVolume(e\SoundCHN, opt\VoiceVolume * opt\MasterVolume)
								EndIf
							EndIf
						Else
							If ChannelPlaying(e\SoundCHN) Then StopChannel(e\SoundCHN) : e\SoundCHN = 0
						EndIf
					Else
						If e\Img2 <> 0
							FreeImage(e\Img2) : e\Img2 = 0
							StopChannel(e\SoundCHN) : e\SoundCHN = 0
						EndIf
					EndIf
				EndIf
				Exit
			EndIf
		Next
	EndIf
	
	If I_294\Using Then Update294()
	If (Not (MenuOpen Lor InvOpen Lor ConsoleOpen Lor SandBoxMenuOpen Lor I_294\Using Lor OtherOpen <> Null Lor d_I\SelectedDoor <> Null Lor SelectedScreen <> Null Lor me\Health = 0))
		If d_I\ClosestButton <> 0
			If HitKeyUse
				HitKeyUse = False
				If d_I\ClosestDoor <> Null
					If d_I\ClosestDoor\Code <> "" Lor (d_I\ClosestDoor\DoorType = ELEVATOR_DOOR And d_I\ClosestButton=d_I\ClosestDoor\Buttons[0])
						
						Temp = True
						
;						If gm\ID = GM_NTF Then
;							For elev = Each Elevator
;								If elev\door = d_I\ClosestDoor Then
;									For np = Each NPCs
;										If np\NPCType = NPCType173 And np\Idle = SCP173_BOXED Then
;											If Abs(EntityX(np\Collider) - EntityX(elev\obj, True)) >= 280.0 * RoomScale + (0.015 * fps\Factor[0]) Lor Abs(EntityZ(np\Collider) - EntityZ(elev\obj, True)) >= 280.0 * RoomScale + (0.015 * fps\Factor[0]) Then
;												Temp = 2
;											EndIf
;										EndIf
;										If np\NPCType = NPCTypeMTF And np\HP > 0 Then
;											If Abs(EntityX(np\Collider) - EntityX(elev\obj, True)) >= 280.0 * RoomScale + (0.015 * fps\Factor[0]) Lor Abs(EntityZ(np\Collider) - EntityZ(elev\obj, True)) >= 280.0 * RoomScale + (0.015 * fps\Factor[0]) Then
;												Temp = False
;												Exit
;											EndIf
;										EndIf
;									Next
;									Exit
;								EndIf
;							Next
;						EndIf
						
						If Temp = 1 Then
							d_I\SelectedDoor = d_I\ClosestDoor
						ElseIf Temp = 2 Then
							CreateMsg(GetLocalString("msg", "elev.wait_173"))
						Else
							CreateMsg(GetLocalString("msg", "elev.wait"))
						EndIf
						
					ElseIf d_I\ClosestDoor\DoorType = ELEVATOR_DOOR And d_I\ClosestButton = d_I\ClosestDoor\Buttons[1] Then
						PlaySound2(ButtonSFX[0], Camera, d_I\ClosestButton)
						For elev = Each Elevator
							If elev\Door = d_I\ClosestDoor
								If elev\State = 0.0
									If EntityY(elev\Door\FrameOBJ)>elev\FloorY[1]*RoomScale+1
										StartNewElevator(d_I\ClosestDoor, 3)
									ElseIf EntityY(elev\Door\FrameOBJ)<elev\FloorY[2]*RoomScale-1 And EntityY(elev\Door\FrameOBJ)>elev\FloorY[0]*RoomScale
										StartNewElevator(d_I\ClosestDoor, 2)
									Else
										StartNewElevator(d_I\ClosestDoor, 1)
									EndIf
								Else
									If (msg\Txt<>GetLocalString("msg", "elev.called"))
										If (msg\Txt=GetLocalString("msg", "elev.already")) Lor (msg\Timer < 70.0 * 3.0)	
											Select(Rand(10))
												Case 1
													CreateMsg(GetLocalString("msg", "elev.stop"))
												Case 2
													CreateMsg(GetLocalString("msg", "elev.faster"))
												Case 3
													CreateMsg(GetLocalString("msg", "elev.mav"))
												Default
													CreateMsg(GetLocalString("msg", "elev.already"))
											End Select
										EndIf
									Else
										CreateMsg(GetLocalString("msg", "elev.already"))
									EndIf
								EndIf
							EndIf
						Next
					ElseIf me\Playable
						;PlaySound2(ButtonSFX[0], Camera, d_I\ClosestButton)
						UseDoor()
					EndIf
				EndIf
;				If d_I\ClosestDoor <> Null
;					If d_I\ClosestDoor\Code <> ""
;						d_I\SelectedDoor = d_I\ClosestDoor
;					ElseIf me\Playable
;						UseDoor()
;					EndIf
;				EndIf
			EndIf
		EndIf
	EndIf
	
	If (Not (MenuOpen Lor me\Health = 0))
		If SelectedScreen <> Null
			If mo\MouseUp1 Lor mo\MouseHit2 Then
				FreeImage(SelectedScreen\Img) : SelectedScreen\Img = 0
				mo\MouseUp1 = False
				SelectedScreen = Null
			EndIf
		EndIf
	EndIf
	
	Local PrevInvOpen% = InvOpen, MouseSlot% = 66
	Local ShouldDrawHUD% = True
	
	If d_I\SelectedDoor <> Null
		If SelectedItem <> Null
			If SelectedItem\ItemTemplate\TempName = "scp005"
				UseDoor()
				ShouldDrawHUD = False
			Else
				SelectedItem = Null
			EndIf
		EndIf
		If ShouldDrawHUD
			
			HideEntity wep_I\Pivot
;			HideEntity me\ModelPivot
			
			Local zcamerazoom#
			
			If d_I\SelectedDoor\DoorType <> ELEVATOR_DOOR Then
				zcamerazoom = 0.22
			Else
				zcamerazoom = 0.26
			EndIf
			
			Local ButtonPosX# = EntityX(d_I\ClosestButton, True)
			Local ButtonPosY# = EntityY(d_I\ClosestButton, True)
			Local ButtonPosZ# = EntityZ(d_I\ClosestButton, True)
			
			CameraZoom(Camera, Min(1.0 + (me\CurrCameraZoom / 400.0), 1.1) / Tan((2.0 * ATan(Tan((opt\FOV) / 2.0) * opt\RealGraphicWidth / opt\RealGraphicHeight)) / 2.0))
			Pvt = CreatePivot()
			PositionEntity(Pvt, ButtonPosX, ButtonPosY, ButtonPosZ)
			RotateEntity(Pvt, 0.0, EntityYaw(d_I\ClosestButton, True) - 180.0, 0.0)
			MoveEntity(Pvt, 0.0, 0.0, zcamerazoom)
			PositionEntity(Camera, EntityX(Pvt), EntityY(Pvt), EntityZ(Pvt))
			PointEntity(Camera, d_I\ClosestButton)
			FreeEntity(Pvt) : Pvt = 0
			
			CameraProject(Camera, ButtonPosX, ButtonPosY + (MeshHeight(d_I\ButtonModelID[BUTTON_DEFAULT_MODEL]) * 0.015), ButtonPosZ)
			ProjY = ProjectedY()
			CameraProject(Camera, ButtonPosX, ButtonPosY - (MeshHeight(d_I\ButtonModelID[BUTTON_DEFAULT_MODEL]) * 0.015), ButtonPosZ)
			Scale = (ProjectedY() - ProjY) / (462.0 * MenuScale)
			
			x = mo\Viewport_Center_X - ImageWidth(t\ImageID[Img_KeyPad_HUD]) * (Scale / 2)
			y = mo\Viewport_Center_Y - ImageHeight(t\ImageID[Img_KeyPad_HUD]) * (Scale / 2)
			
			If d_I\SelectedDoor\DoorType <> ELEVATOR_DOOR Then
				
				If msg\KeyPadMsg <> ""
					msg\KeyPadTimer = msg\KeyPadTimer - fps\Factor[0]
					If msg\KeyPadTimer <= 0.0
						msg\KeyPadMsg = ""
						d_I\SelectedDoor = Null
						StopMouseMovement()
					EndIf
				EndIf
				
				x = x + (44 * MenuScale * Scale)
				y = y + (249 * MenuScale * Scale)
				
				For n = 0 To 3
					For i = 0 To 2
						xTemp = x + ((58.5 * MenuScale * Scale) * n)
						yTemp = y + ((67 * MenuScale * Scale) * i)
						
						Temp = False
						If MouseOn(xTemp, yTemp, 54 * MenuScale * Scale, 65 * MenuScale * Scale) And msg\KeyPadMsg = ""
							If mo\MouseUp1
								PlaySound_Strict(ButtonSFX[0])
								
								Select((n + 1) + (i * 4))
									Case 1, 2, 3
										;[Block]
										msg\KeyPadInput = msg\KeyPadInput + ((n + 1) + (i * 4))
										;[End Block]
									Case 4
										;[Block]
										msg\KeyPadInput = msg\KeyPadInput + "0"
										;[End Block]
									Case 5, 6, 7
										;[Block]
										msg\KeyPadInput = msg\KeyPadInput + ((n + 1) + (i * 4) - 1)
										;[End Block]
									Case 8
										;[Block]
										UseDoor()
										If msg\KeyPadInput = d_I\SelectedDoor\Code
											d_I\SelectedDoor = Null
											StopMouseMovement()
										Else
											msg\KeyPadMsg = GetLocalString("msg", "denied")
											msg\KeyPadTimer = 210.0
											msg\KeyPadInput = ""
										EndIf
										;[End Block]
									Case 9, 10, 11
										;[Block]
										msg\KeyPadInput = msg\KeyPadInput + ((n + 1) + (i * 4) - 2)
										;[End Block]
									Case 12
										;[Block]
										msg\KeyPadInput = ""
										;[End Block]
								End Select
								If Len(msg\KeyPadInput) > 4 Then msg\KeyPadInput = Left(msg\KeyPadInput, 4)
							EndIf
						Else
							Temp = False
						EndIf
					Next
				Next
				
			Else ; ~ Elevator pannel buttons
				
				MoveEntity Camera, 0.0, 0.047, 0.0
				
				Local buttonid%, KeyPressed[3]
				
				For buttonid = 0 To 2
					KeyPressed[buttonid] = KeyHit(buttonid + 2)
				Next
				
				x = x + 121*Scale
				y = y + 259*Scale
				
				If (RectsOverlap(x+1,y+10,82*Scale,82*Scale,MousePosX,MousePosY,0,0) And mo\MouseHit1) Lor KeyPressed[2]
					PlaySound_Strict ButtonSFX[0]
					StartNewElevator(d_I\SelectedDoor, 3)
					d_I\SelectedDoor = Null
					ResetInput()
				EndIf
				
				y = y + 131*Scale
				
				If (RectsOverlap(x+1,y+7.5,82*Scale,82*Scale,MousePosX,MousePosY,0,0) And mo\MouseHit1) Lor KeyPressed[1]
					PlaySound_Strict ButtonSFX[0]
					StartNewElevator(d_I\SelectedDoor, 2)
					d_I\SelectedDoor = Null
					ResetInput()
				EndIf
				
				y = y + 130*Scale
				
				If (RectsOverlap(x,y,82*Scale,82*Scale,MousePosX,MousePosY,0,0) And mo\MouseHit1) Lor KeyPressed[0]
					PlaySound_Strict ButtonSFX[0]
					StartNewElevator(d_I\SelectedDoor, 1)
					d_I\SelectedDoor = Null
					ResetInput()
				EndIf
				
			EndIf
			
			If mo\MouseHit2
				d_I\SelectedDoor = Null
				StopMouseMovement()
			EndIf
		Else
			d_I\SelectedDoor = Null
		EndIf
	Else
		msg\KeyPadInput = ""
		msg\KeyPadTimer = 0.0
		msg\KeyPadMsg = ""
	EndIf
	
	If KeyHit(1) And cra\EndingTimer = 0.0 And me\SelectedEnding = -1 And me\KillAnimTimer <= 400.0
		If MenuOpen
			ResumeSounds()
			If igm\OptionsMenu <> 0 Then SaveOptionsINI()
			StopMouseMovement()
			ShouldDeleteGadgets = True
		Else
			PauseSounds()
		EndIf
		MenuOpen = (Not MenuOpen)
		
		igm\AchievementsMenu = 0
		igm\OptionsMenu = 0
		igm\QuitMenu = 0
	EndIf
	
	UpdateInventory()
	
	If (Not InvOpen)
		If SelectedItem <> Null
			Select(SelectedItem\ItemTemplate\TempName)
				Case "scp513"
					;[Block]
					PlaySound_Strict(LoadTempSound("SFX\SCP\513\Bell.ogg"))
					
					GiveAchievement(Achv513)
					
					If n_I\Curr513_1 = Null And (Not me\Deaf) Then n_I\Curr513_1 = CreateNPC(NPCType513_1, 0.0, 0.0, 0.0)
					SelectedItem = Null
					;[End Block]
				Case "scp500pill"
					;[Block]
					If CanUseItem(True)
						GiveAchievement(Achv500)
						
						If I_008\Timer > 0.0
							CreateMsg(GetLocalString("msg", "pill.nausea"))
							I_008\Revert = True
						ElseIf I_409\Timer > 0.0
							CreateMsg(GetLocalString("msg", "pill.crystals"))
							I_409\Revert = True
						Else
							CreateMsg(GetLocalString("msg", "pill"))
						EndIf
						
						me\DeathTimer = 0.0
						me\Stamina = 100.0
						
						For i = 0 To 6
							I_1025\State[i] = 0.0
						Next
						
						If me\StaminaEffect > 1.0
							me\StaminaEffect = 1.0
							me\StaminaEffectTimer = 0.0
						EndIf
						
						If me\BlinkEffect > 1.0
							me\BlinkEffect = 1.0
							me\BlinkEffectTimer = 0.0
						EndIf
						
						For e.Events = Each Events
							If e\EventID = e_1048_a
								If e\EventState2 > 0.0
									CreateMsg(GetLocalString("msg", "pill.ears"))
									e\EventState3 = 1.0
								EndIf
							EndIf
						Next
						RemoveItem(SelectedItem)
					EndIf
					;[End Block]
				Case "veryfinefirstaid"
					;[Block]
					If CanUseItem(False, True)
						Select(Rand(5))
							Case 1
								;[Block]
								DamagePlayer(15)
								CreateMsg(GetLocalString("msg", "bleed"))
								;[End Block]
							Case 2
								;[Block]
								me\Health = 100
								me\Bloodloss = 0.0
								CreateMsg(GetLocalString("msg", "rapidly"))
								;[End Block]
							Case 3
								;[Block]
								HealPlayer(Rand(15, 20))
								me\Bloodloss = Max(0.0, me\Bloodloss - Rnd(10.0, 100.0))
								CreateMsg(GetLocalString("msg", "better_1"))
								;[End Block]
							Case 4
								;[Block]
								me\BlurTimer = 10000.0
								me\Bloodloss = 0.0
								CreateMsg(GetLocalString("msg", "nausea"))
								;[End Block]
							Case 5
								;[Block]
								me\BlinkTimer = -10.0
								
								If RID = r_dimension_1499 Lor IsPlayerOutsideFacility()
									DamagePlayer(Rand(15, 20))
									CreateMsg(GetLocalString("msg", "bleed"))
								Else
									For r.Rooms = Each Rooms
										If r\RoomTemplate\RoomID = r_dimension_106
											TeleportToRoom(r)
											TeleportEntity(me\Collider, EntityX(r\OBJ), EntityY(r\OBJ) + 0.5, EntityZ(r\OBJ))
											PlaySound_Strict(Use914SFX)
											me\DropSpeed = 0.0
											n_I\Curr106\State = -2500.0
											Exit
										EndIf
									Next
									CreateMsg(GetLocalString("msg", "aid.106"))
								EndIf
								;[End Block]
						End Select
						RemoveItem(SelectedItem)
					EndIf
					;[End Block]
				Case "firstaid", "finefirstaid", "firstaid2"
					;[Block]
					If CanUseItem(False, True)
						If me\Bloodloss = 0.0 And me\Health = 100
							CreateMsg(GetLocalString("msg", "aid.no"))
							SelectedItem = Null
							Return
						Else
							me\CurrSpeed = CurveValue(0.0, me\CurrSpeed, 5.0)
							If (Not me\Crouch) Then SetCrouch(True)
							
							If SelectedItem\ItemTemplate\TempName = "firstaid"
								SelectedItem\State = Min(SelectedItem\State + (fps\Factor[0] / 5.0), 100.0)
							Else
								SelectedItem\State = Min(SelectedItem\State + (fps\Factor[0] / 4.0), 100.0)
							EndIf
							
							If SelectedItem\State = 100.0
								If SelectedItem\ItemTemplate\TempName = "finefirstaid"
									me\Bloodloss = 0.0
									HealPlayer(Rand(15, 20))
									If me\Health = 100
										CreateMsg(GetLocalString("msg", "aid.fine"))
									ElseIf me\Health < 60
										CreateMsg(GetLocalString("msg", "aid.bleed"))
									Else
										CreateMsg(GetLocalString("msg", "aid.sore"))
									EndIf
								Else
									me\Bloodloss = Max(0.0, me\Bloodloss - Rnd(10.0, 20.0))
									If me\Health < 40
										CreateMsg(GetLocalString("msg", "aid.toobad_1"))
										HealPlayer(Rand(15, 20))
									ElseIf me\Health < 70
										HealPlayer(Rand(15, 20))
										If me\Health < 60
											CreateMsg(GetLocalString("msg", "aid.toobad_2"))
										Else
											CreateMsg(GetLocalString("msg", "aid.stop"))
										EndIf
									Else
										If me\Health < 80
											HealPlayer(Rand(15, 20))
											CreateMsg(GetLocalString("msg", "aid.slight"))
										Else
											HealPlayer(Rand(15, 20))
											CreateMsg(GetLocalString("msg", "aid.nowalk"))
										EndIf
									EndIf
									
									If SelectedItem\ItemTemplate\TempName = "firstaid2"
										Select(Rand(6))
											Case 1
												;[Block]
												chs\SuperMan = True
												CreateMsg(GetLocalString("msg", "aid.super"))
												;[End Block]
											Case 2
												;[Block]
												opt\InvertMouseX = (Not opt\InvertMouseX)
												opt\InvertMouseY = (Not opt\InvertMouseY)
												CreateMsg(GetLocalString("msg", "aid.invert"))
												;[End Block]
											Case 3
												;[Block]
												me\BlurTimer = 5000.0
												CreateMsg(GetLocalString("msg", "nausea"))
												;[End Block]
											Case 4
												;[Block]
												me\BlinkEffect = 0.6
												me\BlinkEffectTimer = Rnd(20.0, 30.0)
												;[End Block]
											Case 5
												;[Block]
												me\Bloodloss = 0.0
												me\Health = 100
												CreateMsg(GetLocalString("msg", "aid.stopall"))
												;[End Block]
											Case 6
												;[Block]
												CreateMsg(GetLocalString("msg", "aid.through"))
												DamagePlayer(Rand(12, 17))
												;[End Block]
										End Select
									EndIf
								EndIf
								RemoveItem(SelectedItem)
							EndIf
						EndIf
					EndIf
					;[End Block]
				Case "eyedrops", "eyedrops2"
					;[Block]
					If CanUseItem(True)
						me\BlinkEffect = 0.7
						me\BlinkEffectTimer = Rnd(30.0, 35.0)
						me\BlurTimer = 200.0
						
						CreateMsg(GetLocalString("msg", "eyedrop.moisturized"))
						
						RemoveItem(SelectedItem)
					EndIf
					;[End Block]
				Case "fineeyedrops"
					;[Block]
					If CanUseItem(True)
						me\BlinkEffect = 0.5
						me\BlinkEffectTimer = Rnd(40.0, 45.0)
						me\Bloodloss = Max(me\Bloodloss - 1.0, 0.0)
						me\BlurTimer = 200.0
						
						CreateMsg(GetLocalString("msg", "eyedrop.moisturized.very"))
						
						RemoveItem(SelectedItem)
					EndIf
					;[End Block]
				Case "veryfineeyedrops"
					;[Block]
					If CanUseItem(True)
						me\BlinkEffect = 0.0
						me\BlinkEffectTimer = 60.0
						me\EyeStuck = 10000.0
						me\BlurTimer = 1000.0
						
						CreateMsg(GetLocalString("msg", "eyedrop.moisturized.veryvery"))
						
						RemoveItem(SelectedItem)
					EndIf
					;[End Block]
				Case "scp1025"
					;[Block]
					GiveAchievement(Achv1025)
					If SelectedItem\ItemTemplate\Img = 0
						SelectedItem\ItemTemplate\Img = LoadImage_Strict(ItemHUDTexturePath + "page_1025(" + (Int(SelectedItem\State) + 1) + ").png")
						SelectedItem\ItemTemplate\Img = ScaleImage2(SelectedItem\ItemTemplate\Img, MenuScale, MenuScale)
						SelectedItem\ItemTemplate\ImgWidth = ImageWidth(SelectedItem\ItemTemplate\Img) / 2
						SelectedItem\ItemTemplate\ImgHeight = ImageHeight(SelectedItem\ItemTemplate\Img) / 2
						MaskImage(SelectedItem\ItemTemplate\Img, 255, 0, 255)
					EndIf
					
					If SelectedItem\State3 = 0.0
						If I_714\Using = 0 And wi\GasMask <> 4 And wi\HazmatSuit <> 4
							If SelectedItem\State = 7.0
								If I_008\Timer = 0.0 Then I_008\Timer = 0.001
							Else
								I_1025\State[SelectedItem\State] = Max(1.0, I_1025\State[SelectedItem\State])
								I_1025\State[7] = 1 + (SelectedItem\State2 = 2.0) * 2.0 ; ~ 3x as fast if VERYFINE
							EndIf
						EndIf
						If Rand(3 - (SelectedItem\State2 <> 2.0) * SelectedItem\State2) = 1 ; ~ Higher chance for good illness if FINE, lower change for good illness if COARSE
							SelectedItem\State = 6.0
						Else
							SelectedItem\State = Rand(0, 7)
						EndIf
						SelectedItem\State3 = 1.0
					EndIf
					;[End Block]
				Case "book"
					;[Block]
					CreateMsg(GetLocalString("msg", "redbook"))
					SelectedItem = Null
					;[End Block]
				Case "cup"
					;[Block]
					If CanUseItem(True)
						Local Drink% = Int(SelectedItem\Name)
						
						If JsonIsNull(JsonGetValue(Drink, "refuse_message"))
							If (Not JsonIsNull(JsonGetValue(Drink, "drink_message"))) Then CreateMsg(JsonGetString(JsonGetValue(Drink, "drink_message")))
							
							me\BlurTimer = Max(JsonGetFloat(JsonGetValue(Drink, "blur")) * 70.0, 0.0)
							If me\VomitTimer = 0.0
								me\VomitTimer = JsonGetFloat(JsonGetValue(Drink, "vomit"))
							Else
								me\VomitTimer = Min(me\VomitTimer, JsonGetFloat(JsonGetValue(Drink, "vomit")))
							EndIf
							me\CameraShakeTimer = JsonGetFloat(JsonGetValue(Drink, "camera_shake"))
							me\DeafTimer = Max(me\DeafTimer + JsonGetFloat(JsonGetValue(Drink, "deaf_timer")), 0.0)
							DamagePlayer(JsonGetInt(JsonGetValue(Drink, "damage")), 0.0)
							me\Bloodloss = Max(me\Bloodloss + JsonGetFloat(JsonGetValue(Drink, "bloodloss")), 0.0)
							
							If (Not JsonIsNull(JsonGetValue(Drink, "drink_sound"))) Then PlaySound_Strict(LoadTempSound(JsonGetString(JsonGetValue(Drink, "drink_sound"))), True)
							
							If JsonGetBool(JsonGetValue(Drink, "stomachache")) Then I_1025\State[3] = 1.0
							
							If JsonGetBool(JsonGetValue(Drink, "infection")) Then I_008\Timer = I_008\Timer + 0.001
							
							If JsonGetBool(JsonGetValue(Drink, "crystallization")) Then I_409\Timer = I_409\Timer + 0.001
							
							If JsonGetBool(JsonGetValue(Drink, "mutation"))
								If I_427\Timer < 70.0 * 360.0 Then I_427\Timer = 70.0 * 360.0
							EndIf
							
							If JsonGetBool(JsonGetValue(Drink, "revitalize"))
								For i = 0 To 6
									I_1025\State[i] = 0.0
								Next
							EndIf
							
							If (Not JsonIsNull(JsonGetValue(Drink, "death_timer")))
								If (Not JsonIsNull(JsonGetValue(Drink, "death_message")))
									msg\DeathMsg = JsonGetString(JsonGetValue(Drink, "death_message"))
								Else
									msg\DeathMsg = ""
								EndIf
								
								Local DeathTimer1% = JsonGetFloat(JsonGetValue(Drink, "death_timer"))
								
								If DeathTimer1 = 0 
									Kill()
								ElseIf me\DeathTimer = 0.0
									me\DeathTimer = DeathTimer1 * 70.0
								Else
									me\DeathTimer = Min(me\DeathTimer, DeathTimer1)
								EndIf
							EndIf
							
							; ~ The state of refined items is more than 1.0 (fine setting increases it by 1, very fine doubles it)
							If (Not JsonIsNull(JsonGetValue(Drink, "blink_effect"))) Then me\BlinkEffect = JsonGetFloat(JsonGetValue(Drink, "blink_effect")) ^ SelectedItem\State
							If (Not JsonIsNull(JsonGetValue(Drink, "blink_timer"))) Then me\BlinkEffectTimer = JsonGetFloat(JsonGetValue(Drink, "blink_timer")) * SelectedItem\State
							If (Not JsonIsNull(JsonGetValue(Drink, "stamina_effect"))) Then me\BlinkEffectTimer = JsonGetFloat(JsonGetValue(Drink, "stamina_effect")) ^ SelectedItem\State
							If (Not JsonIsNull(JsonGetValue(Drink, "stamina_timer"))) Then me\BlinkEffectTimer = JsonGetFloat(JsonGetValue(Drink, "stamina_timer")) * SelectedItem\State
							
							it.Items = CreateItem("Empty Cup", "emptycup", 0.0, 0.0, 0.0)
							it\Picked = True
							For i = 0 To MaxItemAmount - 1
								If Inventory(i) = SelectedItem
									Inventory(i) = it
									Exit
								EndIf
							Next
							EntityType(it\Collider, HIT_ITEM)
							
							Local itm%
							
							CarriableWeight = it\ItemTemplate\Weight
							For itm = 0 To MaxItemAmount - 1
								If Inventory(itm) <> Null
									CarriableWeight = CarriableWeight + Inventory(itm)\ItemTemplate\Weight
								EndIf
							Next
							
							RemoveItem(SelectedItem)
						Else
							CreateMsg(JsonGetString(JsonGetValue(Drink, "refuse_message")))
							SelectedItem = Null
						EndIf
					EndIf
					;[End Block]
				Case "syringe"
					;[Block]
					If CanUseItem(False, True)
						me\HealTimer = 30.0
						me\StaminaEffect = 0.5
						me\StaminaEffectTimer = 20.0
						
						CreateMsg(GetLocalString("msg", "syringe_1"))
						
						RemoveItem(SelectedItem)
					EndIf
					;[End Block]
				Case "finesyringe"
					;[Block]
					If CanUseItem(False, True)
						me\HealTimer = Rnd(20.0, 40.0)
						me\StaminaEffect = Rnd(0.4, 0.6)
						me\StaminaEffectTimer = Rnd(20.0, 30.0)
						
						CreateMsg(GetLocalString("msg", "syringe_2"))
						
						RemoveItem(SelectedItem)
					EndIf
					;[End Block]
				Case "veryfinesyringe"
					;[Block]
					If CanUseItem(False, True)
						Select(Rand(3))
							Case 1
								;[Block]
								me\HealTimer = Rnd(40.0, 60.0)
								me\StaminaEffect = 0.1
								me\StaminaEffectTimer = 30.0
								CreateMsg(GetLocalString("msg", "syringe_3"))
								;[End Block]
							Case 2
								;[Block]
								chs\SuperMan = True
								CreateMsg(GetLocalString("msg", "syringe_4"))
								;[End Block]
							Case 3
								;[Block]
								me\VomitTimer = 30.0
								CreateMsg(GetLocalString("msg", "syringe_5"))
								;[End Block]
						End Select
						
						RemoveItem(SelectedItem)
					EndIf
					;[End Block]
				Case "syringeinf"
					;[Block]
					If CanUseItem(False, True)
						CreateMsg(GetLocalString("msg", "syringe_6"))
						
						me\VomitTimer = 70.0 * 1.0
						
						I_008\Timer = I_008\Timer + (1.0 + (1.0 * SelectedDifficulty\AggressiveNPCs))
						RemoveItem(SelectedItem)
					EndIf
					;[End Block]
				Case "radio", "18vradio", "fineradio", "veryfineradio"
					;[Block]
					If SelectedItem\ItemTemplate\Img = 0
						SelectedItem\ItemTemplate\Img = LoadImage_Strict(SelectedItem\ItemTemplate\ImgPath)
						SelectedItem\ItemTemplate\Img = ScaleImage2(SelectedItem\ItemTemplate\Img, MenuScale, MenuScale)
						SelectedItem\ItemTemplate\ImgWidth = ImageWidth(SelectedItem\ItemTemplate\Img)
						SelectedItem\ItemTemplate\ImgHeight = ImageHeight(SelectedItem\ItemTemplate\Img)
						MaskImage(SelectedItem\ItemTemplate\Img, 255, 0, 255)
					EndIf
					
					Local RadioType%
					
					Select(SelectedItem\ItemTemplate\TempName)
						Case "18vradio"
							;[Block]
							RadioType = 1
							;[End Block]
						Case "fineradio"
							;[Block]
							RadioType = 2
							;[End Block]
						Case "veryfineradio"
							;[Block]
							RadioType = 3
							;[End Block]
						Default
							;[Block]
							RadioType = 0
							;[End Block]
					End Select
					If RadioType < 2 Then SelectedItem\State = Max(0.0, SelectedItem\State - fps\Factor[0] * 0.004)
					
					; ~ RadioState[5] = Has the "use the number keys" -message been shown yet (True / False)
					; ~ RadioState[6] = A timer for the "code channel"
					; ~ RadioState[7] = Another timer for the "code channel"
					
					If SelectedItem\State > 0.0 Lor RadioType > 1
						IsUsingRadio = True
						If RadioState[5] = 0.0
							CreateMsg(GetLocalString("msg", "radio"))
							RadioState[5] = 1.0
							RadioState[0] = -1.0
						EndIf
						
						If RID = r_dimension_106 Lor RID = r_dimension_1499
							For i = 0 To 5
								If ChannelPlaying(RadioCHN[i]) Then PauseChannel(RadioCHN[i])
							Next
							
							If (Not ChannelPlaying(RadioCHN[6])) Then RadioCHN[6] = PlaySound_Strict(RadioStatic)
						ElseIf CoffinDistance < 8.0
							For i = 0 To 5
								If ChannelPlaying(RadioCHN[i]) Then PauseChannel(RadioCHN[i])
							Next
							
							If (Not ChannelPlaying(RadioCHN[6])) Then RadioCHN[6] = PlaySound_Strict(RadioStatic895)
						Else
							Select(Int(SelectedItem\State2))
								Case 0
									;[Block]
									If opt\UserTrackMode = 0
										If (Not ChannelPlaying(RadioCHN[6])) Then RadioCHN[6] = PlaySound_Strict(RadioStatic)
									ElseIf UserTrackMusicAmount < 1
										If (Not ChannelPlaying(RadioCHN[6])) Then RadioCHN[6] = PlaySound_Strict(RadioStatic)
									Else
										If ChannelPlaying(RadioCHN[6]) Then StopChannel(RadioCHN[6]) : RadioCHN[6] = 0
										
										If (Not ChannelPlaying(RadioCHN[0]))
											If (Not UserTrackFlag)
												If opt\UserTrackMode = 1
													If RadioState[0] < (UserTrackMusicAmount - 1)
														RadioState[0] = RadioState[0] + 1.0
													Else
														RadioState[0] = 0.0
													EndIf
													UserTrackFlag = True
												ElseIf opt\UserTrackMode = 2
													RadioState[0] = Rand(0.0, UserTrackMusicAmount - 1)
												EndIf
											EndIf
											If CurrUserTrack <> 0 Then FreeSound_Strict(CurrUserTrack) : CurrUserTrack = 0
											CurrUserTrack = LoadSound_Strict("SFX\Radio\UserTracks\" + UserTrackName[RadioState[0]])
											RadioCHN[0] = PlaySound_Strict(CurrUserTrack)
										Else
											UserTrackFlag = False
										EndIf
										
										If KeyHit(2)
											PlaySound_Strict(RadioSquelch)
											If (Not UserTrackFlag)
												If opt\UserTrackMode = 1
													If RadioState[0] < (UserTrackMusicAmount - 1)
														RadioState[0] = RadioState[0] + 1.0
													Else
														RadioState[0] = 0.0
													EndIf
													UserTrackFlag = True
												ElseIf opt\UserTrackMode = 2
													RadioState[0] = Rand(0.0, UserTrackMusicAmount - 1)
												EndIf
											EndIf
											If CurrUserTrack <> 0 Then FreeSound_Strict(CurrUserTrack) : CurrUserTrack = 0
											CurrUserTrack = LoadSound_Strict("SFX\Radio\UserTracks\" + UserTrackName[RadioState[0]])
											RadioCHN[0] = PlaySound_Strict(CurrUserTrack)
										EndIf
									EndIf
									;[End Block]
								Case 1
									;[Block]
									If ChannelPlaying(RadioCHN[6]) Then StopChannel(RadioCHN[6]) : RadioCHN[6] = 0
									
									If (Not ChannelPlaying(RadioCHN[1]))
										If RadioState[1] >= 5.0
											RadioCHN[1] = PlaySound_Strict(RadioSFX(0, 1))
											RadioState[1] = 0.0
										Else
											RadioState[1] = RadioState[1] + 1.0
											RadioCHN[1] = PlaySound_Strict(RadioSFX(0, 0))
										EndIf
									EndIf
									;[End Block]
								Case 2
									;[Block]
									If ChannelPlaying(RadioCHN[6]) Then StopChannel(RadioCHN[6]) : RadioCHN[6] = 0
									
									If (Not ChannelPlaying(RadioCHN[2]))
										RadioState[2] = RadioState[2] + 1.0
										If RadioState[2] = 17.0 Then RadioState[2] = 1.0
										If Floor(RadioState[2] / 2.0) = Ceil(RadioState[2] / 2.0)
											RadioCHN[2] = PlaySound_Strict(RadioSFX(1, Int(RadioState[2] / 2.0)))
										Else
											RadioCHN[2] = PlaySound_Strict(RadioSFX(1, 0))
										EndIf
									EndIf
									;[End Block]
								Case 3
									;[Block]
									If (Not ChannelPlaying(RadioCHN[6])) And (Not ChannelPlaying(RadioCHN[3])) Then RadioCHN[6] = PlaySound_Strict(RadioStatic)
									
									If MTFTimer > 0.0
										If (Not RadioState2[6]) Then RadioState[3] = RadioState[3] + Max(Rand(-10, 1), 0.0)
										Select(RadioState[3])
											Case 40
												;[Block]
												If (Not RadioState2[0])
													RadioCHN[3] = PlaySound_Strict(LoadTempSound("SFX\Character\MTF\Random1.ogg"), True)
													RadioState[3] = RadioState[3] + 1.0
													RadioState2[0] = True
												EndIf
												;[End Block]
											Case 400
												;[Block]
												If (Not RadioState2[1])
													RadioCHN[3] = PlaySound_Strict(LoadTempSound("SFX\Character\MTF\Random2.ogg"), True)
													RadioState[3] = RadioState[3] + 1.0
													RadioState2[1] = True
												EndIf
												;[End Block]
											Case 800
												;[Block]
												If (Not RadioState2[2])
													RadioCHN[3] = PlaySound_Strict(LoadTempSound("SFX\Character\MTF\Random3.ogg"), True)
													RadioState[3] = RadioState[3] + 1.0
													RadioState2[2] = True
												EndIf
												;[End Block]
											Case 1200
												;[Block]
												If (Not RadioState2[3])
													RadioCHN[3] = PlaySound_Strict(LoadTempSound("SFX\Character\MTF\Random4.ogg"), True)
													RadioState[3] = RadioState[3] + 1.0
													RadioState2[3] = True
												EndIf
												;[End Block]
											Case 1600
												;[Block]
												If (Not RadioState2[4])
													RadioCHN[3] = PlaySound_Strict(LoadTempSound("SFX\Character\MTF\Random5.ogg"), True)
													RadioState[3] = RadioState[3] + 1.0
													RadioState2[4] = True
												EndIf
												;[End Block]
											Case 2000
												;[Block]
												If (Not RadioState2[5])
													RadioCHN[3] = PlaySound_Strict(LoadTempSound("SFX\Character\MTF\Random6.ogg"), True)
													RadioState[3] = RadioState[3] + 1.0
													RadioState2[5] = True
												EndIf
												;[End Block]
											Case 2400
												;[Block]
												If (Not RadioState2[6])
													RadioCHN[3] = PlaySound_Strict(LoadTempSound("SFX\Character\MTF\Random7.ogg"), True)
													RadioState[3] = RadioState[3] + 1.0
													RadioState2[6] = True
												EndIf
												;[End Block]
										End Select
									EndIf
									;[End Block]
								Case 4
									;[Block]
									If (Not ChannelPlaying(RadioCHN[6])) Then RadioCHN[6] = PlaySound_Strict(RadioStatic)
									
									If (Not ChannelPlaying(RadioCHN[4]))
										If (Not RemoteDoorOn) And RadioState[8] = 0
											RadioCHN[4] = PlaySound_Strict(LoadTempSound("SFX\Radio\Chatter3.ogg"), True)
											RadioState[8] = 1
										Else
											RadioState[4] = RadioState[4] + Max(Rand(-10, 1), 0.0)
											
											Select(RadioState[4])
												Case 10
													;[Block]
													If (Not n_I\Curr106\Contained)
														If (Not RadioState3[0])
															RadioCHN[4] = PlaySound_Strict(LoadTempSound("SFX\Radio\OhGod.ogg"), True)
															RadioState[4] = RadioState[4] + 1.0
															RadioState3[0] = True
														EndIf
													EndIf
													;[End Block]
												Case 100
													;[Block]
													If (Not RadioState3[1])
														RadioCHN[4] = PlaySound_Strict(LoadTempSound("SFX\Radio\Chatter2.ogg"), True)
														RadioState[4] = RadioState[4] + 1.0
														RadioState3[1] = True
													EndIf
													;[End Block]
												Case 158
													;[Block]
													If MTFTimer = 0.0 And (Not RadioState3[2])
														RadioCHN[4] = PlaySound_Strict(LoadTempSound("SFX\Radio\Franklin1.ogg"), True)
														RadioState[4] = RadioState[4] + 1.0
														RadioState[2] = True
													EndIf
													;[End Block]
												Case 200
													;[Block]
													If (Not RadioState3[3])
														RadioCHN[4] = PlaySound_Strict(LoadTempSound("SFX\Radio\Chatter4.ogg"), True)
														RadioState[4] = RadioState[4] + 1.0
														RadioState3[3] = True
													EndIf
													;[End Block]
												Case 260
													;[Block]
													If (Not RadioState3[4])
														RadioCHN[4] = PlaySound_Strict(LoadTempSound("SFX\SCP\035\RadioHelp1.ogg"), True)
														RadioState[4] = RadioState[4] + 1.0
														RadioState3[4] = True
													EndIf
													;[End Block]
												Case 300
													;[Block]
													If (Not RadioState3[5])
														RadioCHN[4] = PlaySound_Strict(LoadTempSound("SFX\Radio\Chatter1.ogg"), True)
														RadioState[4] = RadioState[4] + 1.0
														RadioState3[5] = True
													EndIf
													;[End Block]
												Case 350
													;[Block]
													If (Not RadioState3[6])
														RadioCHN[4] = PlaySound_Strict(LoadTempSound("SFX\Radio\Franklin2.ogg"), True)
														RadioState[4] = RadioState[4] + 1.0
														RadioState3[6] = True
													EndIf
													;[End Block]
												Case 400
													;[Block]
													If (Not RadioState3[7])
														RadioCHN[4] = PlaySound_Strict(LoadTempSound("SFX\SCP\035\RadioHelp2.ogg"), True)
														RadioState[4] = RadioState[4] + 1.0
														RadioState3[7] = True
													EndIf
													;[End Block]
												Case 450
													;[Block]
													If (Not RadioState3[8])
														RadioCHN[4] = PlaySound_Strict(LoadTempSound("SFX\Radio\Franklin3.ogg"), True)
														RadioState[4] = RadioState[4] + 1.0
														RadioState3[8] = True
													EndIf
													;[End Block]
												Case 600
													;[Block]
													If (Not RadioState3[9])
														RadioCHN[4] = PlaySound_Strict(LoadTempSound("SFX\Radio\Franklin4.ogg"), True)
														RadioState[4] = RadioState[4] + 1.0
														RadioState3[9] = True
													EndIf
													;[End Block]
											End Select
										EndIf
									EndIf
									;[End Block]
								Case 5
									;[Block]
									If ChannelPlaying(RadioCHN[6]) Then StopChannel(RadioCHN[6]) : RadioCHN[6] = 0
									If (Not ChannelPlaying(RadioCHN[5])) Then RadioCHN[5] = PlaySound_Strict(RadioStatic)
									;[End Block]
							End Select
							
							If RadioType = 3
								SelectedItem\State2 = -1
								If (Not ChannelPlaying(RadioCHN[6])) Then RadioCHN[6] = PlaySound_Strict(RadioStatic)
								RadioState[6] = RadioState[6] + fps\Factor[0]
								Temp = Mid(Str(CODE_DR_MAYNARD), RadioState[8] + 1.0, 1)
								If RadioState[6] - fps\Factor[0] <= RadioState[7] * 50.0 And RadioState[6] > RadioState[7] * 50.0
									PlaySound_Strict(RadioBuzz)
									RadioState[7] = RadioState[7] + 1.0
									If RadioState[7] >= Temp
										RadioState[7] = 0.0
										RadioState[6] = -100.0
										RadioState[8] = RadioState[8] + 1.0
										If RadioState[8] = 4.0 Then RadioState[8] = 0.0 : RadioState[6] = -200.0
									EndIf
								EndIf
							Else
								For i = 2 To 6
									If KeyHit(i)
										If SelectedItem\State2 <> i - 2
											PlaySound_Strict(RadioSquelch)
											PauseChannel(RadioCHN[Int(SelectedItem\State2)])
										EndIf
										SelectedItem\State2 = i - 2
										StopChannel(RadioCHN[6]) : RadioCHN[6] = 0
									EndIf
								Next
								If (Not ChannelPlaying(RadioCHN[SelectedItem\State2])) Then ResumeChannel(RadioCHN[SelectedItem\State2])
							EndIf
						EndIf
						
						If RadioType < 2
							If SelectedItem\State <= 20.0
								UpdateBatteryTimer()
								If BatMsgTimer >= 70.0 * 1.0
									If (Not ChannelPlaying(LowBatteryCHN[0])) Then LowBatteryCHN[0] = PlaySound_Strict(LowBatterySFX[0])
								EndIf
							EndIf
						EndIf
					EndIf
					;[End Block]
				Case "nav", "nav310", "navulti", "nav300"
					;[Block]
					If SelectedItem\ItemTemplate\Img = 0
						SelectedItem\ItemTemplate\Img = LoadImage_Strict(SelectedItem\ItemTemplate\ImgPath)
						SelectedItem\ItemTemplate\Img = ScaleImage2(SelectedItem\ItemTemplate\Img, MenuScale, MenuScale)
						SelectedItem\ItemTemplate\ImgWidth = ImageWidth(SelectedItem\ItemTemplate\Img) / 2
						SelectedItem\ItemTemplate\ImgHeight = ImageHeight(SelectedItem\ItemTemplate\Img) / 2
						MaskImage(SelectedItem\ItemTemplate\Img, 255, 0, 255)
					EndIf
					
					If SelectedItem\ItemTemplate\Name = "nav" Lor SelectedItem\ItemTemplate\Name = "nav310"
						SelectedItem\State = Max(0.0, SelectedItem\State - fps\Factor[0] * 0.005)
						
						If SelectedItem\State > 0.0 And SelectedItem\State <= 20.0
							UpdateBatteryTimer()
							If BatMsgTimer >= 70.0 * 1.0
								If (Not ChannelPlaying(LowBatteryCHN[0])) Then LowBatteryCHN[0] = PlaySound_Strict(LowBatterySFX[0])
							EndIf
						EndIf
					EndIf
					;[End Block]
				Case "cigarette"
					;[Block]
					If CanUseItem(True)
						Select(Rand(6))
							Case 1
								;[Block]
								CreateMsg(GetLocalString("msg", "cigarette_1"))
								;[End Block]
							Case 2
								;[Block]
								CreateMsg(GetLocalString("msg", "cigarette_2"))
								;[End Block]
							Case 3
								;[Block]
								CreateMsg(GetLocalString("msg", "cigarette_3"))
								;[End Block]
							Case 4
								;[Block]
								CreateMsg(GetLocalString("msg", "cigarette_4"))
								;[End Block]
							Case 5
								;[Block]
								CreateMsg(GetLocalString("msg", "cigarette_5"))
								;[End Block]
							Case 6
								;[Block]
								CreateMsg(GetLocalString("msg", "cigarette_6"))
								;[End Block]
						End Select
						RemoveItem(SelectedItem)
					EndIf
					;[End Block]
				Case "scp420j"
					;[Block]
					If CanUseItem(True)
						If I_714\Using > 0
							CreateMsg(GetLocalString("msg", "420j.no"))
						Else
							CreateMsg(GetLocalString("msg", "420j.yeah"))
							DamagePlayer(Rand(1, 2))
							me\BlurTimer = 500.0
							GiveAchievement(Achv420_J)
							PlaySound_Strict(LoadTempSound("SFX\Music\Using420J.ogg"))
						EndIf
						RemoveItem(SelectedItem)
					EndIf
					;[End Block]
				Case "joint", "scp420s"
					;[Block]
					If CanUseItem(True)
						If I_714\Using > 0
							CreateMsg(GetLocalString("msg", "420j.no"))
						Else
							CreateMsg(GetLocalString("msg", "420j.dead"))
							msg\DeathMsg = Format(GetLocalString("death", "joint"), Occupation)
							Kill()
						EndIf
						RemoveItem(SelectedItem)
					EndIf
					;[End Block]
				Case "scp714", "coarse714"
					;[Block]
					If I_714\Using > 0
						CreateMsg(GetLocalString("msg", "714.off"))
						I_714\Using = 0
					Else
						GiveAchievement(Achv714)
						CreateMsg(GetLocalString("msg", "714.on"))
						Select(SelectedItem\ItemTemplate\TempName)
							Case "coarse714"
								;[Block]
								I_714\Using = 1
								;[End Block]
							Case "scp714"
								;[Block]
								I_714\Using = 2
								;[End Block]
						End Select
					EndIf
					SelectedItem = Null
					;[End Block]
				Case "fine714", "ring"
					;[Block]
					If CanUseItem(False, True)
						If SelectedItem\ItemTemplate\TempName = "fine714"
							CreateMsg(GetLocalString("msg", "714.sleep"))
							msg\DeathMsg = Format(GetLocalString("death", "ringsleep"), Occupation)
							Kill()
						Else
							CreateMsg(GetLocalString("msg", "714.small"))
						EndIf
						SelectedItem = Null
					EndIf
					;[End Block]
				Case "ticket"
					;[Block]
					If SelectedItem\ItemTemplate\Img = 0
						SelectedItem\ItemTemplate\Img = LoadImage_Strict(SelectedItem\ItemTemplate\ImgPath)
						SelectedItem\ItemTemplate\Img = ScaleImage2(SelectedItem\ItemTemplate\Img, MenuScale, MenuScale)
						SelectedItem\ItemTemplate\ImgWidth = ImageWidth(SelectedItem\ItemTemplate\Img) / 2
						SelectedItem\ItemTemplate\ImgHeight = ImageHeight(SelectedItem\ItemTemplate\Img) / 2
						MaskImage(SelectedItem\ItemTemplate\Img, 255, 0, 255)
					EndIf
					
					If SelectedItem\State = 0.0
						CreateMsg(GetLocalString("msg", "ticket"))
						PlaySound_Strict(LoadTempSound("SFX\SCP\1162_ARC\NostalgiaCancer" + Rand(5) + ".ogg"))
						SelectedItem\State = 1.0
					EndIf
					;[End Block]
				Case "badge", "burntbadge"
					;[Block]
					If SelectedItem\ItemTemplate\Img = 0
						SelectedItem\ItemTemplate\Img = LoadImage_Strict(SelectedItem\ItemTemplate\ImgPath)
						SelectedItem\ItemTemplate\Img = ScaleImage2(SelectedItem\ItemTemplate\Img, MenuScale, MenuScale)
						SelectedItem\ItemTemplate\ImgWidth = ImageWidth(SelectedItem\ItemTemplate\Img) / 2
						SelectedItem\ItemTemplate\ImgHeight = ImageHeight(SelectedItem\ItemTemplate\Img) / 2
					EndIf
					
					If SelectedItem\State = 0.0
						PlaySound_Strict(LoadTempSound("SFX\SCP\1162_ARC\NostalgiaCancer" + Rand(6, 10) + ".ogg"))
						SelectedItem\State = 1.0
					EndIf
					;[End Block]
				Case "oldbadge"
					;[Block]
					If SelectedItem\ItemTemplate\Img = 0
						SelectedItem\ItemTemplate\Img = LoadImage_Strict(SelectedItem\ItemTemplate\ImgPath)
						SelectedItem\ItemTemplate\Img = ScaleImage2(SelectedItem\ItemTemplate\Img, MenuScale, MenuScale)
						SelectedItem\ItemTemplate\ImgWidth = ImageWidth(SelectedItem\ItemTemplate\Img) / 2
						SelectedItem\ItemTemplate\ImgHeight = ImageHeight(SelectedItem\ItemTemplate\Img) / 2
						MaskImage(SelectedItem\ItemTemplate\Img, 255, 0, 255)
					EndIf
					
					If SelectedItem\State = 0.0
						CreateMsg(GetLocalString("msg", "oldbadge"))
						PlaySound_Strict(LoadTempSound("SFX\SCP\1162_ARC\NostalgiaCancer" + Rand(6, 10) + ".ogg"))
						SelectedItem\State = 1.0
					EndIf
					;[End Block]
				Case "oldpaper"
					;[Block]
					If SelectedItem\ItemTemplate\Img = 0
						SelectedItem\ItemTemplate\Img = LoadImage_Strict(SelectedItem\ItemTemplate\ImgPath)
						SelectedItem\ItemTemplate\Img = ScaleImage2(SelectedItem\ItemTemplate\Img, MenuScale, MenuScale)
						SelectedItem\ItemTemplate\ImgWidth = ImageWidth(SelectedItem\ItemTemplate\Img) / 2
						SelectedItem\ItemTemplate\ImgHeight = ImageHeight(SelectedItem\ItemTemplate\Img) / 2
					EndIf
					
					If SelectedItem\State = 0.0
						me\BlurTimer = 1000.0
						CreateMsg(GetLocalString("msg", "oldpaper"))
						PlaySound_Strict(LoadTempSound("SFX\SCP\1162_ARC\NostalgiaCancer" + Rand(6, 10) + ".ogg"))
						SelectedItem\State = 1.0
					EndIf
					;[End Block]
				Case "key"
					;[Block]
					If SelectedItem\State = 0.0
						PlaySound_Strict(LoadTempSound("SFX\SCP\1162_ARC\NostalgiaCancer" + Rand(6, 10) + ".ogg"))
						CreateMsg(GetLocalString("msg", "lostkey"))
						SelectedItem\State = 1.0
					EndIf
					;[End Block]
				Case "coin"
					;[Block]
					If SelectedItem\State = 0.0
						PlaySound_Strict(LoadTempSound("SFX\SCP\1162_ARC\NostalgiaCancer" + Rand(5) + ".ogg"))
						SelectedItem\State = 1.0
					EndIf
					;[End Block]
				Case "scp427"
					;[Block]
					If I_427\Using
						CreateMsg(GetLocalString("msg", "427.off"))
						I_427\Using = False
					Else
						GiveAchievement(Achv427)
						CreateMsg(GetLocalString("msg", "427.on"))
						I_427\Using = True
					EndIf
					SelectedItem = Null
					;[End Block]
				Case "pill"
					;[Block]
					If CanUseItem(True)
						CreateMsg(GetLocalString("msg", "pill"))
						I_1025\State[0] = 0.0
						RemoveItem(SelectedItem)
					EndIf
					;[End Block]
				Case "scp500pilldeath"
					;[Block]
					If CanUseItem(True)
						CreateMsg(GetLocalString("msg", "pill"))
						
						If I_427\Timer < 70.0 * 360.0 Then I_427\Timer = 70.0 * 360.0
						
						RemoveItem(SelectedItem)
					EndIf
					;[End Block]
				Case "scp500"
					;[Block]
					If I_500\Taken < Rand(20)
						If ItemAmount < MaxItemAmount
							For i = 0 To MaxItemAmount - 1
								If Inventory(i) = Null
									Inventory(i) = CreateItem("SCP-500-01", "scp500pill", 0.0, 0.0, 0.0)
									Inventory(i)\Picked = True
									Inventory(i)\Dropped = -1
									Inventory(i)\ItemTemplate\Found = True
									HideEntity(Inventory(i)\Collider)
									EntityType(Inventory(i)\Collider, HIT_ITEM)
									EntityParent(Inventory(i)\Collider, 0)
									Exit
								EndIf
							Next
							CreateMsg(GetLocalString("msg", "500"))
							I_500\Taken = I_500\Taken + 1
						Else
							CreateMsg(GetLocalString("msg", "cantcarry"))
						EndIf
						SelectedItem = Null
					Else
						I_500\Taken = 0
						RemoveItem(SelectedItem)
					EndIf
					;[End Block]
				Case "scp1123"
					;[Block]
					Use1123()
					SelectedItem = Null
					;[End Block]
				Case "key0", "key1", "key2", "key3", "key4", "key5", "key6", "keyomni", "scp860", "hand", "hand2", "hand3", "25ct", "scp005", "key", "coin", "mastercard", "paper"
					;[Block]
					; ~ Skip this line
					;[End Block]
				Default
					;[Block]
					; ~ Check if the item is an inventory-type object
					If SelectedItem\InvSlots > 0 Then OtherOpen = SelectedItem
					ResetInput()
					SelectedItem = Null
					;[End Block]
			End Select
			
			If ((mo\MouseHit2 Lor KeyHit(key\INVENTORY)) And (Not MenuOpen)) Lor me\Health = 0 Lor me\FallTimer < 0.0 Lor (Not me\Playable) Lor me\Zombie
				Select(SelectedItem\ItemTemplate\TempName)
					Case "firstaid", "finefirstaid", "firstaid2"
						;[Block]
						SelectedItem\State = 0.0
						;[End Block]
					Case "scp1025"
						;[Block]
						SelectedItem\State3 = 0.0
						;[End Block]
				End Select
				If SelectedItem\ItemTemplate\SoundID <> 66 Then PlaySound_Strict(PickSFX[SelectedItem\ItemTemplate\SoundID])
				If SelectedItem\ItemTemplate\Img <> 0 Then FreeImage(SelectedItem\ItemTemplate\Img) : SelectedItem\ItemTemplate\Img = 0
				
				For i = 0 To 6
					If ChannelPlaying(RadioCHN[i]) Then StopChannel(RadioCHN[i]) : RadioCHN[i] = 0
				Next
				IsUsingRadio = False
				
				SelectedItem = Null
			EndIf
		Else
			If ChannelPlaying(LowBatteryCHN[0]) Then StopChannel(LowBatteryCHN[0]) : LowBatteryCHN[0] = 0
		EndIf
	EndIf
	
	For it.Items = Each Items
		If it <> SelectedItem
			Select(it\ItemTemplate\TempName)
				Case "firstaid", "finefirstaid", "firstaid2"
					;[Block]
					it\State = 0.0
					;[End Block]
				Case "scp1025"
					;[Block]
					it\State3 = 0.0
					;[End Block]
			End Select
		EndIf
	Next
	
	If PrevInvOpen And (Not InvOpen) Then MoveMouse(mo\Viewport_Center_X, mo\Viewport_Center_Y)
	
	CatchErrors("Uncaught: UpdateGUI()")
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D TSS