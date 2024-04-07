Include "Source Code\Math_Core.bb"
Include "Source Code\Strict_Functions_Core.bb"

Global ButtonSFX%[2]

Const TICK_DURATION# = 70.0 / 60.0

Type FramesPerSeconds
	Field Accumulator#
	Field PrevTime%
	Field CurrTime%
	Field FPS%
	Field TempFPS%
	Field Goal%
	Field LoopDelay%
	Field Factor#[2]
End Type

Global fps.FramesPerSeconds = New FramesPerSeconds

Global MilliSec%

fps\LoopDelay = MilliSecs()

Global SplitSpace$

If opt\LauncherEnabled
	Local lnchr.Launcher
	
	lnchr.Launcher = New Launcher
	
	lnchr\TotalGFXModes = CountGfxModes3D()
	
	opt\AspectRatio = 1.0
	
	UpdateLauncher(lnchr)
	
	Delete(lnchr)
EndIf

Global GraphicWidthFloat#, RealGraphicWidthFloat#
Global GraphicHeightFloat#, RealGraphicHeightFloat#

; ~ New "fake fullscreen" - ENDSHN Psst, it's called borderless windowed mode -- Love Mark
If opt\DisplayMode = 1
	opt\RealGraphicWidth = DesktopWidth() : opt\RealGraphicHeight = DesktopHeight()
	GraphicWidthFloat = Float(opt\GraphicWidth) : GraphicHeightFloat = Float(opt\GraphicHeight)
	RealGraphicWidthFloat = Float(opt\RealGraphicWidth) : RealGraphicHeightFloat = Float(opt\RealGraphicHeight)
	opt\AspectRatio = (GraphicWidthFloat / GraphicHeightFloat) / (RealGraphicWidthFloat / RealGraphicHeightFloat)
	Graphics3DExt(opt\RealGraphicWidth, opt\RealGraphicHeight, 0, 4)
Else
	opt\AspectRatio = 1.0
	opt\RealGraphicWidth = opt\GraphicWidth : opt\RealGraphicHeight = opt\GraphicHeight
	GraphicWidthFloat = Float(opt\GraphicWidth) : GraphicHeightFloat = Float(opt\GraphicHeight)
	RealGraphicWidthFloat = Float(opt\RealGraphicWidth) : RealGraphicHeightFloat = Float(opt\RealGraphicHeight)
	Graphics3DExt(opt\GraphicWidth, opt\GraphicHeight, 0, (opt\DisplayMode = 2) + 1)
EndIf

AppTitle("SCP: Classified Stories v" + VersionNumber)

Global MenuScale# = opt\GraphicHeight / 1024.0

Global Input_ResetTime# = 0.0
Global MousePosX#, MousePosY#

Function UpdateMouseInput%()
	MousePosX = ScaledMouseX()
	MousePosY = ScaledMouseY()
	
	If Input_ResetTime > 0.0
		Input_ResetTime = Max(Input_ResetTime - fps\Factor[1], 0.0)
	Else
		mo\DoubleClick = False
		mo\MouseHit1 = MouseHit(1)
		If mo\MouseHit1
			If MilliSecs() - mo\LastMouseHit1 < 800 Then mo\DoubleClick = True
			mo\LastMouseHit1 = MilliSecs()
		EndIf
		
		Local PrevMouseDown1% = mo\MouseDown1
		
		mo\MouseDown1 = MouseDown(1)
		mo\MouseUp1 = (PrevMouseDown1 And (Not mo\MouseDown1))
		
		mo\MouseHit2 = MouseHit(2)
		
		Local PrevMouseDown2% = mo\MouseDown2
		
		mo\MouseDown2 = MouseDown(2)
		mo\MouseUp2 = (PrevMouseDown2 And (Not mo\MouseDown2))
		
		mo\MouseHit3 = MouseHit(3)
		
		Local PrevMouseDown3% = mo\MouseDown3
		
		mo\MouseDown3 = MouseDown(3)
		mo\MouseUp3 = (PrevMouseDown3 And (Not mo\MouseDown3))
	EndIf
End Function

Function StopMouseMovement%()
	MouseXSpeed() : MouseYSpeed() : MouseZSpeed()
	mo\Mouse_X_Speed_1 = 0.0
	mo\Mouse_Y_Speed_1 = 0.0
End Function

Function ResetInput%()
	FlushKeys()
	FlushMouse()
	mo\MouseHit1 = False
	mo\MouseHit2 = False
	mo\MouseHit3 = False
	mo\MouseDown1 = False
	mo\MouseDown2 = False
	mo\MouseDown3 = False
	mo\MouseUp1 = False
	mo\MouseUp2 = False
	mo\MouseUp3 = False
	mo\LastMouseHit1 = False
	GrabbedEntity = 0
	Input_ResetTime = 10.0
	HitKeyUse = False
	DownKeyUse = False
End Function

Function GetAnyKey%()
	
	If GetKey() <> 0 Lor MouseHit(1) Lor MouseHit(2) Lor MouseHit(3) Then Return(True)
	
End Function

mo\Mouselook_X_Inc = 0.3 ; ~ This sets both the sensitivity and direction (+ / -) of the mouse on the X axis
mo\Mouselook_Y_Inc = 0.3 ; ~ This sets both the sensitivity and direction (+ / -) of the mouse on the Y axis
mo\Mouse_Left_Limit = 250 * MenuScale
mo\Mouse_Right_Limit = opt\GraphicWidth - mo\Mouse_Left_Limit
mo\Mouse_Top_Limit = 150 * MenuScale
mo\Mouse_Bottom_Limit = opt\GraphicHeight - mo\Mouse_Top_Limit ; ~ As above

; ~ Viewport
mo\Viewport_Center_X = opt\GraphicWidth / 2
mo\Viewport_Center_Y = opt\GraphicHeight / 2

SetBuffer(BackBuffer())

SeedRnd(MilliSec)

PlayStartupVideos()

Global CursorIMG%
If opt\DisplayMode = 0
	CursorIMG = LoadImage_Strict("GFX\Menu\Images\Cursor.png")
	CursorIMG = ScaleImage2(CursorIMG, MenuScale, MenuScale)
EndIf

InitLoadingScreens(LoadingScreensFile)

If (Not opt\PlayStartup) Then fo\FontID[Font_Default] = LoadFont_Strict(FontsPath + GetFileLocalString(FontsFile, "Default", "File"), GetFileLocalString(FontsFile, "Default", "Size"))
fo\FontID[Font_Default_Medium] = LoadFont_Strict(FontsPath + GetFileLocalString(FontsFile, "Default_Medium", "File"), GetFileLocalString(FontsFile, "Default_Medium", "Size"))
fo\FontID[Font_Default_Big] = LoadFont_Strict(FontsPath + GetFileLocalString(FontsFile, "Default_Big", "File"), GetFileLocalString(FontsFile, "Default_Big", "Size"))
fo\FontID[Font_Digital] = LoadFont_Strict(FontsPath + GetFileLocalString(FontsFile, "Digital", "File"), GetFileLocalString(FontsFile, "Digital", "Size"))
fo\FontID[Font_Digital_Big] = LoadFont_Strict(FontsPath + GetFileLocalString(FontsFile, "Digital_Big", "File"), GetFileLocalString(FontsFile, "Digital_Big", "Size"))
fo\FontID[Font_Journal] = LoadFont_Strict(FontsPath + GetFileLocalString(FontsFile, "Journal", "File"), GetFileLocalString(FontsFile, "Journal", "Size"))
fo\FontID[Font_Console] = LoadFont_Strict(FontsPath + GetFileLocalString(FontsFile, "Console", "File"), GetFileLocalString(FontsFile, "Console", "Size"))

SetFontEx(fo\FontID[Font_Default_Big])

RenderLoading(0, GetLocalString("loading", "core.main"))

RenderLoading(5, GetLocalString("loading", "core.achv"))

Include "Source Code\Achievements_Core.bb"

RenderLoading(10, GetLocalString("loading", "core.diff"))

Include "Source Code\Difficulty_Core.bb"

RenderLoading(15, GetLocalString("loading", "core.loading"))

Include "Source Code\Loading_Core.bb"

RenderLoading(20, GetLocalString("loading", "core.subtitle"))

Include "Source Code\Subtitles_Core.bb"

RenderLoading(25, GetLocalString("loading", "core.sound"))

Include "Source Code\Sounds_Core.bb"

RenderLoading(30, GetLocalString("loading", "core.item"))

Include "Source Code\Items_Core.bb"

Include "Source Code\Weapons_Core.bb"

RenderLoading(35, GetLocalString("loading", "core.particle"))

Include "Source Code\Particles_Core.bb"

RenderLoading(40, GetLocalString("loading", "core.grap"))

Include "Source Code\Graphics_Core.bb"

RenderLoading(45, GetLocalString("loading", "core.map"))

Include "Source Code\SZL_Core.bb"

Include "Source Code\Map_Core.bb"

RenderLoading(65, GetLocalString("loading", "core.npc"))

Include "Source Code\NPCs_Core.bb"

RenderLoading(70, GetLocalString("loading", "core.event"))

Include "Source Code\Events_Core.bb"

RenderLoading(75, GetLocalString("loading", "core.save"))

Include "Source Code\Save_Core.bb"

RenderLoading(80, GetLocalString("loading", "core.menu"))

Include "Source Code\Menu_Core.bb"

InitMainMenuAssets()
MainMenuOpen = True
ResetInput()

RenderLoading(100)

InitErrorMsgs(12, True)
SetErrorMsg(0, Format(GetLocalString("error", "title"), VersionNumber))

SetErrorMsg(1, Format(Format(GetLocalString("error", "date"), CurrentDate(), "{0}"), CurrentTime(), "{1}"))
SetErrorMsg(2, Format(Format(Format(GetLocalString("error", "os"), SystemProperty("os"), "{0}"), (32 + (GetEnv("ProgramFiles(X86)") <> 0) * 32), "{1}"), SystemProperty("osbuild"), "{2}"))
SetErrorMsg(3, Format(Format(Format(GetLocalString("error", "cpu"), Trim(SystemProperty("cpuname")), "{0}"), SystemProperty("cpuarch"), "{1}"), GetEnv("NUMBER_OF_PROCESSORS"), "{2}"))

SetErrorMsg(10, Format(GetLocalString("error", "ex"), "_CaughtError_") + Chr(10))
SetErrorMsg(11, GetLocalString("error", "shot")) 

Function CatchErrors%(Location$)
	SetErrorMsg(9, Format(GetLocalString("error", "error"), Location))
End Function

Repeat
	SetErrorMsg(4, Format(Format(Format(GetLocalString("error", "gpu"), GfxDriverName(CountGfxDrivers()), "{0}"), ((TotalVidMem() / 1024) - (AvailVidMem() / 1024)), "{1}"), (TotalVidMem() / 1024), "{2}"))
	SetErrorMsg(5, Format(Format(GetLocalString("error", "status"), ((TotalPhys() / 1024) - (AvailPhys() / 1024)), "{0}"), (TotalPhys() / 1024), "{1}"))
	
	Cls()
	
	Local ElapsedMilliSecs%
	
	MilliSec = MilliSecs()
	fps\CurrTime = MilliSec
	
	ElapsedMilliSecs = fps\CurrTime - fps\PrevTime
	If (ElapsedMilliSecs > 0 And ElapsedMilliSecs < 500) Then fps\Accumulator = fps\Accumulator + Max(0.0, Float(ElapsedMilliSecs) * 70.0 / 1000.0)
	fps\PrevTime = fps\CurrTime
	
	If opt\FrameLimit > 0.0
		Local WaitingTime% = (1000.0 / opt\FrameLimit) - (MilliSecs() - fps\LoopDelay)
		
		Delay(WaitingTime)
		fps\LoopDelay = MilliSecs()
	EndIf
	
	fps\Factor[0] = TICK_DURATION
	fps\Factor[1] = fps\Factor[0]
	
	If MainMenuOpen
		If cra\EndingTimer > 2.0
			ShouldPlay = MUS_MENU_CREDITS
			UpdateCredits()
		Else
			UpdateMainMenu()
		EndIf
	Else
		UpdateGame()
	EndIf
	
	RenderGamma()
	
	If KeyHit(key\SCREENSHOT) Then GetScreenshot()
	
	If opt\ShowFPS
		If fps\Goal < MilliSecs()
			fps\FPS = fps\TempFPS
			fps\TempFPS = 0
			fps\Goal = MilliSecs() + 1000
		Else
			fps\TempFPS = fps\TempFPS + 1
		EndIf
	EndIf
	
	Flip(opt\VSync)
Forever

Function UpdateGame%()
	Local e.Events, ev.Events, r.Rooms
	Local i%, TempStr$
	
	If SelectedCustomMap = Null
		TempStr = GetLocalString("menu", "new.seed") + RandomSeed
	Else
		If Len(ConvertToUTF8(SelectedCustomMap\Name)) > 15
			TempStr = GetLocalString("menu", "new.map") + Left(ConvertToUTF8(SelectedCustomMap\Name), 14) + "..."
		Else
			TempStr = GetLocalString("menu", "new.map") + ConvertToUTF8(SelectedCustomMap\Name)
		EndIf
	EndIf
	SetErrorMsg(6, TempStr)
	SetErrorMsg(7, Format(GetLocalString("misc", "room"), PlayerRoom\RoomTemplate\RoomID))
	
	For ev.Events = Each Events
		If ev\room = PlayerRoom
			SetErrorMsg(8, Format(Format(Format(Format(Format(GetLocalString("misc", "event"), ev\EventID, "{0}"), ev\EventState, "{1}"), ev\EventState2, "{2}"), ev\EventState3, "{3}"), ev\EventState4, "{4}") + Chr(10))
			Exit
		EndIf
	Next
	
	CatchErrors("UpdateGame()")
	
	While fps\Accumulator > 0.0
		fps\Accumulator = fps\Accumulator - TICK_DURATION
		If fps\Accumulator <= 0.0 Then CaptureWorld()
		
		If MenuOpen Lor ConsoleOpen Then fps\Factor[0] = 0.0
		
		UpdateMouseInput()
		
		HitKeyUse = KeyHit(key\INTERACT)
		DownKeyUse = KeyDown(key\INTERACT)
		
		If (Not DownKeyUse) And (Not HitKeyUse) Then GrabbedEntity = 0
		
		If ShouldDeleteGadgets Then DeleteMenuGadgets()
		ShouldDeleteGadgets = False
		
		UpdateMusic()
		If opt\EnableSFXRelease Then AutoReleaseSounds()
		
		UpdateStreamSounds()
		
		If (Not (MenuOpen Lor ConsoleOpen Lor cra\EndingTimer > 0.0))
			DrawHandIcon = False
			For i = 0 To 2 Step 2
				DrawArrowIcon[i] = False
				DrawArrowIcon[i + 1] = False
			Next
			
			me\RestoreSanity = True
			ShouldEntitiesFall = True
			
			wa_I\UpdateWaterString = ""
			wa_I\IgnoreOBJ = 0
			
			If PlayerInReachableRoom(True, True)
				
				Select(szl\CurrentZone)
					Case LCZ_SL_3, LCZ_SL_2, LCZ_SL_1, LCZ
						;[Block]
						ShouldPlay = MUS_LCZ
						;[End Block]
					Case HCZ_SL_3, HCZ_SL_2, HCZ_SL_1, HCZ
						;[Block]
						ShouldPlay = MUS_HCZ
						;[End Block]
					Case EZ_SL_3, EZ_SL_2, EZ_SL_1, EZ
						;[Block]
						ShouldPlay = MUS_EZ
						;[End Block]
					Case RCZ_SL_3, RCZ_SL_2, RCZ_SL_1, RCZ
						;[Block]
						ShouldPlay = MUS_RCZ
						;[End Block]
					Case BCZ_SL_3, BCZ_SL_2, BCZ_SL_1, BCZ
						;[Block]
						ShouldPlay = MUS_BCZ
						;[End Block]
					Case GATE_A_TOPSIDE, GATE_B_TOPSIDE, GATE_C_TOPSIDE, GATE_D_TOPSIDE
						;[Block]
						ShouldPlay = MUS_SURFACE_DAY
						;[End Block]
				End Select
				
				If Rand(1500) = 1
					For i = 0 To 5
						If AmbientSFX(i, CurrAmbientSFX) <> 0
							If (Not ChannelPlaying(AmbientSFXCHN)) Then FreeSound_Strict(AmbientSFX(i, CurrAmbientSFX)) : AmbientSFX(i, CurrAmbientSFX) = 0
						EndIf
					Next
					
					PositionEntity(SoundEmitter, EntityX(Camera) + Rnd(-1.0, 1.0), 0.0, EntityZ(Camera) + Rnd(-1.0, 1.0))
					
					If Rand(3) = 1 Then me\Zone = 3
					
					If PlayerRoom\RoomTemplate\RoomID = r_cont1_173_intro
						me\Zone = 4
					ElseIf forest_event <> Null
						If PlayerRoom = forest_event\room
							If forest_event\EventState = 1.0
								me\Zone = 5
								PositionEntity(SoundEmitter, EntityX(SoundEmitter), 30.0, EntityZ(SoundEmitter))
							EndIf
						EndIf
					EndIf
					
					CurrAmbientSFX = Rand(0, AmbientSFXAmount[me\Zone] - 1)
					
					Select(szl\CurrentZone)
						Case LCZ, HCZ, EZ
							;[Block]
							If AmbientSFX((szl\CurrentZone - 1), CurrAmbientSFX) = 0 Then AmbientSFX((szl\CurrentZone - 1), CurrAmbientSFX) = LoadSound_Strict("SFX\Ambient\Zone" + (szl\CurrentZone) + "\Ambient" + (CurrAmbientSFX + 1) + ".ogg")
							;[End Block]
					End Select
					
					Select(me\Zone)
						Case 3
							;[Block]
							If AmbientSFX(me\Zone, CurrAmbientSFX) = 0 Then AmbientSFX(me\Zone, CurrAmbientSFX) = LoadSound_Strict("SFX\Ambient\General\Ambient" + (CurrAmbientSFX + 1) + ".ogg")
							;[End Block]
						Case 4
							;[Block]
							If AmbientSFX(me\Zone, CurrAmbientSFX) = 0 Then AmbientSFX(me\Zone, CurrAmbientSFX) = LoadSound_Strict("SFX\Ambient\Pre-breach\Ambient" + (CurrAmbientSFX + 1) + ".ogg")
							;[End Block]
						Case 5
							;[Block]
							If AmbientSFX(me\Zone, CurrAmbientSFX) = 0 Then AmbientSFX(me\Zone, CurrAmbientSFX) = LoadSound_Strict("SFX\Ambient\Forest\Ambient" + (CurrAmbientSFX + 1) + ".ogg")
							;[End Block]
					End Select
					
					AmbientSFXCHN = PlaySound2(AmbientSFX((szl\CurrentZone - 1), CurrAmbientSFX), Camera, SoundEmitter)
				EndIf
				UpdateSoundOrigin(AmbientSFXCHN, Camera, SoundEmitter)
				
				If Rand(50000) = 3
					me\LightBlink = Rnd(1.0, 2.0)
					PlaySound_Strict(LoadTempSound("SFX\SCP\079\Broadcast" + Rand(8) + ".ogg"), True)
				EndIf
			EndIf
			
			me\SndVolume = CurveValue(0.0, me\SndVolume, 5.0)
			InFacility = IsInFacility(EntityY(me\Collider))
			If (Not IsPlayerOutsideFacility()) Then HideDistance = 17.0
			UpdateDeaf()
			UpdateDecals()
			UpdateMouseLook()
			UpdateMoving()
			UpdateSaveState()
			UpdateVomit()
			UpdateEscapeTimer()
			UpdatePlayerModel()
			If me\Health > 0
				UpdateWeapons()
				UpdateWeaponBreathing()
				UpdateIronSight()
			EndIf
			DecalStep = 0
			If PlayerRoom\RoomTemplate\RoomID = r_dimension_106
				UpdateSoundEmitters()
				If QuickLoadPercent = -1 Lor QuickLoadPercent = 100 Then UpdateDimension106()
			Else
				If szl\Loading = 0
					UpdateElevators()
					UpdateFluLights()
					UpdateLightVolume()
					UpdateLights(Camera)
					UpdateEmitters()
					UpdateDoors()
					UpdateSecurityCams()
					UpdateScreens()
					UpdateSoundEmitters()
					If wa_I\UpdateWaterString <> "" Then UpdateWater(wa_I\UpdateWaterString)
				EndIf
				If IsPlayerOutsideFacility()
					If QuickLoadPercent = -1 Lor QuickLoadPercent = 100 Then UpdateEndings()
				Else
					UpdateRooms()
					If QuickLoadPercent = -1 Lor QuickLoadPercent = 100 Then UpdateEvents()
				EndIf
				TimeCheckpointMonitors()
				UpdateMonitorSaving()
			EndIf
			If szl\Loading = 0
				UpdateZoneColor()
				UpdateMTF()
				UpdateNPCs()
				Update268()
				Update427()
			EndIf
			UpdateItems()
			UpdateParticles()
			
			If szl\Loading <> 0 Then UpdateZoneLoading()
			
			If chs\InfiniteStamina Then me\Stamina = 100.0
			If chs\NoBlink Then me\BlinkTimer = me\BLINKFREQ
			
			me\BlurVolume = Min(CurveValue(0.0, me\BlurVolume, 20.0), 0.95)
			If me\BlurTimer > 0.0
				me\BlurVolume = Max(Min(0.95, me\BlurTimer / 1000.0), me\BlurVolume)
				me\BlurTimer = Max(me\BlurTimer - fps\Factor[0], 0.0)
			EndIf
			
			Local DarkAlpha# = 0.0
			
			If me\Sanity < 0.0
				If me\RestoreSanity Then me\Sanity = Min(me\Sanity + fps\Factor[0], 0.0)
				If me\Sanity < -200.0
					DarkAlpha = Max(Min((-me\Sanity - 200.0) / 700.0, 0.6), DarkAlpha)
					If me\Health > 0
						me\HeartBeatVolume = Min(Abs(me\Sanity + 20.00) / 500.0, 1.0)
						me\HeartBeatRate = Max(70.0 + Abs(me\Sanity + 200.0) / 6.0, me\HeartBeatRate)
					EndIf
				EndIf
			EndIf
			
			If me\EyeStuck > 0.0
				me\BlinkTimer = me\BLINKFREQ
				me\EyeStuck = Max(me\EyeStuck - fps\Factor[0], 0.0)
				
				If me\EyeStuck < 9000.0 Then me\BlurTimer = Max(me\BlurTimer, (9000.0 - me\EyeStuck) * 0.5)
				If me\EyeStuck < 6000.0 Then DarkAlpha = Min(Max(DarkAlpha, (6000.0 - me\EyeStuck) / 5000.0), 1.0)
				If me\EyeStuck < 9000.0 And me\EyeStuck + fps\Factor[0] >= 9000.0 Then CreateMsg(GetLocalString("msg", "eyedrop.tear"))
			EndIf
			
			If me\BlinkTimer < 0.0
				If me\BlinkTimer > -5.0
					DarkAlpha = Max(DarkAlpha, Sin(Abs(me\BlinkTimer * 18.0)))
				ElseIf me\BlinkTimer > -15.0
					DarkAlpha = 1.0
				Else
					DarkAlpha = Max(DarkAlpha, Abs(Sin(me\BlinkTimer * 18.0)))
				EndIf
				
				If me\BlinkTimer <= -20.0
					; ~ Randomizes the frequency of blinking. Scales with difficulty
					Select(SelectedDifficulty\OtherFactors)
						Case EASY
							;[Block]
							me\BLINKFREQ = Rnd(600.0, 700.0)
							;[End Block]
						Case NORMAL
							;[Block]
							me\BLINKFREQ = Rnd(500.0, 600.0)
							;[End Block]
						Case HARD
							;[Block]
							me\BLINKFREQ = Rnd(400.0, 500.0)
							;[End Block]
						Case EXTREME
							;[Block]
							me\BLINKFREQ = Rnd(300.0, 400.0)
							;[End Block]
					End Select
					me\BlinkTimer = me\BLINKFREQ
					If (Not (PlayerRoom\RoomTemplate\RoomID = r_room3_storage And EntityY(me\Collider) =< (-4100.0) * RoomScale)) Then me\BlurTimer = Max(me\BlurTimer - Rnd(50.0, 150.0), 0.0)
				EndIf
				me\BlinkTimer = me\BlinkTimer - fps\Factor[0]
			Else
				me\BlinkTimer = me\BlinkTimer - (fps\Factor[0] * 0.6 * me\BlinkEffect)
				If wi\NightVision = 0 And wi\SCRAMBLE = 0
					If me\EyeIrritation > 0.0 Then me\BlinkTimer = me\BlinkTimer - Min((me\EyeIrritation / 100.0) + 1.0, 4.0) * fps\Factor[0]
				EndIf
			EndIf
			
			me\EyeIrritation = Max(0.0, me\EyeIrritation - fps\Factor[0])
			
			If me\BlinkEffectTimer > 0.0
				me\BlinkEffectTimer = me\BlinkEffectTimer - (fps\Factor[0] / 70.0)
			Else
				me\BlinkEffect = 1.0
			EndIf
			
			me\LightBlink = Max(me\LightBlink - (fps\Factor[0] / 35.0), 0.0)
			If IsBlackOut
				SecondaryLightOn = CurveValue(0.0, SecondaryLightOn, 10.0)
			Else
				If me\LightBlink > 0.0
					SecondaryLightOn = CurveValue(0.0, SecondaryLightOn, 10.0)
				Else
					SecondaryLightOn = CurveValue(1.0, SecondaryLightOn, 10.0)
				EndIf
			EndIf
			
			If I_294\Using Then DarkAlpha = 1.0
			
			If wi\NightVision = 0 Then DarkAlpha = Max((1.0 - SecondaryLightOn) * 0.9, DarkAlpha)
			
			If me\Health = 0
				NullSelectedStuff()
				me\BlurTimer = me\KillAnimTimer * 5.0
					me\KillAnimTimer = me\KillAnimTimer + fps\Factor[0]
					If me\KillAnimTimer >= 400.0 Then MenuOpen = True
				DarkAlpha = Max(DarkAlpha, Min(Abs(400.0), 1.0))
			Else
				If (Not EntityHidden(t\OverlayID[Orl_blood])) Then HideEntity(t\OverlayID[Orl_blood])
				me\KillAnimTimer = 0.0
			EndIf
			
			If me\SelectedEnding <> -1
				MenuOpen = True
				KillSounds()
				cra\EndingTimer = 0.1
			EndIf
			
			If me\FallTimer < 0.0
				NullSelectedStuff()
				me\BlurTimer = Abs(me\FallTimer * 10.0)
				me\FallTimer = me\FallTimer - fps\Factor[0]
				DarkAlpha = Max(DarkAlpha, Min(Abs(me\FallTimer / 400.0), 1.0))
			EndIf
			
			If me\LightFlash > 0.0
				If EntityHidden(t\OverlayID[Orl_Light]) Then ShowEntity(t\OverlayID[Orl_Light])
				EntityAlpha(t\OverlayID[Orl_Light], Max(Min(me\LightFlash + Rnd(-0.2, 0.2), 1.0), 0.0))
				me\LightFlash = Max(me\LightFlash - (fps\Factor[0] / 70.0), 0.0)
			Else
				If (Not EntityHidden(t\OverlayID[Orl_Light])) Then HideEntity(t\OverlayID[Orl_Light])
			EndIf
			
			If (Not (SelectedItem = Null Lor InvOpen Lor OtherOpen <> Null))
				If IsItemInFocus() Then DarkAlpha = Max(DarkAlpha, 0.5)
			EndIf
			
			If SelectedScreen <> Null Lor d_I\SelectedDoor <> Null Then DarkAlpha = Max(DarkAlpha, 0.5)
			
			If DarkAlpha <> 0.0
				If EntityHidden(t\OverlayID[Orl_Dark]) Then ShowEntity(t\OverlayID[Orl_Dark])
				EntityAlpha(t\OverlayID[Orl_Dark], DarkAlpha)
			Else
				If (Not EntityHidden(t\OverlayID[Orl_Dark])) Then HideEntity(t\OverlayID[Orl_Dark])
			EndIf
		EndIf
		
		If fps\Factor[0] = 0.0
			UpdateWorld(0.0)
		Else
			UpdateWorld()
			ManipulateNPCBones()
		EndIf
		
		UpdateWorld2()
		
		UpdateGUI()
		
		If KeyHit(key\INVENTORY)
			If d_I\SelectedDoor = Null And SelectedScreen = Null And (Not I_294\Using) And me\Playable And (Not me\Zombie) And me\VomitTimer >= 0.0 And me\FallTimer >= 0.0 And me\Health > 0 And me\SelectedEnding = -1
				If InvOpen
					StopMouseMovement()
				Else
					mo\DoubleClickSlot = -1
				EndIf
				InvOpen = (Not InvOpen)
				OtherOpen = Null
				SelectedItem = Null
			EndIf
		EndIf
		
		If KeyHit(key\SAVE)
			If SelectedDifficulty\SaveType < SAVE_ON_QUIT
				If CanSave = 0 ; ~ Scripted location
					CreateHintMsg(GetLocalString("save", "failed.now"))
				ElseIf CanSave = 1 ; ~ Endings / Intro location
					CreateHintMsg(GetLocalString("save", "failed.location"))
					If QuickLoadPercent > -1 Then CreateHintMsg(msg\HintTxt + GetLocalString("save", "failed.loading"))
				ElseIf CanSave = 2 ; ~ Triggered SCP-096
					CreateHintMsg(GetLocalString("save", "failed.096"))
				ElseIf as\Timer <= 70.0 * 5.0
					CancelAutoSave()
				ElseIf SelectedDifficulty\SaveType = SAVE_ON_SCREENS
					If SelectedScreen = Null And sc_I\SelectedMonitor = Null
						CreateHintMsg(GetLocalString("save", "failed.screen"))
					Else
						SaveGame(CurrSave\Name) ; ~ Can save at screen
					EndIf
				Else
					SaveGame(CurrSave\Name) ; ~ Can save
				EndIf
			Else
				CreateHintMsg(GetLocalString("save", "disable"))
			EndIf
		ElseIf SelectedDifficulty\SaveType = SAVE_ON_SCREENS And (SelectedScreen <> Null Lor sc_I\SelectedMonitor <> Null)
			If msg\HintTxt = "" Lor msg\HintTimer <= 0.0 Then CreateHintMsg(Format(GetLocalString("save", "save"), key\Name[key\SAVE]))
			If mo\MouseHit2 Then sc_I\SelectedMonitor = Null
		EndIf
		UpdateAutoSave()
		
		If KeyHit(key\CONSOLE)
			If opt\CanOpenConsole
				If ConsoleOpen
					UsedConsole = True
					ResumeSounds()
					StopMouseMovement()
					ShouldDeleteGadgets = True
				Else
					PauseSounds()
				EndIf
				ConsoleOpen = (Not ConsoleOpen)
				FlushKeys()
			EndIf
		EndIf
		
		UpdateMessages()
		UpdateHintMessages()
		UpdateSubtitles()
		
		UpdateConsole()
		
		UpdateQuickLoading()
		
		UpdateAchievementMsg()
		
		If cra\EndingTimer > 0.0
			UpdateEnding()
		Else
			UpdateMenu()
		EndIf
	Wend
	
	; ~ Go out of function immediately if the game has been quit
	If MainMenuOpen Then Return
	
	RenderGame()
	
	CatchErrors("Uncaught: UpdateGame()")
End Function

Global RenderTween#

Function RenderGame%()
	CatchErrors("RenderGame()")
	
	RenderTween = Max(0.0, 1.0 + (fps\Accumulator / TICK_DURATION))
	
	If fps\Factor[0] > 0.0 And PlayerInReachableRoom(False, True) Then RenderSecurityCams()
	
	RenderWorld2(RenderTween)
	
	RenderBlur(me\BlurVolume)
	
	If wa_I\UpdateWaterString <> "" Then RenderWater(wa_I\UpdateWaterString)
	
	RenderGUI()
	
	RenderMessages()
	RenderHintMessages()
	RenderSubtitles()
	
	RenderConsole()
	
	RenderQuickLoading()
	
	RenderAchievementMsg()
	
	If cra\EndingTimer > 0.0
		RenderEnding()
	Else
		RenderMenu()
	EndIf
	
	CatchErrors("Uncaught: RenderGame()")
End Function

Global WireFrameState%

Include "Source Code\Console_Core.bb"

Function ResetNegativeStats%(Revive% = False)
	Local e.Events
	Local i%
	
	me\Bloodloss = 0.0
	
	me\BlurTimer = 0.0
	me\LightFlash = 0.0
	me\LightBlink = 0.0
	me\CameraShake = 0.0
	
	me\DeafTimer = 0.0
	
	me\DeathTimer = 0.0
	
	me\VomitTimer = 0.0
	me\HeartBeatVolume = 0.0
	
	If me\BlinkEffect > 1.0
		me\BlinkEffect = 1.0
		me\BlinkEffectTimer = 0.0
	EndIf
	
	If me\StaminaEffect > 1.0
		me\StaminaEffect = 1.0
		me\StaminaEffectTimer = 0.0
	EndIf
	me\Stamina = 100.0
	
	For i = 0 To 6
		I_1025\State[i] = 0.0
	Next
	
	If I_427\Timer >= 70.0 * 360.0 Then I_427\Timer = 0.0
	I_008\Timer = 0.0
	I_409\Timer = 0.0
	
	If Revive
		ClearCheats()
		
		; ~ If death by SCP-173 or SCP-106, enable GodMode, prevent instant death again -- Salvage
		If n_I\Curr173\Idle = 1
			CreateConsoleMsg(Format(GetLocalString("console", "revive.by"), "SCP-173"))
			chs\GodMode = True
			n_I\Curr173\Idle = 0
		EndIf
		If EntityDistanceSquared(me\Collider, n_I\Curr106\Collider) < 4.0
			CreateConsoleMsg(Format(GetLocalString("console", "revive.by"), "SCP-106"))
			chs\GodMode = True
		EndIf
		If n_I\Curr049 <> Null
			n_I\Curr049\State = 1.0 ; ~ Reset SCP-049
			If EntityDistanceSquared(me\Collider, n_I\Curr049\Collider) < 4.0
				CreateConsoleMsg(Format(GetLocalString("console", "revive.by"), "SCP-049"))
				chs\GodMode = True
			EndIf
		EndIf
		
		me\DropSpeed = -0.1
		me\HeadDropSpeed = 0.0
		me\Shake = 0.0
		me\CurrSpeed = 0.0
		
		me\FallTimer = 0.0
		MenuOpen = False
		
		HideEntity(me\Head)
		ShowEntity(me\Collider)
		
		me\Health = 100
		me\KillAnim = 0
	EndIf
End Function

Type Messages
	Field Txt$
	Field Timer#
	Field DeathMsg$
	Field KeyPadMsg$
	Field KeyPadTimer#
	Field KeyPadInput$
	Field HintTxt$
	Field HintTimer#
	Field HintY#
End Type

Global msg.Messages

Function CreateMsg%(Txt$, Sec# = 6.0)
	If (Not (opt\HUDEnabled And me\ShowHUD)) Then Return
	
	msg\Txt = Txt
	msg\Timer = 70.0 * Sec
End Function

Function UpdateMessages%()
	If (Not (opt\HUDEnabled And me\ShowHUD)) Then Return
	
	If msg\Timer > 0.0
		msg\Timer = msg\Timer - fps\Factor[0]
	Else
		msg\Timer = 0.0 : msg\Txt = ""
	EndIf
End Function

Function RenderMessages%()
	If (Not (opt\HUDEnabled And me\ShowHUD)) Then Return
	
	If msg\Timer > 0.0
		Local Temp%
		
		If (Not (InvOpen Lor OtherOpen <> Null)) Then Temp = ((I_294\Using Lor d_I\SelectedDoor <> Null Lor SelectedScreen <> Null) Lor (SelectedItem <> Null And (SelectedItem\ItemTemplate\TempName = "paper" Lor SelectedItem\ItemTemplate\TempName = "oldpaper")))
		
		Local Temp2% = Min(msg\Timer / 2.0, 255.0)
		
		SetFontEx(fo\FontID[Font_Default])
		Color(Temp2, Temp2, Temp2)
		
		Local PosY%
		
		If (Not Temp)
			PosY = mo\Viewport_Center_Y + (200 * MenuScale)
		Else
			PosY = opt\GraphicHeight * 0.94
		EndIf
		TextEx(mo\Viewport_Center_X, PosY, msg\Txt, True)
	EndIf
	Color(255, 255, 255)
	If opt\ShowFPS
		Local CoordEx% = 20 * MenuScale
		
		SetFontEx(fo\FontID[Font_Console])
		TextEx(CoordEx, CoordEx, "FPS: " + fps\FPS)
		SetFontEx(fo\FontID[Font_Default])
	EndIf
End Function

Function CreateHintMsg%(Txt$, Sec# = 6.0)
	If (Not (opt\HUDEnabled And me\ShowHUD)) Then Return
	
	msg\HintTxt = Txt
	msg\HintTimer = 70.0 * Sec
End Function

Function UpdateHintMessages%()
	If (Not (opt\HUDEnabled And me\ShowHUD)) Then Return
	
	Local Scale# = opt\GraphicHeight / 768.0
	Local Width = StringWidth(msg\HintTxt) + (20 * Scale)
	Local Height% = 30 * Scale
	
	If msg\HintTxt <> ""
		If msg\HintTimer > 0.0
			If msg\HintY < Height
				msg\HintY = Min(msg\HintY + (2.0 * fps\Factor[0]), Height)
			Else
				msg\HintY = Height
			EndIf
			msg\HintTimer = msg\HintTimer - fps\Factor[0]
		Else
			If msg\HintY > 0.0
				msg\HintY = Max(msg\HintY - (2.0 * fps\Factor[0]), 0.0)
			Else
				msg\HintTxt = ""
				msg\HintTimer = 0.0
				msg\HintY = 0.0
			EndIf
		EndIf
	EndIf
End Function

Function RenderHintMessages%()
	If (Not (opt\HUDEnabled And me\ShowHUD)) Then Return
	
	Local Scale# = opt\GraphicHeight / 768.0
	Local Width% = StringWidth(msg\HintTxt) + (20 * Scale)
	Local Height% = 30 * Scale
	Local x% = mo\Viewport_Center_X - (Width / 2)
	Local y% = (-Height) + msg\HintY
	
	If msg\HintTxt <> ""
		RenderFrame(x, y, Width, Height)
		Color(255, 255, 255)
		SetFontEx(fo\FontID[Font_Default])
		TextEx(mo\Viewport_Center_X, y + (Height / 2), msg\HintTxt, True, True)
	EndIf
End Function

; ~ Fog Constants
;[Block]
Const FogColorLCZ$ = "005005005"
Const FogColorHCZ$ = "007002002"
Const FogColorEZ$ = "007007012"
Const FogColorStorageTunnels$ = "002007000"
Const FogColorOutside$ = "255255255"
Const FogColorDimension_1499$ = "096097104"
Const FogColorPD$ = "000000000"
Const FogColorPDTrench$ = "038055047"
Const FogColorForest$ = "098133162"
Const FogColorForestChase$ = "032044054"
;[End Block]

Global CurrFogColor$
Global CurrFogColorR#, CurrFogColorG#, CurrFogColorB#

; ~ Ambient Color Constants
;[Block]
Const AmbientColorLCZ$ = "030030030"
Const AmbientColorHCZ$ = "030023023"
Const AmbientColorEZ$ = "023023030"
;[End Block]

Global CurrAmbientColor$
Global CurrAmbientColorR#, CurrAmbientColorG#, CurrAmbientColorB#

Const ZoneColorChangeSpeed# = 50.0

Function SetZoneColor%(FogColor$, AmbientColor$ = AmbientColorLCZ)
	CurrFogColor = FogColor
	CurrAmbientColor = AmbientColor
End Function

Function UpdateZoneColor%()
	Local e.Events
	Local i%
	Local RID% = PlayerRoom\RoomTemplate\RoomID
	Local PlayerPosY# = EntityY(me\Collider, True)
	
	CurrFogColor$ = ""
	CurrAmbientColor$ = ""
	
	CameraFogMode(Camera, 1)
	CameraFogRange(Camera, 0.1 * LightVolume, opt\CameraFogFar * LightVolume)
	If opt\DebugMode = 1
		CameraRange(Camera, 0.01, 100.0)
	Else
		CameraRange(Camera, 0.01, opt\CameraFogFar * LightVolume * 1.2)
	EndIf
	; ~ Handle room-specific settings
	If RID = r_room3_storage And PlayerPosY < (-4100.0) * RoomScale
		SetZoneColor(FogColorStorageTunnels)
	ElseIf IsPlayerOutsideFacility()
		SetZoneColor(FogColorOutside)
		opt\CameraFogFar = 60.0
		CameraFogRange(Camera, 5.0, 60.0)
		CameraRange(Camera, 0.01, 72.0)
	ElseIf RID = r_cont1_173_intro
		opt\CameraFogFar = 45.0
		CameraFogRange(Camera, 5.0, 45.0)
		CameraRange(Camera, 0.01, 54.0)
	ElseIf RID = r_dimension_1499
		SetZoneColor(FogColorDimension_1499)
		opt\CameraFogFar = 80.0
		LightVolume = 1.0
		CameraFogRange(Camera, 40.0, 80.0)
		CameraRange(Camera, 0.01, 96.0)
	ElseIf RID = r_dimension_106
		For e.Events = Each Events
			If e\EventID = e_dimension_106
				LightVolume = 1.0
				If e\EventState2 = PD_TrenchesRoom Lor e\EventState2 = PD_TowerRoom
					SetZoneColor(FogColorPDTrench)
				ElseIf e\EventState2 = PD_FakeTunnelRoom
					SetZoneColor(FogColorHCZ, AmbientColorHCZ)
				Else
					If e\EventState2 = PD_Labyrinth Then opt\CameraFogFar = 3.5
					SetZoneColor(FogColorPD)
				EndIf
				Exit
			EndIf
		Next
	ElseIf (RID = r_room2_mt And (PlayerPosY >= 8.0 And PlayerPosY <= 12.0)) Lor (RID = r_cont2_409 And PlayerPosY < (-3728.0) * RoomScale) Lor (RID = r_cont1_895 And PlayerPosY < (-1200.0) * RoomScale)
		SetZoneColor(FogColorHCZ, AmbientColorHCZ)
	ElseIf forest_event <> Null
		If PlayerRoom = forest_event\room
			If forest_event\EventState = 1.0
				SetZoneColor(FogColorForest)
				If forest_event\room\NPC[0] <> Null
					If forest_event\room\NPC[0]\State >= 2.0 Then SetZoneColor(FogColorForestChase)
				EndIf
				opt\CameraFogFar = 8.0
				LightVolume = 1.0
				CameraFogRange(Camera, 0.1, 8.0)
				CameraRange(Camera, 0.01, 9.6)
			EndIf
		EndIf
	EndIf
	
	; ~ If unset, use standard settings based on zone
	If CurrFogColor = ""
		Select(szl\CurrentZone)
			Case LCZ
				;[Block]
				SetZoneColor(FogColorLCZ, AmbientColorLCZ)
				;[End Block]
			Case HCZ
				;[Block]
				SetZoneColor(FogColorHCZ, AmbientColorHCZ)
				;[End Block]
			Case EZ
				;[Block]
				SetZoneColor(FogColorEZ, AmbientColorEZ)
				;[End Block]
		End Select
	EndIf
	
	CurrFogColorR = CurveValue(Left(CurrFogColor, 3), CurrFogColorR, ZoneColorChangeSpeed)
	CurrFogColorG = CurveValue(Mid(CurrFogColor, 4, 3), CurrFogColorG, ZoneColorChangeSpeed)
	CurrFogColorB = CurveValue(Right(CurrFogColor, 3), CurrFogColorB, ZoneColorChangeSpeed)
	
	CameraFogColor(Camera, CurrFogColorR, CurrFogColorG, CurrFogColorB)
	CameraClsColor(Camera, CurrFogColorR, CurrFogColorG, CurrFogColorB)
	
	CurrAmbientColorR = CurveValue(Left(CurrAmbientColor, 3), CurrAmbientColorR, ZoneColorChangeSpeed)
	CurrAmbientColorG = CurveValue(Mid(CurrAmbientColor, 4, 3), CurrAmbientColorG, ZoneColorChangeSpeed)
	CurrAmbientColorB = CurveValue(Right(CurrAmbientColor, 3), CurrAmbientColorB, ZoneColorChangeSpeed)
	
	Local CurrR#, CurrG#, CurrB#
	
	If wi\NightVision > 0
		CurrR = CurrAmbientColorR * 6.0 : CurrG = CurrAmbientColorG * 6.0 : CurrB = CurrAmbientColorB * 6.0
		AmbientLightRooms(CurrR / 3.0, CurrG / 3.0, CurrB / 3.0)
	ElseIf wi\SCRAMBLE > 0
		CurrR = CurrAmbientColorR * 2.0 : CurrG = CurrAmbientColorG * 2.0 : CurrB = CurrAmbientColorB * 2.0
		AmbientLightRooms(CurrR / 3.0, CurrG / 3.0, CurrB / 3.0)
	Else
		AmbientLightRooms(CurrAmbientColorR / 5.0, CurrAmbientColorG / 5.0, CurrAmbientColorB / 5.0)
		CurrR = CurrAmbientColorR : CurrG = CurrAmbientColorG : CurrB = CurrAmbientColorB
		If RID = r_cont1_173_intro
			CurrR = CurrAmbientColorR * 1.5 : CurrG = CurrAmbientColorG * 1.5 : CurrB = CurrAmbientColorB * 1.5
		ElseIf forest_event <> Null
			If PlayerRoom = forest_event\room
				If forest_event\EventState = 1.0 Then CurrR = 200.0 : CurrG = 200.0 : CurrB = 200.0
			EndIf
		EndIf
	EndIf
	AmbientLight(CurrR, CurrG, CurrB)
End Function

Function NullSelectedStuff%()
	InvOpen = False
	I_294\Using = False
	d_I\SelectedDoor = Null
	SelectedScreen = Null
	sc_I\SelectedMonitor = Null
	SelectedItem = Null
	OtherOpen = Null
	d_I\ClosestButton = 0
	GrabbedEntity = 0
End Function

Global IsUsingRadio%

Global GrabbedEntity%

Global RadioState#[9]
Global RadioState2%[9]
Global RadioState3%[10]

Global DrawHandIcon%
Global DrawArrowIcon%[4]

Global InvOpen%

Include "Source Code\GUI_Core.bb"
Include "Source Code\Inventory_Core.bb"
Include "Source Code\Tasks_Core.bb"

Type InGameMenu
	Field AchievementsMenu% = 0
	Field QuitMenu% = 0
	Field OptionsMenu% = 0
End Type

Global igm.InGameMenu

; ~ Menu Tab Options Constants
;[Block]
Const MenuTab_Options_Graphics% = 2
Const MenuTab_Options_Audio% = 3
Const MenuTab_Options_Controls% = 4
Const MenuTab_Options_Advanced% = 5
;[End Block]

Global MenuOpen%

Function UpdateMenu%()
	CatchErrors("UpdateMenu()")
	
	Local r.Rooms, sc.SecurityCams
	Local x%, y%, z%, Width%, Height%, i%
	
	If MenuOpen
		If (Not IsPlayerOutsideFacility()) And me\Health > 0
			If me\StopHidingTimer = 0.0
				If (EntityDistanceSquared(n_I\Curr173\Collider, me\Collider) < 0.64 And n_I\Curr173\Idle < 2) Lor EntityDistanceSquared(n_I\Curr106\Collider, me\Collider) < 0.64 Lor (n_I\Curr049 <> Null And EntityDistanceSquared(n_I\Curr049\Collider, me\Collider) < 0.64) And (n_I\Curr066 <> Null And EntityDistanceSquared(n_I\Curr066\Collider, me\Collider) < 0.64) And (n_I\Curr096 <> Null And EntityDistanceSquared(n_I\Curr096\Collider, me\Collider) < 0.64) Then me\StopHidingTimer = 1.0
			ElseIf me\StopHidingTimer < Rnd(120.0, 240.0)
				me\StopHidingTimer = me\StopHidingTimer + 1
			Else
				me\StopHidingTimer = 0.0
				PlaySound_Strict(LoadTempSound("SFX\General\STOPHIDING.ogg"))
				CreateHintMsg(GetLocalString("msg", "stophiding"))
				ShouldDeleteGadgets = True
				MenuOpen = False
				Return
			EndIf
		EndIf
		
		InvOpen = False
		
		Width = ImageWidth(t\ImageID[Img_Pause_Menu])
		Height = ImageHeight(t\ImageID[Img_Pause_Menu])
		x = mo\Viewport_Center_X - (Width / 2)
		y = mo\Viewport_Center_Y - (Height / 2)
		
		x = x + (132 * MenuScale)
		y = y + (122 * MenuScale)
		
		If (Not mo\MouseDown1) Then OnSliderID = 0
		
		If igm\AchievementsMenu <= 0 And igm\OptionsMenu > 0 And igm\QuitMenu <= 0
			If igm\OptionsMenu = 1
				If UpdateMenuButton(x, y, 430 * MenuScale, 60 * MenuScale, GetLocalString("options", "grap"), Font_Default_Big) Then ChangeOptionTab(MenuTab_Options_Graphics, False)
				If UpdateMenuButton(x, y + (75 * MenuScale), 430 * MenuScale, 60 * MenuScale, GetLocalString("options", "audio"), Font_Default_Big) Then ChangeOptionTab(MenuTab_Options_Audio, False)
				If UpdateMenuButton(x, y + (150 * MenuScale), 430 * MenuScale, 60 * MenuScale, GetLocalString("options", "ctrl"), Font_Default_Big) Then ChangeOptionTab(MenuTab_Options_Controls, False)
				If UpdateMenuButton(x, y + (225 * MenuScale), 430 * MenuScale, 60 * MenuScale, GetLocalString("options", "avc"), Font_Default_Big) Then ChangeOptionTab(MenuTab_Options_Advanced, False)
				
				If UpdateMenuButton(x + (101 * MenuScale), y + (455 * MenuScale), 230 * MenuScale, 60 * MenuScale, GetLocalString("menu", "back"), Font_Default_Big)
					igm\AchievementsMenu = 0
					igm\OptionsMenu = 0
					igm\QuitMenu = 0
					ResetInput()
					
					ShouldDeleteGadgets = True
				EndIf
			Else
				If UpdateMenuButton(x + (101 * MenuScale), y + (455 * MenuScale), 230 * MenuScale, 60 * MenuScale, GetLocalString("menu", "back"), Font_Default_Big)
					igm\AchievementsMenu = 0
					igm\OptionsMenu = 1
					igm\QuitMenu = 0
					ResetInput()
					SaveOptionsINI()
					
					AntiAlias(opt\AntiAliasing)
					TextureLodBias(opt\TextureDetailsLevel)
					TextureAnisotropic(opt\AnisotropicLevel)
					ShouldDeleteGadgets = True
				EndIf
				
				x = x + (270 * MenuScale)
				
				Select(igm\OptionsMenu)
					Case MenuTab_Options_Graphics
						;[Block]
						opt\BumpEnabled = UpdateMenuTick(x, y, opt\BumpEnabled, True)
						
						y = y + (30 * MenuScale)
						
						opt\VSync = UpdateMenuTick(x, y, opt\VSync)
						
						y = y + (30 * MenuScale)
						
						opt\AntiAliasing = UpdateMenuTick(x, y, opt\AntiAliasing, opt\DisplayMode <> 0)
						
						y = y + (30 * MenuScale)
						
						opt\AdvancedRoomLights = UpdateMenuTick(x, y, opt\AdvancedRoomLights)
						
						y = y + (40 * MenuScale)
						
						opt\ScreenGamma = UpdateMenuSlideBar(x, y, 100 * MenuScale, opt\ScreenGamma * 50.0, 1) / 50.0
						
						y = y + (45 * MenuScale)
						
						opt\ParticleAmount = UpdateMenuSlider3(x, y, 100 * MenuScale, opt\ParticleAmount, 2, GetLocalString("options", "min"), GetLocalString("options", "red"), GetLocalString("options", "full"))
						
						y = y + (45 * MenuScale)
						
						opt\TextureDetails = UpdateMenuSlider5(x, y, 100 * MenuScale, opt\TextureDetails, 3, "0.8", "0.4", "0.0", "-0.4", "-0.8")
						Select(opt\TextureDetails)
							Case 0
								;[Block]
								opt\TextureDetailsLevel = 0.8
								;[End Block]
							Case 1
								;[Block]
								opt\TextureDetailsLevel = 0.4
								;[End Block]
							Case 2
								;[Block]
								opt\TextureDetailsLevel = 0.0
								;[End Block]
							Case 3
								;[Block]
								opt\TextureDetailsLevel = -0.4
								;[End Block]
							Case 4
								;[Block]
								opt\TextureDetailsLevel = -0.8
								;[End Block]
						End Select
						TextureLodBias(opt\TextureDetailsLevel)
						
						y = y + (35 * MenuScale)
						
						opt\SaveTexturesInVRAM = UpdateMenuTick(x, y, opt\SaveTexturesInVRAM, True)
						
						y = y + (40 * MenuScale)
						
						opt\CurrFOV = UpdateMenuSlideBar(x, y, 100 * MenuScale, opt\CurrFOV * 2.0, 4) / 2.0
						opt\FOV = opt\CurrFOV + 40
						CameraZoom(Camera, Min(1.0 + (me\CurrCameraZoom / 400.0), 1.1) / Tan((2.0 * ATan(Tan((opt\FOV) / 2.0) * opt\RealGraphicWidth / opt\RealGraphicHeight)) / 2.0))
						
						y = y + (45 * MenuScale)
						
						opt\Anisotropic = UpdateMenuSlider5(x, y, 100 * MenuScale, opt\Anisotropic, 5, "Trilinear", "2x", "4x", "8x", "16x")
						Select(opt\Anisotropic)
							Case 0
								;[Block]
								opt\AnisotropicLevel = 0
								;[End Block]
							Case 1
								;[Block]
								opt\AnisotropicLevel = 2
								;[End Block]
							Case 2
								;[Block]
								opt\AnisotropicLevel = 4
								;[End Block]
							Case 3
								;[Block]
								opt\AnisotropicLevel = 8
								;[End Block]
							Case 4
								;[Block]
								opt\AnisotropicLevel = 16
								;[End Block]
						End Select
						TextureAnisotropic(opt\AnisotropicLevel)
						
						y = y + (35 * MenuScale)
						
						opt\Atmosphere = UpdateMenuTick(x, y, opt\Atmosphere, True)
						
						y = y + (45 * MenuScale)
						
						opt\SecurityCamRenderInterval = UpdateMenuSlider5(x, y, 100 * MenuScale, opt\SecurityCamRenderInterval, 17, "24.0", "18.0", "12.0", "6.0", "0.0")
						Select(opt\SecurityCamRenderInterval)
							Case 0
								;[Block]
								opt\SecurityCamRenderIntervalLevel = 24.0
								;[End Block]
							Case 1
								;[Block]
								opt\SecurityCamRenderIntervalLevel = 18.0
								;[End Block]
							Case 2
								;[Block]
								opt\SecurityCamRenderIntervalLevel = 12.0
								;[End Block]
							Case 3
								;[Block]
								opt\SecurityCamRenderIntervalLevel = 6.0
								;[End Block]
							Case 4
								;[Block]
								opt\SecurityCamRenderIntervalLevel = 0.0
								;[End Block]
						End Select
						For sc.SecurityCams = Each SecurityCams
							If sc\Screen Then sc\RenderInterval = opt\SecurityCamRenderIntervalLevel
						Next
						;[End Block]
					Case MenuTab_Options_Audio
						;[Block]
						opt\PrevMasterVolume = UpdateMenuSlideBar(x, y, 100 * MenuScale, opt\MasterVolume * 100.0, 1) / 100.0
						If (Not me\Deaf) Then opt\MasterVolume = opt\PrevMasterVolume
						
						y = y + (40 * MenuScale)
						
						opt\MusicVolume = UpdateMenuSlideBar(x, y, 100 * MenuScale, opt\MusicVolume * 100.0, 2) / 100.0
						
						y = y + (40 * MenuScale)
						
						opt\SFXVolume = UpdateMenuSlideBar(x, y, 100 * MenuScale, opt\SFXVolume * 100.0, 3) / 100.0
						
						y = y + (40 * MenuScale)
						
						opt\VoiceVolume = UpdateMenuSlideBar(x, y, 100 * MenuScale, opt\VoiceVolume * 100.0, 18) / 100.0
						
						y = y + (40 * MenuScale)
						
						opt\EnableSFXRelease = UpdateMenuTick(x, y, opt\EnableSFXRelease, True)
						
						y = y + (30 * MenuScale)
						
						Local PrevEnableUserTracks% = opt\UserTrackMode
						
						If UpdateMenuButton(x, y, 20 * MenuScale, 20 * MenuScale, ">")
							If opt\UserTrackMode < 2
								opt\UserTrackMode = opt\UserTrackMode + 1
							Else
								opt\UserTrackMode = 0
							EndIf
						EndIf
						
						If opt\UserTrackMode > 0
							UpdateMenuButton(x - (270 * MenuScale), y + (30 * MenuScale), 210 * MenuScale, 30 * MenuScale, GetLocalString("options", "scantracks"), Font_Default, False, True)
							y = y + (40 * MenuScale)
						EndIf
						
						y = y + (30 * MenuScale)
						
						Local PrevEnableSubtitles% = opt\EnableSubtitles
						Local PrevOverrideSubColor% = opt\OverrideSubColor
						
						opt\EnableSubtitles = UpdateMenuTick(x, y, opt\EnableSubtitles)
						If PrevEnableSubtitles <> opt\EnableSubtitles
							If opt\EnableSubtitles Then ClearSubtitles()
						EndIf
						
						If opt\EnableSubtitles
							y = y + (30 * MenuScale)
							
							opt\OverrideSubColor = UpdateMenuTick(x, y, opt\OverrideSubColor)
						EndIf
						
						If opt\EnableSubtitles And opt\OverrideSubColor
							y = y + (35 * MenuScale)
							
							UpdateMenuPalette(x - (43 * MenuScale), y + (5 * MenuScale))
							
							y = y + (30 * MenuScale)
							
							opt\SubColorR = Min(UpdateMenuInputBox(x - (115 * MenuScale), y, 40 * MenuScale, 20 * MenuScale, Str(Int(opt\SubColorR)), Font_Default, 14, 3), 255.0)
							
							y = y + (30 * MenuScale)
							
							opt\SubColorG = Min(UpdateMenuInputBox(x - (115 * MenuScale), y, 40 * MenuScale, 20 * MenuScale, Str(Int(opt\SubColorG)), Font_Default, 15, 3), 255.0)
							
							y = y + (30 * MenuScale)
							
							opt\SubColorB = Min(UpdateMenuInputBox(x - (115 * MenuScale), y, 40 * MenuScale, 20 * MenuScale, Str(Int(opt\SubColorB)), Font_Default, 16, 3), 255.0)
						EndIf
						If PrevEnableSubtitles Lor PrevOverrideSubColor Lor PrevEnableUserTracks <> 1 Then ShouldDeleteGadgets = (PrevEnableSubtitles <> opt\EnableSubtitles) Lor (PrevOverrideSubColor <> opt\OverrideSubColor) Lor PrevEnableUserTracks <> opt\UserTrackMode
						;[End Block]
					Case MenuTab_Options_Controls
						;[Block]
						opt\MouseSensitivity = (UpdateMenuSlideBar(x, y, 100 * MenuScale, (opt\MouseSensitivity + 0.5) * 100.0, 1) / 100.0) - 0.5
						
						y = y + (40 * MenuScale)
						
						opt\InvertMouseX = UpdateMenuTick(x, y, opt\InvertMouseX)
						
						y = y + (40 * MenuScale)
						
						opt\InvertMouseY = UpdateMenuTick(x, y, opt\InvertMouseY)
						
						y = y + (40 * MenuScale)
						
						opt\MouseSmoothing = UpdateMenuSlideBar(x, y, 100 * MenuScale, (opt\MouseSmoothing) * 50.0, 2) / 50.0
						
						y = y + (80 * MenuScale)
						
						UpdateMenuInputBox(x, y, 110 * MenuScale, 20 * MenuScale, key\Name[Min(key\MOVEMENT_UP, 210.0)], Font_Default, 3)
						
						y = y + (20 * MenuScale)
						
						UpdateMenuInputBox(x, y, 110 * MenuScale, 20 * MenuScale, key\Name[Min(key\MOVEMENT_LEFT, 210.0)], Font_Default, 4)
						
						y = y + (20 * MenuScale)
						
						UpdateMenuInputBox(x, y, 110 * MenuScale, 20 * MenuScale, key\Name[Min(key\MOVEMENT_DOWN, 210.0)], Font_Default, 5)
						
						y = y + (20 * MenuScale)
						
						UpdateMenuInputBox(x, y, 110 * MenuScale, 20 * MenuScale, key\Name[Min(key\MOVEMENT_RIGHT, 210.0)], Font_Default, 6)
						
						y = y + (20 * MenuScale)
						
						UpdateMenuInputBox(x, y, 110 * MenuScale, 20 * MenuScale, key\Name[Min(key\SPRINT, 210.0)], Font_Default, 7)
						
						y = y + (20 * MenuScale)
						
						UpdateMenuInputBox(x, y, 110 * MenuScale, 20 * MenuScale, key\Name[Min(key\CROUCH, 210.0)], Font_Default, 8)
						
						y = y + (20 * MenuScale)
						
						UpdateMenuInputBox(x, y, 110 * MenuScale, 20 * MenuScale, key\Name[Min(key\BLINK, 210.0)], Font_Default, 9)
						
						y = y + (20 * MenuScale)
						
						UpdateMenuInputBox(x, y, 110 * MenuScale, 20 * MenuScale, key\Name[Min(key\INVENTORY, 210.0)], Font_Default, 10)
						
						y = y + (20 * MenuScale)
						
						UpdateMenuInputBox(x, y, 110 * MenuScale, 20 * MenuScale, key\Name[Min(key\SAVE, 210.0)], Font_Default, 11)
						
						y = y + (20 * MenuScale)
						
						UpdateMenuInputBox(x, y, 110 * MenuScale, 20 * MenuScale, key\Name[Min(key\SCREENSHOT, 210.0)], Font_Default, 13)
						
						If opt\CanOpenConsole
							y = y + (20 * MenuScale)
							
							UpdateMenuInputBox(x, y, 110 * MenuScale, 20 * MenuScale, key\Name[Min(key\CONSOLE, 210.0)], Font_Default, 12)
						EndIf
						
						Local TempKey%
						
						For i = 0 To 227
							If KeyHit(i)
								TempKey = i
								Exit
							EndIf
						Next
						If TempKey <> 0
							Select(SelectedInputBox)
								Case 3
									;[Block]
									key\MOVEMENT_UP = TempKey
									;[End Block]
								Case 4
									;[Block]
									key\MOVEMENT_LEFT = TempKey
									;[End Block]
								Case 5
									;[Block]
									key\MOVEMENT_DOWN = TempKey
									;[End Block]
								Case 6
									;[Block]
									key\MOVEMENT_RIGHT = TempKey
									;[End Block]
								Case 7
									;[Block]
									key\SPRINT = TempKey
									;[End Block]
								Case 8
									;[Block]
									key\CROUCH = TempKey
									;[End Block]
								Case 9
									;[Block]
									key\BLINK = TempKey
									;[End Block]
								Case 10
									;[Block]
									key\INVENTORY = TempKey
									;[End Block]
								Case 11
									;[Block]
									key\SAVE = TempKey
									;[End Block]
								Case 12
									;[Block]
									key\CONSOLE = TempKey
									;[End Block]
								Case 13
									;[Block]
									key\SCREENSHOT = TempKey
									;[End Block]
							End Select
							SelectedInputBox = 0
						EndIf
						;[End Block]
					Case MenuTab_Options_Advanced
						;[Block]
						opt\HUDEnabled = UpdateMenuTick(x, y, opt\HUDEnabled)
						
						y = y + (30 * MenuScale)
						
						Local PrevCanOpenConsole% = opt\CanOpenConsole
						
						opt\CanOpenConsole = UpdateMenuTick(x, y, opt\CanOpenConsole)
						
						If PrevCanOpenConsole Then ShouldDeleteGadgets = (PrevCanOpenConsole <> opt\CanOpenConsole)
						
						y = y + (30 * MenuScale)
						
						If opt\CanOpenConsole Then opt\ConsoleOpening = UpdateMenuTick(x, y, opt\ConsoleOpening)
						
						y = y + (30 * MenuScale)
						
						opt\AchvMsgEnabled = UpdateMenuTick(x, y, opt\AchvMsgEnabled)
						
						y = y + (30 * MenuScale)
						
						opt\AutoSaveEnabled = UpdateMenuTick(x, y, opt\AutoSaveEnabled, SelectedDifficulty\SaveType <> SAVE_ANYWHERE)
						
						y = y + (30 * MenuScale)
						
						opt\TextShadow = UpdateMenuTick(x, y, opt\TextShadow)
						
						y = y + (30 * MenuScale)
						
						opt\ShowFPS = UpdateMenuTick(x, y, opt\ShowFPS)
						
						y = y + (30 * MenuScale)
						
						Local PrevCurrFrameLimit% = opt\CurrFrameLimit > 0.0
						
						If UpdateMenuTick(x, y, opt\CurrFrameLimit > 0.0)
							opt\CurrFrameLimit = UpdateMenuSlideBar(x - (120 * MenuScale), y + (40 * MenuScale), 100 * MenuScale, opt\CurrFrameLimit * 99.0, 1) / 99.0
							opt\CurrFrameLimit = Max(opt\CurrFrameLimit, 0.01)
							opt\FrameLimit = 19 + (opt\CurrFrameLimit * 100.0)
						Else
							opt\CurrFrameLimit = 0.0
							opt\FrameLimit = 0
						EndIf
						
						If PrevCurrFrameLimit Then ShouldDeleteGadgets = (PrevCurrFrameLimit <> opt\CurrFrameLimit)
						
						y = y + (80 * MenuScale)
						
						opt\SmoothBars = UpdateMenuTick(x, y, opt\SmoothBars)
						
						y = y + (30 * MenuScale)
						
						opt\PlayStartup = UpdateMenuTick(x, y, opt\PlayStartup)
						
						y = y + (30 * MenuScale)
						
						opt\LauncherEnabled = UpdateMenuTick(x, y, opt\LauncherEnabled)
						
						y = y + (40 * MenuScale)
						
						UpdateMenuButton(x - (270 * MenuScale), y, 195 * MenuScale, 30 * MenuScale, GetLocalString("options", "reset"), Font_Default, False, True)
						;[End Block]
				End Select
			EndIf
		ElseIf igm\AchievementsMenu <= 0 And igm\OptionsMenu <= 0 And igm\QuitMenu > 0
			Local QuitButton% = 85
			
			If SelectedDifficulty\SaveType = SAVE_ON_QUIT Lor SelectedDifficulty\SaveType = SAVE_ANYWHERE
				If CanSave = 3
					QuitButton = 160
					If UpdateMenuButton(x, y + (85 * MenuScale), 430 * MenuScale, 60 * MenuScale, GetLocalString("menu", "savequit"), Font_Default_Big)
						me\DropSpeed = 0.0
						SaveGame(CurrSave\Name)
						NullGame()
						CurrSave = Null
						ResetInput()
						Return
					EndIf
				EndIf
			EndIf
			
			If UpdateMenuButton(x, y + (QuitButton * MenuScale), 430 * MenuScale, 60 * MenuScale, GetLocalString("menu", "quit"), Font_Default_Big)
				NullGame()
				CurrSave = Null
				ResetInput()
				Return
			EndIf
			
			If UpdateMenuButton(x + (101 * MenuScale), y + 385 * MenuScale, 230 * MenuScale, 60 * MenuScale, GetLocalString("menu", "back"), Font_Default_Big)
				igm\AchievementsMenu = 0
				igm\OptionsMenu = 0
				igm\QuitMenu = 0
				ResetInput()
				ShouldDeleteGadgets = True
			EndIf
		ElseIf igm\AchievementsMenu > 0 And igm\OptionsMenu <= 0 And igm\QuitMenu <= 0
			If UpdateMenuButton(x + (101 * MenuScale), y + 345 * MenuScale, 230 * MenuScale, 60 * MenuScale, GetLocalString("menu", "back"), Font_Default_Big)
				igm\AchievementsMenu = 0
				igm\OptionsMenu = 0
				igm\QuitMenu = 0
				ResetInput()
				ShouldDeleteGadgets = True
			EndIf
			
			If igm\AchievementsMenu > 0
				If igm\AchievementsMenu <= Floor(Float(MaxAchievements - 1) / 12.0)
					If UpdateMenuButton(x + (341 * MenuScale), y + (345 * MenuScale), 60 * MenuScale, 60 * MenuScale, ">", Font_Default_Big)
						igm\AchievementsMenu = igm\AchievementsMenu + 1
						ShouldDeleteGadgets = True
					EndIf
				Else
					UpdateMenuButton(x + (341 * MenuScale), y + (345 * MenuScale), 60 * MenuScale, 60 * MenuScale, ">", Font_Default_Big, False, True)
				EndIf
				If igm\AchievementsMenu > 1
					If UpdateMenuButton(x + (31 * MenuScale), y + (345 * MenuScale), 60 * MenuScale, 60 * MenuScale, "<", Font_Default_Big)
						igm\AchievementsMenu = igm\AchievementsMenu - 1
						ShouldDeleteGadgets = True
					EndIf
				Else
					UpdateMenuButton(x + (31 * MenuScale), y + (345 * MenuScale), 60 * MenuScale, 60 * MenuScale, "<", Font_Default_Big, False, True)
				EndIf
			EndIf
		Else
			y = y + (10 * MenuScale)
			
			If me\Health > 0 Lor me\SelectedEnding <> - 1
				y = y + (75 * MenuScale)
				
				If UpdateMenuButton(x, y, 430 * MenuScale, 60 * MenuScale, GetLocalString("menu", "resume"), Font_Default_Big, True)
					ResumeSounds()
					StopMouseMovement()
					DeleteMenuGadgets()
					MenuOpen = False
					Return
				EndIf
				
				y = y + (75 * MenuScale)
				
				If SelectedDifficulty\SaveType <> NO_SAVES
					If GameSaved
						If UpdateMenuButton(x, y, 430 * MenuScale, 60 * MenuScale, GetLocalString("menu", "load"), Font_Default_Big)
							RenderLoading(0, GetLocalString("loading", "files"))
							
							KillSounds()
							LoadGameQuick(CurrSave\Name)
							
							MoveMouse(mo\Viewport_Center_X, mo\Viewport_Center_Y)
							HidePointer()
							
							UpdateRender()
							
							For r.Rooms = Each Rooms
								x = Abs(EntityX(me\Collider) - EntityX(r\OBJ))
								z = Abs(EntityZ(me\Collider) - EntityZ(r\OBJ))
								
								If x < 12.0 And z < 12.0
									CurrMapGrid\Found[Floor(EntityX(r\OBJ) / RoomSpacing) + (Floor(EntityZ(r\OBJ) / RoomSpacing) * MapGridSize)] = Max(CurrMapGrid\Found[Floor(EntityX(r\OBJ) / RoomSpacing) + (Floor(EntityZ(r\OBJ) / RoomSpacing) * MapGridSize)], 1.0)
									If x < 4.0 And z < 4.0
										If Abs(EntityY(me\Collider) - EntityY(r\OBJ)) < 1.5 Then PlayerRoom = r
										CurrMapGrid\Found[Floor(EntityX(r\OBJ) / RoomSpacing) + (Floor(EntityZ(r\OBJ) / RoomSpacing) * MapGridSize)] = MapGrid_Tile
									EndIf
								EndIf
							Next
							
							RenderLoading(100)
							
							me\DropSpeed = 0.0
							
							UpdateWorld(0.0)
							
							fps\Factor[0] = 0.0
							fps\PrevTime = MilliSecs()
							
							ResetInput()
							MenuOpen = False
							Return
						EndIf
					Else
						UpdateMenuButton(x, y, 430 * MenuScale, 60 * MenuScale, GetLocalString("menu", "load"), Font_Default_Big, False, True)
					EndIf
					y = y + (75 * MenuScale)
				EndIf
				
				If UpdateMenuButton(x, y, 430 * MenuScale, 60 * MenuScale, GetLocalString("menu", "achievements"), Font_Default_Big)
					igm\AchievementsMenu = 1
					ShouldDeleteGadgets = True
				EndIf
				
				y = y + (75 * MenuScale)
				
				If UpdateMenuButton(x, y, 430 * MenuScale, 60 * MenuScale, GetLocalString("menu", "options"), Font_Default_Big)
					igm\OptionsMenu = 1
					ShouldDeleteGadgets = True
				EndIf
				
				y = y + (75 * MenuScale)
				
				If UpdateMenuButton(x, y, 430 * MenuScale, 60 * MenuScale, GetLocalString("menu", "quit"), Font_Default_Big)
					igm\QuitMenu = 1
					ShouldDeleteGadgets = True
				EndIf
			Else
				y = y + (75 * MenuScale)
				
				If SelectedDifficulty\SaveType <> NO_SAVES
					If GameSaved
						If UpdateMenuButton(x, y, 430 * MenuScale, 60 * MenuScale, GetLocalString("menu", "load"), Font_Default_Big)
							RenderLoading(0, GetLocalString("loading", "files"))
							
							KillSounds()
							LoadGameQuick(CurrSave\Name)
							
							MoveMouse(mo\Viewport_Center_X, mo\Viewport_Center_Y)
							HidePointer()
							
							UpdateRender()
							
							For r.Rooms = Each Rooms
								x = Abs(EntityX(me\Collider) - EntityX(r\OBJ))
								z = Abs(EntityZ(me\Collider) - EntityZ(r\OBJ))
								
								If x < 12.0 And z < 12.0
									CurrMapGrid\Found[Floor(EntityX(r\OBJ) / RoomSpacing) + (Floor(EntityZ(r\OBJ) / RoomSpacing) * MapGridSize)] = Max(CurrMapGrid\Found[Floor(EntityX(r\OBJ) / RoomSpacing) + (Floor(EntityZ(r\OBJ) / RoomSpacing) * MapGridSize)], 1.0)
									If x < 4.0 And z < 4.0
										If Abs(EntityY(me\Collider) - EntityY(r\OBJ)) < 1.5 Then PlayerRoom = r
										CurrMapGrid\Found[Floor(EntityX(r\OBJ) / RoomSpacing) + (Floor(EntityZ(r\OBJ) / RoomSpacing) * MapGridSize)] = MapGrid_Tile
									EndIf
								EndIf
							Next
							
							RenderLoading(100)
							
							me\DropSpeed = 0.0
							
							UpdateWorld(0.0)
							
							fps\Factor[0] = 0.0
							fps\PrevTime = MilliSecs()
							
							ResetInput()
							MenuOpen = False
							Return
						EndIf
					Else
						UpdateMenuButton(x, y, 430 * MenuScale, 60 * MenuScale, GetLocalString("menu", "load"), Font_Default_Big, False, True)
					EndIf
					y = y + (75 * MenuScale)
				EndIf
				If UpdateMenuButton(x, y, 430 * MenuScale, 60 * MenuScale, GetLocalString("menu", "quitmenu"), Font_Default_Big)
					NullGame()
					CurrSave = Null
					ResetInput()
					Return
				EndIf
			EndIf
		EndIf
	EndIf
	
	CatchErrors("Uncaught: UpdateMenu()")
End Function

Function RenderMenu%()
	CatchErrors("RenderMenu()")
	
	Local x%, y%, Width%, Height%, i%
	Local TempStr$
	
	If (Not InFocus()) ; ~ Game is out of focus then pause the game
		MenuOpen = True
		PauseSounds()
		Delay(1000) ; ~ Reduce the CPU take while game is not in focus
	EndIf
	If MenuOpen
		Width = ImageWidth(t\ImageID[Img_Pause_Menu])
		Height = ImageHeight(t\ImageID[Img_Pause_Menu])
		x = mo\Viewport_Center_X - (Width / 2)
		y = mo\Viewport_Center_Y - (Height / 2)
		
		If (Not OnPalette)
			ShowPointer()
		Else
			HidePointer()
		EndIf
		
		DrawBlock(t\ImageID[Img_Pause_Menu], x, y)
		
		Color(255, 255, 255)
		
		If igm\AchievementsMenu > 0
			TempStr = GetLocalString("menu", "achievements")
		ElseIf igm\OptionsMenu > 0
			If igm\OptionsMenu = 1
				TempStr = GetLocalString("menu", "options")
			ElseIf igm\OptionsMenu = MenuTab_Options_Graphics
				TempStr = GetLocalString("options", "grap")
			ElseIf igm\OptionsMenu = MenuTab_Options_Audio
				TempStr = GetLocalString("options", "audio")
			ElseIf igm\OptionsMenu = MenuTab_Options_Controls
				TempStr = GetLocalString("options", "ctrl")
			ElseIf igm\OptionsMenu = MenuTab_Options_Advanced
				TempStr = GetLocalString("options", "avc")
			EndIf
		ElseIf igm\QuitMenu > 0
			TempStr = GetLocalString("menu", "quit?")
		ElseIf me\Health > 0 Lor me\SelectedEnding <> -1
			TempStr = GetLocalString("menu", "paused")
		Else
			TempStr = GetLocalString("menu", "died")
		EndIf
		SetFontEx(fo\FontID[Font_Default_Big])
		TextEx(x + (Width / 2) + (47 * MenuScale), y + (48 * MenuScale), TempStr, True, True)
		SetFontEx(fo\FontID[Font_Default])
		
		x = x + (132 * MenuScale)
		y = y + (122 * MenuScale)
		
		Local AchvXIMG% = x + (22 * MenuScale)
		Local Scale# = opt\GraphicHeight / 768.0
		Local SeparationConst% = 76 * Scale
		
		If igm\AchievementsMenu <= 0 And igm\OptionsMenu > 0 And igm\QuitMenu <= 0
			If igm\OptionsMenu > 1
				Local tX# = mo\Viewport_Center_X + (Width / 2)
				Local tY# = y
				Local tW# = 400.0 * MenuScale
				Local tH# = 150.0 * MenuScale
				
				Color(255, 255, 255)
				Select(igm\OptionsMenu)
					Case MenuTab_Options_Graphics
						;[Block]
						Color(100, 100, 100)
						TextEx(x, y + (5 * MenuScale), GetLocalString("options", "bump"))
						If MouseOn(x + (270 * MenuScale), y, 20 * MenuScale, 20 * MenuScale) And OnSliderID = 0 Then RenderOptionsTooltip(tX, tY, tW, tH, Tooltip_BumpMapping)
						
						y = y + (30 * MenuScale)
						
						Color(255, 255, 255)
						TextEx(x, y + (5 * MenuScale), GetLocalString("options", "vsync"))
						If MouseOn(x + (270 * MenuScale), y, 20 * MenuScale, 20 * MenuScale) And OnSliderID = 0 Then RenderOptionsTooltip(tX, tY, tW, tH, Tooltip_VSync)
						
						y = y + (30 * MenuScale)
						
						Color(255 - (155 * (opt\DisplayMode <> 0)), 255 - (155 * (opt\DisplayMode <> 0)), 255 - (155 * (opt\DisplayMode <> 0)))
						TextEx(x, y + (5 * MenuScale), GetLocalString("options", "antialias"))
						If MouseOn(x + (270 * MenuScale), y, 20 * MenuScale, 20 * MenuScale) And OnSliderID = 0 Then RenderOptionsTooltip(tX, tY, tW, tH, Tooltip_AntiAliasing)
						
						y = y + (30 * MenuScale)
						
						Color(255, 255, 255)
						TextEx(x, y + (5 * MenuScale), GetLocalString("options", "lights"))
						If MouseOn(x + (270 * MenuScale), y, 20 * MenuScale, 20 * MenuScale) And OnSliderID = 0 Then RenderOptionsTooltip(tX, tY, tW, tH, Tooltip_RoomLights)
						
						y = y + (40 * MenuScale)
						
						TextEx(x, y + (5 * MenuScale), GetLocalString("options", "gamma"))
						If (MouseOn(x + (270 * MenuScale), y, 114 * MenuScale, 20 * MenuScale) And OnSliderID = 0) Lor OnSliderID = 1 Then RenderOptionsTooltip(tX, tY, tW, tH, Tooltip_ScreenGamma, opt\ScreenGamma)
						
						y = y + (45 * MenuScale)
						
						TextEx(x, y, GetLocalString("options", "particle"))
						If (MouseOn(x + (270 * MenuScale), y - (8 * MenuScale), 114 * MenuScale, 18 * MenuScale) And OnSliderID = 0) Lor OnSliderID = 2 Then RenderOptionsTooltip(tX, tY, tW, tH, Tooltip_ParticleAmount, opt\ParticleAmount)
						
						y = y + (45 * MenuScale)
						
						TextEx(x, y, GetLocalString("options", "lod"))
						If (MouseOn(x + (270 * MenuScale), y - (8 * MenuScale), 114 * MenuScale, 18 * MenuScale) And OnSliderID = 0) Lor OnSliderID = 3 Then RenderOptionsTooltip(tX, tY, tW, tH + 100 * MenuScale, Tooltip_TextureLODBias)
						
						y = y + (35 * MenuScale)
						
						Color(100, 100, 100)
						TextEx(x, y + (5 * MenuScale), GetLocalString("options", "vram"))
						If MouseOn(x + (270 * MenuScale), y, 20 * MenuScale, 20 * MenuScale) And OnSliderID = 0 Then RenderOptionsTooltip(tX, tY, tW, tH, Tooltip_SaveTexturesInVRAM)
						
						y = y + (40 * MenuScale)
						
						Color(255, 255, 255)
						TextEx(x, y + (5 * MenuScale), GetLocalString("options", "fov"))
						Color(255, 255, 0)
						If (MouseOn(x + (270 * MenuScale), y, 114 * MenuScale, 20 * MenuScale) And OnSliderID = 0) Lor OnSliderID = 4 Then RenderOptionsTooltip(tX, tY, tW, tH, Tooltip_FOV)
						
						y = y + (45 * MenuScale)
						
						Color(255, 255, 255)
						TextEx(x, y, GetLocalString("options", "filter"))
						If (MouseOn(x + (270 * MenuScale), y - (8 * MenuScale), 114 * MenuScale, 18 * MenuScale) And OnSliderID = 0) Lor OnSliderID = 5 Then RenderOptionsTooltip(tX, tY, tW, tH, Tooltip_AnisotropicFiltering)
						
						y = y + (35 * MenuScale)
						
						Color(100, 100, 100)
						If opt\Atmosphere
							TempStr = GetLocalString("options", "atmo.bright")
						Else
							TempStr = GetLocalString("options", "atmo.dark")
						EndIf
						TextEx(x, y + (5 * MenuScale), GetLocalString("options", "atmo") + TempStr)
						If MouseOn(x + (270 * MenuScale), y, 20 * MenuScale, 20 * MenuScale) And OnSliderID = 0 Then RenderOptionsTooltip(tX, tY, tW, tH, Tooltip_Atmosphere)
						
						y = y + (45 * MenuScale)
						
						Color(255, 255, 255)
						TextEx(x, y, GetLocalString("options", "screnderinterval"))
						If (MouseOn(x + (270 * MenuScale), y - (8 * MenuScale), 114 * MenuScale, 18 * MenuScale) And OnSliderID = 0) Lor OnSliderID = 17 Then RenderOptionsTooltip(tX, tY, tW, tH, Tooltip_SecurityCamRenderInterval)
						;[End Block]
					Case MenuTab_Options_Audio
						;[Block]
						TextEx(x, y + (5 * MenuScale), GetLocalString("options", "mastervolume"))
						If (MouseOn(x + (250 * MenuScale), y, 114 * MenuScale, 20 * MenuScale) And OnSliderID = 0) Lor OnSliderID = 1 Then RenderOptionsTooltip(tX, tY, tW, tH, Tooltip_MasterVolume, opt\PrevMasterVolume)
						
						y = y + (40 * MenuScale)
						
						TextEx(x, y + (5 * MenuScale), GetLocalString("options", "musicvolume"))
						If (MouseOn(x + (250 * MenuScale), y, 114 * MenuScale, 20 * MenuScale) And OnSliderID = 0) Lor OnSliderID = 2 Then RenderOptionsTooltip(tX, tY, tW, tH, Tooltip_MusicVolume, opt\MusicVolume)
						
						y = y + (40 * MenuScale)
						
						TextEx(x, y + (5 * MenuScale), GetLocalString("options", "soundvolume"))
						If (MouseOn(x + (250 * MenuScale), y, 114 * MenuScale, 20 * MenuScale) And OnSliderID = 0) Lor OnSliderID = 3 Then RenderOptionsTooltip(tX, tY, tW, tH, Tooltip_SoundVolume, opt\SFXVolume)
						
						y = y + (40 * MenuScale)
						
						TextEx(x, y + (5 * MenuScale), GetLocalString("options", "voicevolume"))
						If (MouseOn(x + (250 * MenuScale), y, 114 * MenuScale, 20 * MenuScale) And OnSliderID = 0) Lor OnSliderID = 18 Then RenderOptionsTooltip(tX, tY, tW, tH, Tooltip_VoiceVolume, opt\VoiceVolume)
						
						y = y + (40 * MenuScale)
						
						Color(100, 100, 100)
						TextEx(x, y + (5 * MenuScale), GetLocalString("options", "autorelease"))
						If MouseOn(x + (270 * MenuScale), y, 20 * MenuScale, 20 * MenuScale) And OnSliderID = 0 Then RenderOptionsTooltip(tX, tY, tW, tH + 220 * MenuScale, Tooltip_SoundAutoRelease)
						
						y = y + (30 * MenuScale)
						
						Color(255, 255, 255)
						TextEx(x, y + (5 * MenuScale), GetLocalString("options", "trackmode"))
						If opt\UserTrackMode = 0
							TempStr = GetLocalString("options", "track.disabled")
						ElseIf opt\UserTrackMode = 1
							TempStr = GetLocalString("options", "track.repeat")
						ElseIf opt\UserTrackMode = 2
							TempStr = GetLocalString("options", "track.random")
						EndIf
						TextEx(x + (310 * MenuScale), y + (5 * MenuScale), TempStr)
						If MouseOn(x + (270 * MenuScale), y, 20 * MenuScale, 20 * MenuScale) And OnSliderID = 0 Then RenderOptionsTooltip(tX, tY, tW, tH, Tooltip_UserTracksMode)
						If opt\UserTrackMode > 0
							If MouseOn(x, y + (30 * MenuScale), 210 * MenuScale, 30 * MenuScale) And OnSliderID = 0 Then RenderOptionsTooltip(tX, tY, tW, tH, Tooltip_UserTrackScan)
							y = y + (40 * MenuScale)
						EndIf
						
						y = y + (30 * MenuScale)
						
						Color(255, 255, 255)
						TextEx(x, y + (5 * MenuScale), GetLocalString("options", "subtitles"))
						If MouseOn(x + (270 * MenuScale), y, 20 * MenuScale, 20 * MenuScale) Then RenderOptionsTooltip(tX, tY, tW, tH, Tooltip_Subtitles)
						
						If opt\EnableSubtitles
							y = y + (30 * MenuScale)
							
							TextEx(x, y + (5 * MenuScale), GetLocalString("options", "subtitles.color"))
							
							y = y + (5 * MenuScale)
							
							If MouseOn(x + (210 * MenuScale), y, 147 * MenuScale, 147 * MenuScale) Then RenderOptionsTooltip(tX, tY, tW, tH, Tooltip_SubtitlesColor)
							
							If opt\OverrideSubColor
								y = y + (60 * MenuScale)
								
								TextEx(x, y + (5 * MenuScale), GetLocalString("options", "subtitles.color.red"))
								If MouseOn(x + (105 * MenuScale), y, 40 * MenuScale, 20 * MenuScale) Then RenderOptionsTooltip(tX, tY, tW, tH, Tooltip_SubtitlesColor)
								
								y = y + (30 * MenuScale)
								
								TextEx(x, y + (5 * MenuScale), GetLocalString("options", "subtitles.color.green"))
								If MouseOn(x + (105 * MenuScale), y, 40 * MenuScale, 20 * MenuScale) Then RenderOptionsTooltip(tX, tY, tW, tH, Tooltip_SubtitlesColor)
								
								y = y + (30 * MenuScale)
								
								TextEx(x, y + (5 * MenuScale), GetLocalString("options", "subtitles.color.blue"))
								If MouseOn(x + (105 * MenuScale), y, 40 * MenuScale, 20 * MenuScale) Then RenderOptionsTooltip(tX, tY, tW, tH, Tooltip_SubtitlesColor)
							EndIf
						EndIf
						;[End Block]
					Case MenuTab_Options_Controls
						;[Block]
						TextEx(x, y + (5 * MenuScale), GetLocalString("options", "mousesensitive"))
						If (MouseOn(x + (270 * MenuScale), y, 114 * MenuScale, 20 * MenuScale) And OnSliderID = 0) Lor OnSliderID = 1 Then RenderOptionsTooltip(tX, tY, tW, tH, Tooltip_MouseSensitivity, opt\MouseSensitivity)
						
						y = y + (40 * MenuScale)
						
						TextEx(x, y + (5 * MenuScale), GetLocalString("options", "invertx"))
						If MouseOn(x + (270 * MenuScale), y, 20 * MenuScale, 20 * MenuScale) And OnSliderID = 0 Then RenderOptionsTooltip(tX, tY, tW, tH, Tooltip_MouseInvertX)
						
						y = y + (40 * MenuScale)
						
						TextEx(x, y + (5 * MenuScale), GetLocalString("options", "inverty"))
						If MouseOn(x + (270 * MenuScale), y, 20 * MenuScale, 20 * MenuScale) And OnSliderID = 0 Then RenderOptionsTooltip(tX, tY, tW, tH, Tooltip_MouseInvertY)
						
						y = y + (40 * MenuScale)
						
						TextEx(x, y + (5 * MenuScale), GetLocalString("options", "mousesmooth"))
						If (MouseOn(x + (270 * MenuScale), y, 114 * MenuScale, 20 * MenuScale) And OnSliderID = 0) Lor OnSliderID = 2 Then RenderOptionsTooltip(tX, tY, tW, tH, Tooltip_MouseSmoothing, opt\MouseSmoothing)
						
						y = y + (40 * MenuScale)
						
						TextEx(x, y + (5 * MenuScale), GetLocalString("menu", "controlconfig"))
						
						y = y + (40 * MenuScale)
						
						TextEx(x, y + (5 * MenuScale), GetLocalString("options", "key.forward"))
						
						y = y + (20 * MenuScale)
						
						TextEx(x, y + (5 * MenuScale), GetLocalString("options", "key.left"))
						
						y = y + (20 * MenuScale)
						
						TextEx(x, y + (5 * MenuScale), GetLocalString("options", "key.backward"))
						
						y = y + (20 * MenuScale)
						
						TextEx(x, y + (5 * MenuScale), GetLocalString("options", "key.right"))
						
						y = y + (20 * MenuScale)
						
						TextEx(x, y + (5 * MenuScale), GetLocalString("options", "key.sprint"))
						
						y = y + (20 * MenuScale)
						
						TextEx(x, y + (5 * MenuScale), GetLocalString("options", "key.crouch"))
						
						y = y + (20 * MenuScale)
						
						TextEx(x, y + (5 * MenuScale), GetLocalString("options", "key.blink"))
						
						y = y + (20 * MenuScale)
						
						TextEx(x, y + (5 * MenuScale), GetLocalString("options", "key.inv"))
						
						y = y + (20 * MenuScale)
						
						TextEx(x, y + (5 * MenuScale), GetLocalString("options", "key.save"))
						
						y = y + (20 * MenuScale)
						
						TextEx(x, y + (5 * MenuScale), GetLocalString("options", "key.screenshot"))
						
						If opt\CanOpenConsole
							y = y + (20 * MenuScale)
							
							TextEx(x, y + (5 * MenuScale), GetLocalString("options", "key.console"))
						EndIf
						
						If MouseOn(x, y - ((180 + (20 * opt\CanOpenConsole)) * MenuScale), 380 * MenuScale, ((200 + (20 * opt\CanOpenConsole)) * MenuScale)) Then RenderOptionsTooltip(tX, tY, tW, tH, Tooltip_ControlConfiguration)
						;[End Block]
					Case MenuTab_Options_Advanced
						;[Block]
						TextEx(x, y + (5 * MenuScale), GetLocalString("options", "hud"))
						If MouseOn(x + (270 * MenuScale), y, 20 * MenuScale, 20 * MenuScale) And OnSliderID = 0 Then RenderOptionsTooltip(tX, tY, tW, tH, Tooltip_HUD)
						
						y = y + (30 * MenuScale)
						
						TextEx(x, y + (5 * MenuScale), GetLocalString("options", "console"))
						If MouseOn(x + (270 * MenuScale), y, 20 * MenuScale, 20 * MenuScale) And OnSliderID = 0 Then RenderOptionsTooltip(tX, tY, tW, tH, Tooltip_Console)
						
						y = y + (30 * MenuScale)
						
						If opt\CanOpenConsole
							TextEx(x, y + (5 * MenuScale), GetLocalString("options", "error"))
							If MouseOn(x + (270 * MenuScale), y, 20 * MenuScale, 20 * MenuScale) And OnSliderID = 0 Then RenderOptionsTooltip(tX, tY, tW, tH, Tooltip_ConsoleOnError)
						EndIf
						
						y = y + (30 * MenuScale)
						
						TextEx(x, y + (5 * MenuScale), GetLocalString("options", "achipop"))
						If MouseOn(x + (270 * MenuScale), y, 20 * MenuScale, 20 * MenuScale) And OnSliderID = 0 Then RenderOptionsTooltip(tX, tY, tW, tH, Tooltip_AchievementPopups)
						
						y = y + (30 * MenuScale)
						
						Color(255 - (155 * (SelectedDifficulty\SaveType <> SAVE_ANYWHERE)), 255 - (155 * (SelectedDifficulty\SaveType <> SAVE_ANYWHERE)), 255 - (155 * (SelectedDifficulty\SaveType <> SAVE_ANYWHERE)))
						TextEx(x, y + (5 * MenuScale), GetLocalString("options", "save"))
						If MouseOn(x + (270 * MenuScale), y, 20 * MenuScale, 20 * MenuScale) And OnSliderID = 0 Then RenderOptionsTooltip(tX, tY, tW, tH, Tooltip_AutoSave)
						
						y = y + (30 * MenuScale)
						
						Color(255, 255, 255)
						TextEx(x, y + (5 * MenuScale), GetLocalString("options", "txtshadow"))
						If MouseOn(x + (270 * MenuScale), y, 20 * MenuScale, 20 * MenuScale) And OnSliderID = 0 Then RenderOptionsTooltip(tX, tY, tW, tH, Tooltip_TextShadow)
						
						y = y + (30 * MenuScale)
						
						TextEx(x, y + (5 * MenuScale), GetLocalString("options", "fps"))
						If MouseOn(x + (270 * MenuScale), y, 20 * MenuScale, 20 * MenuScale) And OnSliderID = 0 Then RenderOptionsTooltip(tX, tY, tW, tH, Tooltip_FPS)
						
						y = y + (30 * MenuScale)
						
						TextEx(x, y + (5 * MenuScale), GetLocalString("options", "frame"))
						If MouseOn(x + (270 * MenuScale), y, 20 * MenuScale, 20 * MenuScale) And OnSliderID = 0 Then RenderOptionsTooltip(tX, tY, tW, tH, Tooltip_FrameLimit, opt\FrameLimit)
						If opt\CurrFrameLimit > 0.0
							Color(255, 255, 0)
							TextEx(x, y + (45 * MenuScale), opt\FrameLimit + " FPS")
							If (MouseOn(x + (150 * MenuScale), y + (40 * MenuScale), 114 * MenuScale, 20 * MenuScale) And OnSliderID = 0) Lor OnSliderID = 1 Then RenderOptionsTooltip(tX, tY, tW, tH, Tooltip_FrameLimit, opt\FrameLimit)
						EndIf
						
						y = y + (80 * MenuScale)
						
						Color(255, 255, 255)
						TextEx(x, y + (5 * MenuScale), GetLocalString("options", "bar"))
						If MouseOn(x + (270 * MenuScale), y, 20 * MenuScale, 20 * MenuScale) Then RenderOptionsTooltip(tX, tY, tW, tH, Tooltip_SmoothBars)
						
						y = y + (30 * MenuScale)
						
						TextEx(x, y + (5 * MenuScale), GetLocalString("options", "startvideo"))
						If MouseOn(x + (270 * MenuScale), y, 20 * MenuScale, 20 * MenuScale) Then RenderOptionsTooltip(tX, tY, tW, tH, Tooltip_StartupVideos)
						
						y = y + (30 * MenuScale)
						
						TextEx(x, y + (5 * MenuScale), GetLocalString("options", "launcher"))
						If MouseOn(x + (270 * MenuScale), y, 20 * MenuScale, 20 * MenuScale) Then RenderOptionsTooltip(tX, tY, tW, tH, Tooltip_Launcher)
						
						y = y + (40 * MenuScale)
						
						If MouseOn(x, y, 195 * MenuScale, 30 * MenuScale) Then RenderOptionsTooltip(tX, tY, tW, tH, Tooltip_ResetOptions)
						;[End Block]
				End Select
			EndIf
		ElseIf igm\AchievementsMenu <= 0 And igm\OptionsMenu <= 0 And igm\QuitMenu > 0
			; ~ Just save this line, ok?
		ElseIf igm\AchievementsMenu > 0 And igm\OptionsMenu <= 0 And igm\QuitMenu <= 0
			If igm\AchievementsMenu > 0
				For i = 0 To 11
					If i + ((igm\AchievementsMenu - 1) * 12) < MaxAchievements
						RenderAchvIMG(AchvXIMG, y + ((i / 4) * 120 * MenuScale), i + ((igm\AchievementsMenu - 1) * 12))
					Else
						Exit
					EndIf
				Next
				For i = 0 To 11
					If i + ((igm\AchievementsMenu - 1) * 12) < MaxAchievements
						If MouseOn(AchvXIMG + ((i Mod 4) * SeparationConst), y + ((i / 4) * 120 * MenuScale), 64 * Scale, 64 * Scale)
							AchievementTooltip(i + ((igm\AchievementsMenu - 1) * 12))
							Exit
						EndIf
					Else
						Exit
					EndIf
				Next
			EndIf
		Else
			SetFontEx(fo\FontID[Font_Default])
			TextEx(x, y, GetLocalString("menu", "new.diff") + SelectedDifficulty\Name)
			If CurrSave = Null
				TempStr = GetLocalString("menu", "dataredacted")
			Else
				TempStr = ConvertToUTF8(CurrSave\Name)
			EndIf
			TextEx(x, y + (20 * MenuScale), Format(GetLocalString("menu", "save"), TempStr))
			
			If SelectedCustomMap = Null
				TempStr = GetLocalString("menu", "new.seed") + RandomSeed
			Else
				If Len(ConvertToUTF8(SelectedCustomMap\Name)) > 15
					TempStr = GetLocalString("menu", "new.map") + Left(ConvertToUTF8(SelectedCustomMap\Name), 14) + "..."
				Else
					TempStr = GetLocalString("menu", "new.map") + ConvertToUTF8(SelectedCustomMap\Name)
				EndIf
			EndIf
			TextEx(x, y + (40 * MenuScale), TempStr)
			
			If me\Health = 0 And me\SelectedEnding = -1
				y = y + (175 * MenuScale)
				If SelectedDifficulty\SaveType <> NO_SAVES
					y = y + (75 * MenuScale)
				EndIf
				SetFontEx(fo\FontID[Font_Default])
				RowText(msg\DeathMsg, x, y, 430 * MenuScale, 600 * MenuScale)
			EndIf
		EndIf
		
		RenderMenuButtons()
		RenderMenuPalettes()
		RenderMenuTicks()
		RenderMenuInputBoxes()
		RenderMenuSlideBars()
		RenderMenuSliders()
		
		RenderCursor()
	EndIf
	
	SetFontEx(fo\FontID[Font_Default])
	
	CatchErrors("Uncaught: RenderMenu()")
End Function

; ~ Endings ID Constants
;[Block]
Const Ending_A1% = 0
Const Ending_A2% = 1
Const Ending_B1% = 2
Const Ending_B2% = 3
;[End Block]

Function UpdateEnding%()
	Local x%, y%, Width%, Height%, i%
	
	fps\Factor[0] = 0.0
	
	StopBreathSound() : me\Stamina = 100.0
	
	GiveAchievement(Achv055)
	If ((Not UsedConsole) Lor opt\DebugMode) And SelectedCustomMap = Null
		GiveAchievement(AchvConsole)
		If SelectedDifficulty\Name = "Keter" Lor SelectedDifficulty\Name = "Apollyon"
			GiveAchievement(AchvKeter)
			SaveAchievementsFile()
		EndIf
	EndIf
	
	ShouldPlay = MUS_NULL
	
	If cra\EndingTimer = 0.1
		PlaySound_Strict(LoadTempSound("SFX\Ending\Ending" + (me\SelectedEnding + 1) + ".ogg"), True)
		cra\EndingTimer = 1.0
	EndIf
	If cra\EndingTimer = 1.0
		If cra\EndingScreen = 0
			cra\EndingScreen = LoadImage_Strict("GFX\Menu\Images\Ending_Screen.png")
			cra\EndingScreen = ScaleImage2(cra\EndingScreen, MenuScale, MenuScale)
			ShouldPlay = MUS_MENU_ENDING
			opt\CurrMusicVolume = opt\MusicVolume
			StopStream_Strict(MusicCHN) : MusicCHN = 0
			MusicCHN = StreamSound_Strict("SFX\Music\" + Music[MUS_MENU_ENDING] + ".ogg", opt\CurrMusicVolume * opt\MasterVolume, 0)
			NowPlaying = ShouldPlay
			PlaySound_Strict(LightSFX)
		EndIf
;		cra\EndingScreen = 2.0
;	ElseIf cra\EndingTimer = 2.0
		
		If igm\AchievementsMenu =< 0
			Width = ImageWidth(t\ImageID[Img_Pause_Menu])
			Height = ImageHeight(t\ImageID[Img_Pause_Menu])
			x = mo\Viewport_Center_X - (Width / 2)
			y = mo\Viewport_Center_Y - (Height / 2)
			x = x + (132 * MenuScale)
			y = y + (432 * MenuScale)
			
			If UpdateMenuButton(x, y, 430 * MenuScale, 60 * MenuScale, GetLocalString("menu", "achievements"), Font_Default_Big)
				igm\AchievementsMenu = 1
				ShouldDeleteGadgets = True
			EndIf
			
			y = y + 75 * MenuScale
			
			If UpdateMenuButton(x, y, 430 * MenuScale, 60 * MenuScale, GetLocalString("menu", "mainmenu"), Font_Default_Big) Then LoadCredits()
		Else
			ShouldPlay = MUS_MENU_ENDING
			UpdateMenu()
		EndIf
	ElseIf cra\EndingTimer > 2.0
		ShouldPlay = MUS_MENU_CREDITS
		UpdateCredits()
	EndIf
	
End Function

Function RenderEnding%()
	ShowPointer()
	
	Local itt.ItemTemplates, r.Rooms
	Local x%, y%, Width%, Height%, i%
	
;	Select(me\SelectedEnding
;		Case Ending_A1, Ending_B2
;			;[Block]
;			ClsColor(Max(255.0 + (cra\EndingTimer) * 2.8, 0.0), Max(255.0 + (cra\EndingTimer) * 2.8, 0.0), Max(255.0 + (cra\EndingTimer) * 2.8, 0.0))
;			;[End Block]
;		Default
;			;[Block]
;			ClsColor(0, 0, 0)
;			;[End Block]
;	End Select
	
	ClsColor(0, 0, 0)
	
	Cls()
	
	If cra\EndingTimer = 2.0
		DrawBlock(cra\EndingScreen, mo\Viewport_Center_X - (400 * MenuScale), mo\Viewport_Center_Y - (400 * MenuScale))
		
		Width = ImageWidth(t\ImageID[Img_Pause_Menu])
		Height = ImageHeight(t\ImageID[Img_Pause_Menu])
		x = mo\Viewport_Center_X - (Width / 2)
		y = mo\Viewport_Center_Y - (Height / 2)
		
		DrawBlock(t\ImageID[Img_Pause_Menu], x, y)
		
		Color(255, 255, 255)
		SetFontEx(fo\FontID[Font_Default_Big])
		TextEx(x + (Width / 2) + (47 * MenuScale), y + (48 * MenuScale), GetLocalString("menu", "end"), True, True)
		SetFontEx(fo\FontID[Font_Default])
		
		If igm\AchievementsMenu =< 0
			x = x + (132 * MenuScale)
			y = y + (122 * MenuScale)
			
			Local RoomAmount% = 0, RoomsFound% = 0
			
			For r.Rooms = Each Rooms
				Local RID% = r\RoomTemplate\RoomID
				
				If RID <> r_cont1_173_intro And RID <> r_gate_a And RID <> r_gate_b And RID <> r_dimension_106 And RID <> r_dimension_1499
					RoomAmount = RoomAmount + 1
					RoomsFound = RoomsFound + r\Found
				EndIf
			Next
			
			Local DocAmount% = 0, DocsFound% = 0
			
			For itt.ItemTemplates = Each ItemTemplates
				If itt\TempName = "paper"
					DocAmount = DocAmount + 1
					DocsFound = DocsFound + itt\Found
				EndIf
			Next
			
			Local SCPsEncountered% = 1
			
			For i = Achv005 To Achv1499
				SCPsEncountered = SCPsEncountered + achv\Achievement[i]
			Next
			
			Local AchievementsUnlocked% = 0
			
			For i = 0 To MaxAchievements - 1
				AchievementsUnlocked = AchievementsUnlocked + achv\Achievement[i]
			Next
			
			Local EscapeSeconds% = EscapeTimer Mod 60
			Local EscapeMinutes% = Floor(EscapeTimer / 60)
			Local EscapeHours% = Floor(EscapeMinutes / 60)
			
			EscapeMinutes = EscapeMinutes - (EscapeHours * 60)
			
			TextEx(x, y, Format(GetLocalString("menu", "end.scps"), SCPsEncountered))
			TextEx(x, y + (20 * MenuScale), Format(Format(GetLocalString("menu", "end.achi"), AchievementsUnlocked, "{0}"), MaxAchievements, "{1}"))
			TextEx(x, y + (40 * MenuScale), Format(Format(GetLocalString("menu", "end.room"), RoomsFound, "{0}"), RoomAmount, "{1}"))
			TextEx(x, y + (60 * MenuScale), Format(Format(GetLocalString("menu", "end.doc"), DocsFound, "{0}"), DocAmount, "{1}"))
			TextEx(x, y + (80 * MenuScale), Format(GetLocalString("menu", "end.914"), me\RefinedItems))
			TextEx(x, y + (100 * MenuScale), Format(Format(Format(GetLocalString("menu", "end.escape"), EscapeHours, "{0}"), EscapeMinutes, "{1}"), EscapeSeconds, "{2}"))
		Else
			RenderMenu()
		EndIf
	ElseIf cra\EndingTimer > 2.0
		RenderCredits()
	EndIf
	
	RenderMenuButtons()
	
	RenderCursor()
	
	SetFontEx(fo\FontID[Font_Default])
End Function

Type CreditsLine
	Field Txt$
	Field ID%
	Field Stay%
End Type

Function LoadCredits%()
	Local i%
	
	ShouldPlay = MUS_MENU_CREDITS
	NowPlaying = ShouldPlay
	For i = 0 To 8 Step 2
		If TempSounds[i] <> 0 Then FreeSound_Strict(TempSounds[i]) : TempSounds[i] = 0
		If TempSounds[i + 1] <> 0 Then FreeSound_Strict(TempSounds[i + 1]) : TempSounds[i + 1] = 0
	Next
	StopStream_Strict(MusicCHN) : MusicCHN = 0
	MusicCHN = StreamSound_Strict("SFX\Music\" + Music[NowPlaying] + ".ogg", 0.0, Mode)
	SetStreamVolume_Strict(MusicCHN, opt\MusicVolume * opt\MasterVolume)
	cra\EndingTimer = 3.0
	ShouldDeleteGadgets = True
	ResetInput()
	
	If cra\CreditsScreen = 0
		cra\CreditsScreen = LoadImage_Strict("GFX\Menu\Images\Credits_Screen.png")
		cra\CreditsScreen = ScaleImage2(cra\CreditsScreen, MenuScale, MenuScale)
	EndIf
	
	InitCredits()
	
End Function

Function InitCredits%()
	Local cl.CreditsLine
	Local File% = OpenFile_Strict(lang\LanguagePath + "Credits.txt")
	Local l$
	
	fo\FontID[Font_Credits] = LoadFont_Strict(FontsPath + GetFileLocalString(FontsFile, "Credits", "File"), GetFileLocalString(FontsFile, "Credits", "Size"))
	fo\FontID[Font_Credits_Big] = LoadFont_Strict(FontsPath + GetFileLocalString(FontsFile, "Credits_Big", "File"), GetFileLocalString(FontsFile, "Credits_Big", "Size"))
	
	Repeat
		l = ReadLine(File)
		cl.CreditsLine = New CreditsLine
		cl\Txt = l
	Until Eof(File)
	
	Delete First CreditsLine
	cra\CreditsTimer = 0.0
End Function

Function UpdateCredits%()
	Local cl.CreditsLine, LastCreditLine.CreditsLine
	Local Credits_Y#
	Local ID%
	Local EndLinesAmount%
	
	ID = 0
	EndLinesAmount = 0
	LastCreditLine = Null
	
	cra\CreditsTextTimer = cra\CreditsTextTimer - (fps\Factor[1] / 1.2)
	
	Credits_Y# = ((cra\CreditsTextTimer) / 2) + (opt\GraphicHeight + 10.0)
	
	For cl.CreditsLine = Each CreditsLine
		cl\ID = ID
		If Left(cl\Txt, 1) = "/" Then LastCreditLine = Before(cl)
		If LastCreditLine <> Null Then cl\Stay = (cl\ID > LastCreditLine\ID)
		If cl\Stay Then EndLinesAmount = EndLinesAmount + 1
		ID = ID + 1
	Next
	If (Credits_Y + (24 * LastCreditLine\ID * MenuScale)) < -StringHeight(LastCreditLine\Txt)
		cra\CreditsTimer = cra\CreditsTimer + (0.5 * fps\Factor[1])
	EndIf
	
	If GetAnyKey() Then cra\CreditsTimer = -1.0
	If cra\CreditsTimer = -1.0
		Delete Each CreditsLine
		If (Not MainMenuOpen) Then
			MainMenuOpen = True
			NullGame(False)
		Else
			mm\MainMenuTab = MainMenuTab_Default
		EndIf
		cra\EndingTimer = 0.0
		cra\CreditsTextTimer = 0.0
		StopStream_Strict(MusicCHN) : MusicCHN = 0
		ShouldPlay = MUS_MENU_BREATH
		CurrSave = Null
		ResetLoadingTextColor()
		ResetInput()
		Return
	EndIf
End Function

Function RenderCredits%()
	Local cl.CreditsLine, LastCreditLine.CreditsLine
	Local Credits_Y#
	Local ID%
	Local EndLinesAmount%
	
	Credits_Y# = ((cra\CreditsTextTimer) / 2) + (opt\GraphicHeight + 10.0)
	
	Cls()
	
	ID = 0
	EndLinesAmount = 0
	LastCreditLine = Null
	
	If LastCreditLine = Null
		If Rand(300) > 1 Then DrawBlock(cra\CreditsScreen, mo\Viewport_Center_X - (400 * MenuScale), mo\Viewport_Center_Y - (400 * MenuScale))
	EndIf
	
	Color(255, 255, 255)
	For cl.CreditsLine = Each CreditsLine
		cl\ID = ID
		If Left(cl\Txt, 1) = "*"
			SetFontEx(fo\FontID[Font_Credits_Big])
			If (Not cl\Stay) Then TextEx(mo\Viewport_Center_X, Credits_Y + (24 * cl\ID * MenuScale), Right(cl\Txt, Len(cl\Txt) - 1), True)
		ElseIf Left(cl\Txt, 1) = "/"
			LastCreditLine = Before(cl)
		Else
			SetFontEx(fo\FontID[Font_Credits])
			If (Not cl\Stay) Then TextEx(mo\Viewport_Center_X, Credits_Y + (24 * cl\ID * MenuScale), cl\Txt, True)
		EndIf
		If LastCreditLine <> Null Then cl\Stay = (cl\ID > LastCreditLine\ID)
		If cl\Stay Then EndLinesAmount = EndLinesAmount + 1
		ID = ID + 1
	Next
	If (Credits_Y + (24 * LastCreditLine\ID * MenuScale)) < -StringHeight(LastCreditLine\Txt)
		If cra\CreditsTimer >= 0.0 And cra\CreditsTimer < 255.0
			Color(Max(Min(cra\CreditsTimer, 255.0), 0.0), Max(Min(cra\CreditsTimer, 255.0), 0.0), Max(Min(cra\CreditsTimer, 255.0), 0.0))
		ElseIf cra\CreditsTimer >= 255.0
			Color(255, 255, 255)
		Else
			Color(Max(Min(-cra\CreditsTimer, 255.0), 0.0), Max(Min(-cra\CreditsTimer, 255.0), 0.0), Max(Min(-cra\CreditsTimer, 255.0), 0.0))
		EndIf
	EndIf
	If cra\CreditsTimer <> 0.0
		For cl.CreditsLine = Each CreditsLine
			If cl\Stay
				SetFontEx(fo\FontID[Font_Credits])
				If Left(cl\Txt, 1) = "/"
					TextEx(mo\Viewport_Center_X, mo\Viewport_Center_Y + (EndLinesAmount / 2) + (24 * cl\ID * MenuScale), Right(cl\Txt, Len(cl\Txt) - 1), True)
				Else
					TextEx(mo\Viewport_Center_X, mo\Viewport_Center_Y + (24 * (cl\ID - LastCreditLine\ID) * MenuScale) - ((EndLinesAmount / 2) * 24 * MenuScale), cl\Txt, True)
				EndIf
			EndIf
		Next
	EndIf
	
	RenderLoadingText(20 * MenuScale, opt\GraphicHeight - (35 * MenuScale), GetLocalString("menu", "anykey"))
	
	Flip(True)
	
	If cra\CreditsTimer = -1.0
		FreeFont(fo\FontID[Font_Credits]) : fo\FontID[Font_Credits] = 0
		FreeFont(fo\FontID[Font_Credits_Big]) : fo\FontID[Font_Credits_Big] = 0
		FreeImage(cra\CreditsScreen) : cra\CreditsScreen = 0
		FreeImage(cra\EndingScreen) : cra\EndingScreen = 0
		Return
	EndIf
End Function

Global MTFTimer#
Global MTFCameraCheckTimer#
Global MTFCameraCheckDetected%

Function UpdateMTF%()
	Local RID% = PlayerRoom\RoomTemplate\RoomID
	
	If RID = r_gate_a_entrance Lor gm\ID >= GM_SANDBOX Then Return
	
	Local r.Rooms, n.NPCs
	Local Dist#, i%
	Local SndPath$ = "SFX\Intercom\Scripted\MTF\"
	
	If MTFTimer = 0.0
		If Rand(200) = 1 And RID <> r_dimension_1499
			Local entrance.Rooms = Null
			
			For r.Rooms = Each Rooms
				If r\RoomTemplate\RoomID = r_gate_a_entrance 
					entrance = r
					Exit
				EndIf
			Next
			
			If entrance <> Null
				If Abs(EntityZ(entrance\OBJ) - EntityZ(me\Collider)) < 36.0
					If szl\CurrentZone = EZ
						PlayAnnouncement(SndPath + "NTF\Announc_E11_Enter.ogg")
						
						MTFTimer = fps\Factor[0]
						
						For i = 0 To 2
							n.NPCs = CreateNPC(NPCTypeMTF, EntityX(entrance\RoomCenter, True) + 0.3 * (i - 1), 0.4, EntityZ(entrance\RoomCenter, True))
						Next
						If i = 0 Then n_I\MTFLeader = n
					EndIf
				EndIf
			EndIf
		EndIf
	Else
		If MTFTimer <= 70.0 * 120.0
			MTFTimer = MTFTimer + fps\Factor[0]
		ElseIf MTFTimer > 70.0 * 120.0 And MTFTimer < 10000.0
			PlayAnnouncement(SndPath + "NTF\AnnouncAfter1.ogg")
			MTFTimer = 10000.0
		ElseIf MTFTimer >= 10000.0 And MTFTimer <= 10000.0 + (70.0 * 120.0)
			MTFTimer = MTFTimer + fps\Factor[0]
		ElseIf MTFTimer > 10000.0 + (70.0 * 120.0) And MTFTimer < 20000.0
			PlayAnnouncement(SndPath + "NTF\AnnouncAfter2.ogg")
			MTFTimer = 20000.0
		ElseIf MTFTimer >= 20000.0 And MTFTimer <= 20000.0 + (70.0 * 60.0)
			MTFTimer = MTFTimer + fps\Factor[0]
		ElseIf MTFTimer > 20000.0 + (70.0 * 60.0) And MTFTimer < 25000.0
			Local Temp% = False
			
			; ~ If the player has an SCP in their inventory play special voice line
			For i = 0 To MaxItemAmount - 1
				If Inventory(i) <> Null
					If (Left(Inventory(i)\ItemTemplate\Name, 4) = "SCP-") And (Left(Inventory(i)\ItemTemplate\Name, 7) <> "SCP-035") And (Left(Inventory(i)\ItemTemplate\Name, 7) <> "SCP-093")
						Temp = True
						Exit
					EndIf
				EndIf
			Next
			If Temp
				PlayAnnouncement(SndPath + "NTF\ThreatAnnouncPossession.ogg")
			Else
				PlayAnnouncement(SndPath + "NTF\ThreatAnnounc" + Rand(3) + ".ogg")
			EndIf
			MTFTimer = 25000.0
		ElseIf MTFTimer >= 25000.0 And MTFTimer <= 25000.0 + (70.0 * 60.0)
			MTFTimer = MTFTimer + fps\Factor[0]
		ElseIf MTFTimer > 25000.0 + (70.0 * 60.0) And MTFTimer < 30000.0
			PlayAnnouncement(SndPath + "NTF\ThreatAnnouncFinal.ogg")
			MTFTimer = 30000.0
		EndIf
;		If n_I\MTFLeader = Null And MTFTimer < 35000.0
;			PlayAnnouncement(SndPath + "NTF\AnnouncLost.ogg")
;			MTFTimer = 35000.0
;		EndIf
	EndIf
End Function

Function UpdateCameraCheck%()
	If MTFCameraCheckTimer > 0.0 And MTFCameraCheckTimer < 70.0 * 90.0
		MTFCameraCheckTimer = MTFCameraCheckTimer + fps\Factor[0]
	ElseIf MTFCameraCheckTimer >= 70.0 * 90.0
		MTFCameraCheckTimer = 0.0
		If (Not me\Detected)
			If MTFCameraCheckDetected
				PlayAnnouncement("SFX\Character\MTF\AnnouncCameraFound" + Rand(2) + ".ogg")
				me\Detected = True
				MTFCameraCheckTimer = 70.0 * 60.0
			Else
				PlayAnnouncement("SFX\Character\MTF\AnnouncCameraNoFound.ogg")
			EndIf
		EndIf
		MTFCameraCheckDetected = False
		If MTFCameraCheckTimer = 0.0 Then me\Detected = False
	EndIf
End Function

Function UpdateExplosion%()
	Local p.Particles
	Local i%
	
	; ~ This here is necessary because the SCP-294's drinks with explosion effect didn't worked anymore -- ENDSHN
	If me\ExplosionTimer > 0.0
		me\ExplosionTimer = me\ExplosionTimer + fps\Factor[0]
		If me\ExplosionTimer < 140.0
			If me\ExplosionTimer - fps\Factor[0] < 5.0
				ExplosionSFX = LoadSound_Strict("SFX\Ending\GateB\Nuke1.ogg")
				PlaySound_Strict(ExplosionSFX)
				me\BigCameraShake = 10.0
				me\ExplosionTimer = 5.0
			EndIf
			me\BigCameraShake = CurveValue(me\ExplosionTimer / 60.0, me\BigCameraShake, 50.0)
		Else
			me\BigCameraShake = Min((me\ExplosionTimer / 20.0), 20.0)
			If me\ExplosionTimer - fps\Factor[0] < 140.0
				me\BlinkTimer = 1.0
				ExplosionSFX = LoadSound_Strict("SFX\Ending\GateB\Nuke2.ogg")
				PlaySound_Strict(ExplosionSFX)
				For i = 0 To (10 + (10 * (opt\ParticleAmount + 1)))
					p.Particles = CreateParticle(PARTICLE_BLACK_SMOKE, EntityX(me\Collider) + Rnd(-0.5, 0.5), EntityY(me\Collider) - Rnd(0.2, 1.5), EntityZ(me\Collider) + Rnd(-0.5, 0.5), Rnd(0.2, 0.6), 0.0, 350.0)
					RotateEntity(p\Pvt, -90.0, 0.0, 0.0, True)
					p\Speed = Rnd(0.05, 0.07)
				Next
			EndIf
			me\LightFlash = Min((me\ExplosionTimer - 140.0) / 10.0, 5.0)
			
			If me\ExplosionTimer > 160.0 Then me\Health = 0
			If me\ExplosionTimer > 500.0 Then me\ExplosionTimer = 0.0
			
			; ~ A dirty workaround to prevent the collider from falling down into the facility once the nuke goes off, causing the UpdateEvents() function to be called again and crashing the game
			PositionEntity(me\Collider, EntityX(me\Collider), 200.0, EntityZ(me\Collider))
		EndIf
	EndIf
End Function

Function UpdateVomit%()
	CatchErrors("UpdateVomit()")
	
	Local de.Decals
	Local Pvt%
	Local FPSFactorEx# = fps\Factor[0] / 70.0
	
	If me\CameraShakeTimer > 0.0
		me\CameraShakeTimer = Max(me\CameraShakeTimer - FPSFactorEx, 0.0)
		me\CameraShake = 2.0
	EndIf
	
	If me\VomitTimer > 0.0
		me\VomitTimer = me\VomitTimer - FPSFactorEx
		
		If (MilliSec Mod 1600) < Rand(200, 400)
			If me\BlurTimer = 0.0 Then me\BlurTimer = 70.0 * Rnd(10.0, 20.0)
			me\CameraShake = Rnd(0.0, 2.0)
		EndIf
		
		If Rand(50) = 50 And (MilliSec Mod 4000) < 200 Then PlaySound_Strict(CoughSFX((wi\GasMask > 0) Lor (wi\HazmatSuit > 0), Rand(0, 2)), True)
		
		; ~ Regurgitate when timer is below 10 seconds
		If me\VomitTimer < 10.0 And Rnd(0.0, 500.0 * me\VomitTimer) < 2.0
			If (Not ChannelPlaying(VomitCHN)) And me\Regurgitate = 0
				VomitCHN = PlaySound_Strict(LoadTempSound("SFX\SCP\294\Retch" + Rand(2) + ".ogg"), True)
				me\Regurgitate = MilliSecs() + 50
			EndIf
		EndIf
		
		If me\Regurgitate > MilliSecs() And me\Regurgitate <> 0
			mo\Mouse_Y_Speed_1 = mo\Mouse_Y_Speed_1 + 1.0
		Else
			me\Regurgitate = 0
		EndIf
	ElseIf me\VomitTimer < 0.0 ; ~ Vomit
		me\VomitTimer = me\VomitTimer - FPSFactorEx
		
		If me\VomitTimer > -5.0
			If (MilliSec Mod 400) < 50 Then me\CameraShake = 4.0
			mo\Mouse_X_Speed_1 = 0.0
			MakeMeUnplayable()
		Else
			me\Playable = True
		EndIf
		
		If (Not me\Vomit)
			me\BlurTimer = 70.0 * 40.0
			VomitSFX = LoadSound_Strict("SFX\SCP\294\Vomit.ogg")
			VomitCHN = PlaySound_Strict(VomitSFX, True)
			me\PrevBloodloss = me\Bloodloss
			DamagePlayer(Rand(1, 2))
			If (Not me\Crouch) Then SetCrouch(True)
			me\Bloodloss = 70.0
			me\EyeIrritation = 70.0 * 9.0
			
			Pvt = CreatePivot()
			PositionEntity(Pvt, EntityX(Camera), EntityY(me\Collider) - 0.05, EntityZ(Camera))
			TurnEntity(Pvt, 90.0, 0.0, 0.0)
			EntityPick(Pvt, 0.3)
			de.Decals = CreateDecal(DECAL_BLOOD_4, PickedX(), PickedY() + 0.005, PickedZ(), 90.0, 180.0, 0.0, 0.001, 1.0, 0, 1, 0, Rand(200, 255), 0)
			de\SizeChange = 0.001 : de\MaxSize = 0.6
			EntityParent(de\OBJ, PlayerRoom\OBJ)
			FreeEntity(Pvt) : Pvt = 0
			me\Vomit = True
		EndIf
		
		mo\Mouse_Y_Speed_1 = mo\Mouse_Y_Speed_1 + Max((1.0 + me\VomitTimer / 10.0), 0.0)
		
		If me\VomitTimer < -15.0
			FreeSound_Strict(VomitSFX)
			me\VomitTimer = 0.0
			If me\Health > 0 Then PlaySound_Strict(BreathSFX(0, 0), True)
			me\Bloodloss = me\PrevBloodloss
			DamagePlayer(Rand(1, 2))
			me\Vomit = False
		EndIf
	EndIf
	
	CatchErrors("Uncaught: UpdateVomit()")
End Function

Global EscapeTimer%
Global EscapeSecondsTimer#

Function UpdateEscapeTimer%()
	Local ev.Events
	
	For ev.Events = Each Events
		If ev\EventID = e_cont1_173_intro
			If ev\room = PlayerRoom
				Return
				Exit
			EndIf
		EndIf
	Next
	
	EscapeSecondsTimer = EscapeSecondsTimer - fps\Factor[0]
	If EscapeSecondsTimer <= 0.0
		EscapeTimer = EscapeTimer + 1
		EscapeSecondsTimer = 70.0
	EndIf
End Function

Type SCP005
	Field ChanceToSpawn%
End Type

Global I_005.SCP005

Type SCP035
	Field Sad%
End Type

Global I_035.SCP035

Type SCP500
	Field Taken%
End Type

Global I_500.SCP500

Type SCP714
	Field Using%
End Type

Global I_714.SCP714

Type SCP008
	Field Timer#
	Field Revert%
End Type

Global I_008.SCP008

Function Update008%()
	Local r.Rooms, e.Events, p.Particles, de.Decals
	Local PrevI008Timer#, i%
	Local TeleportForInfect%
	Local SinValue#
	
	TeleportForInfect = PlayerInReachableRoom()
	If I_008\Timer > 0.0
		If EntityHidden(t\OverlayID[Orl_scp_008]) Then ShowEntity(t\OverlayID[Orl_scp_008])
		SinValue = Sin(MilliSec / 8.0) + 2.0
		If I_008\Timer < 93.0
			PrevI008Timer = I_008\Timer
			If I_427\Timer < 70.0 * 360.0
				If I_008\Revert
					I_008\Timer = Max(0.0, I_008\Timer - (fps\Factor[0] * 0.01))
				Else
					If (Not I_427\Using) Then I_008\Timer = Min(I_008\Timer + (fps\Factor[0] * 0.002), 100.0)
				EndIf
			EndIf
			
			me\BlurTimer = Max(I_008\Timer * 3.0 * (2.0 - me\CrouchState), me\BlurTimer)
			
			me\HeartBeatRate = Max(me\HeartBeatRate, 100.0)
			me\HeartBeatVolume = Max(me\HeartBeatVolume, I_008\Timer / 120.0)
			
			EntityAlpha(t\OverlayID[Orl_scp_008], Min(PowTwo(I_008\Timer * 0.2) / 1000.0, 0.5) * SinValue)
			
			For i = 0 To 6
				If I_008\Timer > (i * 15.0) + 10.0 And PrevI008Timer <= (i * 15.0) + 10.0
					If (Not I_008\Revert) Then PlaySound_Strict(LoadTempSound("SFX\SCP\008\Voices" + i + ".ogg"), True)
				EndIf
			Next
			
			If I_008\Revert
				If I_008\Timer <= 20.0 And PrevI008Timer > 20.0
					CreateMsg(GetLocalString("msg", "better_2"))
				ElseIf I_008\Timer <= 40.0 And PrevI008Timer > 40.0
					CreateMsg(GetLocalString("msg", "nauseafading"))
				ElseIf I_008\Timer <= 60.0 And PrevI008Timer > 60.0
					CreateMsg(GetLocalString("msg", "headachefading"))
				ElseIf I_008\Timer <= 80.0 And PrevI008Timer > 80.0
					CreateMsg(GetLocalString("msg", "moreener"))
				EndIf
			Else
				If I_008\Timer > 20.0 And PrevI008Timer <= 20.0
					CreateMsg(GetLocalString("msg", "feverish"))
				ElseIf I_008\Timer > 40.0 And PrevI008Timer <= 40.0
					CreateMsg(GetLocalString("msg", "nausea"))
				ElseIf I_008\Timer > 60.0 And PrevI008Timer <= 60.0
					CreateMsg(GetLocalString("msg", "nauseaworse"))
				ElseIf I_008\Timer > 80.0 And PrevI008Timer <= 80.0
					CreateMsg(GetLocalString("msg", "faint"))
				ElseIf I_008\Timer >= 91.5
					me\BlinkTimer = Max(Min((-10.0) * (I_008\Timer - 91.5), me\BlinkTimer), -10.0)
					me\Zombie = True : MakeMeUnplayable()
					If I_008\Timer >= 92.7 And PrevI008Timer < 92.7
						If TeleportForInfect
							For r.Rooms = Each Rooms
								If r\RoomTemplate\RoomID = r_cont2_008
									PositionEntity(me\Collider, EntityX(r\Objects[8], True), EntityY(r\Objects[8], True), EntityZ(r\Objects[8], True), True)
									ResetEntity(me\Collider)
									r\NPC[0] = CreateNPC(NPCTypeD, EntityX(r\Objects[7], True), EntityY(r\Objects[7], True) + 0.2, EntityZ(r\Objects[7], True))
									PlaySound_Strict(LoadTempSound("SFX\SCP\008\KillScientist1.ogg"), True)
									ChangeNPCTextureID(r\NPC[0], NPC_CLASS_D_VICTIM_008_TEXTURE)
									TeleportToRoom(r)
									Exit
								EndIf
							Next
						EndIf
					EndIf
				EndIf
			EndIf
		Else
			PrevI008Timer = I_008\Timer
			I_008\Timer = Min(I_008\Timer + (fps\Factor[0] * 0.004), 100.0)
			
			If TeleportForInfect
				If I_008\Timer < 94.7
					EntityAlpha(t\OverlayID[Orl_scp_008], 0.5 * SinValue)
					me\BlurTimer = 900.0
					
					If I_008\Timer > 94.5 Then me\BlinkTimer = Max(Min((-50.0) * (I_008\Timer - 94.5), me\BlinkTimer), -10.0)
					PointEntity(me\Collider, PlayerRoom\NPC[0]\Collider)
					PointEntity(PlayerRoom\NPC[0]\Collider, me\Collider)
					PointEntity(Camera, PlayerRoom\NPC[0]\Collider, EntityRoll(Camera))
					me\ForceMove = 0.75
					DamagePlayer(Rand(12, 22))
					me\Bloodloss = 0.0
					me\Playable = True
					
					Animate2(PlayerRoom\NPC[0]\OBJ, AnimTime(PlayerRoom\NPC[0]\OBJ), 357.0, 381.0, 0.3)
				ElseIf I_008\Timer < 98.5
					EntityAlpha(t\OverlayID[Orl_scp_008], 0.5 * SinValue)
					me\BlurTimer = 950.0
					
					me\ForceMove = 0.0
					MakeMeUnplayable()
					PointEntity(Camera, PlayerRoom\NPC[0]\Collider)
					
					If PrevI008Timer < 94.7
						PlayerRoom\NPC[0]\State3 = -1.0 : PlayerRoom\NPC[0]\HP = 0
						SetNPCFrame(PlayerRoom\NPC[0], 19.0)
						
						PlaySound_Strict(LoadTempSound("SFX\SCP\008\KillScientist2.ogg"), True)
						
						msg\DeathMsg = Format(GetLocalString("death", "0081"), Occupation)
						
						de.Decals = CreateDecal(DECAL_BLOOD_2, EntityX(PlayerRoom\NPC[0]\Collider), PlayerRoom\y + 544.0 * RoomScale + 0.01, EntityZ(PlayerRoom\NPC[0]\Collider), 90.0, Rnd(360.0), 0.0, 0.8)
						EntityParent(de\OBJ, PlayerRoom\OBJ)
						
						Kill()
					ElseIf I_008\Timer > 96.0
						me\BlinkTimer = Max(Min((-10.0) * (I_008\Timer - 96.0), me\BlinkTimer), -10.0)
					Else
						me\Health = 0
					EndIf
					
					If opt\ParticleAmount > 0
						If Rand(50) = 1
							p.Particles = CreateParticle(PARTICLE_BLOOD, EntityX(PlayerRoom\NPC[0]\Collider), EntityY(PlayerRoom\NPC[0]\Collider), EntityZ(PlayerRoom\NPC[0]\Collider), Rnd(0.05, 0.1), 0.15, 200.0)
							p\Speed = 0.01 : p\SizeChange = 0.01 : p\Alpha = 0.5 : p\AlphaChange = -0.01
							RotateEntity(p\Pvt, Rnd(360.0), Rnd(360.0), 0.0)
						EndIf
					EndIf
					
					PositionEntity(me\Head, EntityX(PlayerRoom\NPC[0]\Collider, True), EntityY(PlayerRoom\NPC[0]\Collider, True) + 0.65, EntityZ(PlayerRoom\NPC[0]\Collider, True), True)
					SinValue = Sin(MilliSec / 5.0)
					RotateEntity(me\Head, (1.0 + SinValue) * 15.0, PlayerRoom\Angle - 180.0, 0.0, True)
					MoveEntity(me\Head, 0.0, 0.0, -0.4)
					TurnEntity(me\Head, 80.0 + SinValue * 30.0, SinValue * 40.0, 0.0)
				EndIf
			Else
				Local RID% = PlayerRoom\RoomTemplate\RoomID
				
				me\BlinkTimer = Max(Min((-10.0) * (I_008\Timer - 96.0), me\BlinkTimer), -10.0)
				If RID = r_dimension_1499
					msg\DeathMsg = GetLocalString("death", "14991")
				ElseIf IsPlayerOutsideFacility()
					msg\DeathMsg = Format(GetLocalString("death", "008gate"), Occupation, "{0}")
					If RID = r_gate_a
						msg\DeathMsg = Format(msg\DeathMsg, "A", "{1}")
					Else
						msg\DeathMsg = Format(msg\DeathMsg, "B", "{1}")
					EndIf
				Else
					msg\DeathMsg = ""
				EndIf
				Kill()
			EndIf
		EndIf
	Else
		I_008\Revert = False
		If (Not EntityHidden(t\OverlayID[Orl_scp_008])) Then HideEntity(t\OverlayID[Orl_scp_008])
	EndIf
End Function

Type SCP268
	Field Using%
	Field Timer#
	Field InvisibilityOn%
End Type

Global I_268.SCP268

Function Update268%()
    If I_268\Using > 1
		I_268\InvisibilityOn = (I_268\Timer > 0.0)
		If I_268\Using = 3 
            I_268\Timer = Max(I_268\Timer - ((fps\Factor[0] / 1.5) * (1.0 + I_714\Using)), 0.0)
        Else
            I_268\Timer = Max(I_268\Timer - (fps\Factor[0] * (1.0 + I_714\Using)), 0.0)
        EndIf
    ElseIf I_268\Using = 0
        I_268\Timer = Min(I_268\Timer + fps\Factor[0], 600.0)
		I_268\InvisibilityOn = False
    EndIf
End Function 

Type SCP409
	Field Timer#
	Field Revert%
End Type

Global I_409.SCP409

Function Update409%()
	Local PrevI409Timer# = I_409\Timer
	
	If I_409\Timer > 0.0
		If EntityHidden(t\OverlayID[Orl_scp_409]) Then ShowEntity(t\OverlayID[Orl_scp_409])
		If I_427\Timer < 70.0 * 360.0
			If I_409\Revert
				I_409\Timer = Max(0.0, I_409\Timer - (fps\Factor[0] * 0.01))
			Else
				If (Not I_427\Using) Then I_409\Timer = Min(I_409\Timer + (fps\Factor[0] * 0.004), 100.0)
			EndIf
		EndIf
		EntityAlpha(t\OverlayID[Orl_scp_409], Min((PowTwo(I_409\Timer * 0.2)) / 1000.0, 0.5))
		me\BlurTimer = Max(I_409\Timer * 3.0 * (2.0 - me\CrouchState), me\BlurTimer)
		
		If I_409\Revert
			If I_409\Timer <= 40.0 And PrevI409Timer > 40.0
				CreateMsg(GetLocalString("msg", "409legs_1"))
			ElseIf I_409\Timer <= 55.0 And PrevI409Timer > 55.0
				CreateMsg(GetLocalString("msg", "409abdomen_1"))
			ElseIf I_409\Timer <= 70.0 And PrevI409Timer > 70.0
				CreateMsg(GetLocalString("msg", "409arms_1"))
			ElseIf I_409\Timer <= 85.0 And PrevI409Timer > 85.0
				CreateMsg(GetLocalString("msg", "409head_1"))
			EndIf
		Else
			If I_409\Timer > 40.0 And PrevI409Timer <= 40.0
				CreateMsg(GetLocalString("msg", "409legs_2"))
			ElseIf I_409\Timer > 55.0 And PrevI409Timer <= 55.0
				CreateMsg(GetLocalString("msg", "409abdomen_2"))
			ElseIf I_409\Timer > 70.0 And PrevI409Timer <= 70.0
				CreateMsg(GetLocalString("msg", "409arms_2"))
			ElseIf I_409\Timer > 85.0 And PrevI409Timer <= 85.0
				CreateMsg(GetLocalString("msg", "409head_2"))
			ElseIf I_409\Timer > 93.0 And PrevI409Timer <= 93.0
				If (Not I_409\Revert)
					PlaySound_Strict(DamageSFX[13], True)
					DamagePlayer(Rand(34, 45))
				EndIf
			ElseIf I_409\Timer > 94.0
				I_409\Timer = Min(I_409\Timer + (fps\Factor[0] * 0.004), 100.0)
				MakeMeUnplayable()
				me\BlurTimer = 4.0
				me\CameraShake = 3.0
			EndIf
		EndIf
		If I_409\Timer >= 55.0
			me\StaminaEffect = 1.2
			me\StaminaEffectTimer = 1.0
			me\Stamina = Min(me\Stamina, 60.0)
		EndIf
		If I_409\Timer >= 96.92
			msg\DeathMsg = Format(GetLocalString("death", "409"), Occupation)
			Kill(True)
		EndIf
	Else
		I_409\Revert = False
		If (Not EntityHidden(t\OverlayID[Orl_scp_409])) Then HideEntity(t\OverlayID[Orl_scp_409])
	EndIf
End Function

Type SCP427
	Field Using%
	Field Timer#
	Field Sound%[2]
	Field SoundCHN%[2]
End Type

Global I_427.SCP427

Function Update427%()
	Local de.Decals, e.Events
	Local i%, Pvt%, TempCHN%
	Local PrevI427Timer# = I_427\Timer
	
	If I_427\Timer < 70.0 * 360.0
		If I_427\Using
			I_427\Timer = I_427\Timer + fps\Factor[0]
			If me\Health < 100 Then HealPlayer(fps\Factor[0] * 0.005)
			If me\Bloodloss > 0.0 And me\Health > 50 Then me\Bloodloss = Max(me\Bloodloss - (fps\Factor[0] * 0.001), 0.0)
			If I_008\Timer > 0.0 Then I_008\Timer = Max(I_008\Timer - (fps\Factor[0] * 0.001), 0.0)
			If I_409\Timer > 0.0 Then I_409\Timer = Max(I_409\Timer - (fps\Factor[0] * 0.003), 0.0)
			For i = 0 To 6
				If I_1025\State[i] > 0.0 Then I_1025\State[i] = Max(I_1025\State[i] - (0.001 * fps\Factor[0] * I_1025\State[7]), 0.0)
			Next
			If I_427\Sound[0] = 0 Then I_427\Sound[0] = LoadSound_Strict("SFX\SCP\427\Effect.ogg")
			If (Not ChannelPlaying(I_427\SoundCHN[0])) Then I_427\SoundCHN[0] = PlaySound_Strict(I_427\Sound[0])
			If I_427\Timer >= 70.0 * 180.0
				If I_427\Sound[1] = 0 Then I_427\Sound[1] = LoadSound_Strict("SFX\SCP\427\Transform.ogg")
				If (Not ChannelPlaying(I_427\SoundCHN[1])) Then I_427\SoundCHN[1] = PlaySound_Strict(I_427\Sound[1])
			EndIf
			If PrevI427Timer < 70.0 * 60.0 And I_427\Timer >= 70.0 * 60.0
				CreateMsg(GetLocalString("msg", "freshener"))
			ElseIf PrevI427Timer < 70.0 * 180.0 And I_427\Timer >= 70.0 * 180.0
				CreateMsg(GetLocalString("msg", "gentlemuscle"))
			EndIf
		Else
			For i = 0 To 1
				If ChannelPlaying(I_427\SoundCHN[i]) Then StopChannel(I_427\SoundCHN[i]) : I_427\SoundCHN[i] = 0
			Next
		EndIf
	Else
		If PrevI427Timer - fps\Factor[0] < 70.0 * 360.0 And I_427\Timer >= 70.0 * 360.0
			CreateMsg(GetLocalString("msg", "muscleswelling"))
		ElseIf PrevI427Timer - fps\Factor[0] < 70.0 * 390.0 And I_427\Timer >= 70.0 * 390.0
			CreateMsg(GetLocalString("msg", "nolegs"))
		EndIf
		I_427\Timer = I_427\Timer + fps\Factor[0]
		If I_427\Sound[0] = 0 Then I_427\Sound[0] = LoadSound_Strict("SFX\SCP\427\Effect.ogg")
		If I_427\Sound[1] = 0 Then I_427\Sound[1] = LoadSound_Strict("SFX\SCP\427\Transform.ogg")
		For i = 0 To 1
			If (Not ChannelPlaying(I_427\SoundCHN[i])) Then I_427\SoundCHN[i] = PlaySound_Strict(I_427\Sound[i])
		Next
		If Rnd(200) < 2.0
			Pvt = CreatePivot()
			PositionEntity(Pvt, EntityX(me\Collider) + Rnd(-0.05, 0.05), EntityY(me\Collider) - 0.05, EntityZ(me\Collider) + Rnd(-0.05, 0.05))
			TurnEntity(Pvt, 90.0, 0.0, 0.0)
			EntityPick(Pvt, 0.3)
			de.Decals = CreateDecal(DECAL_427, PickedX(), PickedY() + 0.005, PickedZ(), 90.0, Rnd(360.0), 0.0, Rnd(0.03, 0.08) * 2.0)
			de\SizeChange = Rnd(0.001, 0.0015) : de\MaxSize = de\Size + 0.009
			EntityParent(de\OBJ, PlayerRoom\OBJ)
			TempCHN = PlaySound_Strict(DripSFX[Rand(0, 3)])
			ChannelVolume(TempCHN, Rnd(0.0, 0.8) * opt\SFXVolume * opt\MasterVolume)
			ChannelPitch(TempCHN, Rand(20000, 30000))
			FreeEntity(Pvt) : Pvt = 0
			me\BlurTimer = 800.0
		EndIf
		If I_427\Timer >= 70.0 * 420.0
			msg\DeathMsg = GetLocalString("death", "morepower")
			Kill()
		ElseIf I_427\Timer >= 70.0 * 390.0
			If (Not me\Crouch) Then SetCrouch(True)
		EndIf
	EndIf
End Function

Type SCP294
	Field Using%
	Field ToInput$
	Field DrinksMap%
	Field Drinks%
End Type

Global I_294.SCP294

Function Init294Drinks%()
	Local LocalDrinks% = JsonParseFromFile(lang\LanguagePath + SCP294File)
	Local i%, j%
	
	If JsonIsArray(LocalDrinks) ; ~ Has localized scp294 drinks -> Use localized only
		I_294\Drinks = JsonGetArray(LocalDrinks)
	Else
		I_294\Drinks = JsonGetArray(JsonParseFromFile(SCP294File))
	EndIf

	I_294\DrinksMap = CreateS2IMap()
	For i = 0 To JsonGetArraySize(I_294\Drinks) - 1
		Local DrinkNames% = JsonGetArray(JsonGetValue(JsonGetArrayValue(I_294\Drinks, i), "name"))
		
		For j = 0 To JsonGetArraySize(DrinkNames) - 1
			S2IMapSet(I_294\DrinksMap, Upper(JsonGetString(JsonGetArrayValue(DrinkNames, j))), i)
		Next
	Next
End Function

Function Update294%()
	Local it.Items
	Local x#, y#, xTemp%, yTemp%, StrTemp$, Temp%
	Local Alpha#, Glow%
	Local R%, G%, B%
	
	x = mo\Viewport_Center_X - (ImageWidth(t\ImageID[Img_SCP_294]) / 2)
	y = mo\Viewport_Center_Y - (ImageHeight(t\ImageID[Img_SCP_294]) / 2)
	
	Temp = (PlayerRoom\SoundCHN = 0)
	
	If Temp
		If mo\MouseHit1
			xTemp = Floor((MousePosX - x - (228 * MenuScale)) / (35.5 * MenuScale))
			yTemp = Floor((MousePosY - y - (342 * MenuScale)) / (36.5 * MenuScale))
			
			Temp = False
			
			If (yTemp >= 0 And yTemp < 5) And (xTemp >= 0 And xTemp < 10)
				PlaySound_Strict(ButtonSFX[0])
				
				StrTemp = ""
				
				Select(yTemp)
					Case 0
						;[Block]
						StrTemp = ((xTemp + 1) Mod 10)
						;[End Block]
					Case 1
						;[Block]
						Select(xTemp)
							Case 0
								;[Block]
								StrTemp = "Q"
								;[End Block]
							Case 1
								;[Block]
								StrTemp = "W"
								;[End Block]
							Case 2
								;[Block]
								StrTemp = "E"
								;[End Block]
							Case 3
								;[Block]
								StrTemp = "R"
								;[End Block]
							Case 4
								;[Block]
								StrTemp = "T"
								;[End Block]
							Case 5
								;[Block]
								StrTemp = "Y"
								;[End Block]
							Case 6
								;[Block]
								StrTemp = "U"
								;[End Block]
							Case 7
								;[Block]
								StrTemp = "I"
								;[End Block]
							Case 8
								;[Block]
								StrTemp = "O"
								;[End Block]
							Case 9
								;[Block]
								StrTemp = "P"
								;[End Block]
						End Select
						;[End Block]
					Case 2
						;[Block]
						Select(Int(xTemp))
							Case 0
								;[Block]
								StrTemp = "A"
								;[End Block]
							Case 1
								;[Block]
								StrTemp = "S"
								;[End Block]
							Case 2
								;[Block]
								StrTemp = "D"
								;[End Block]
							Case 3
								;[Block]
								StrTemp = "F"
								;[End Block]
							Case 4
								;[Block]
								StrTemp = "G"
								;[End Block]
							Case 5
								;[Block]
								StrTemp = "H"
								;[End Block]
							Case 6
								;[Block]
								StrTemp = "J"
								;[End Block]
							Case 7
								;[Block]
								StrTemp = "K"
								;[End Block]
							Case 8
								;[Block]
								StrTemp = "L"
								;[End Block]
							Case 9 ; ~ Dispense
								;[Block]
								Temp = True
								;[End Block]
						End Select
					Case 3
						;[Block]
						Select(Int(xTemp))
							Case 0
								;[Block]
								StrTemp = "Z"
								;[End Block]
							Case 1
								;[Block]
								StrTemp = "X"
								;[End Block]
							Case 2
								;[Block]
								StrTemp = "C"
								;[End Block]
							Case 3
								;[Block]
								StrTemp = "V"
								;[End Block]
							Case 4
								;[Block]
								StrTemp = "B"
								;[End Block]
							Case 5
								;[Block]
								StrTemp = "N"
								;[End Block]
							Case 6
								;[Block]
								StrTemp = "M"
								;[End Block]
							Case 7
								;[Block]
								StrTemp = "-"
								;[End Block]
							Case 8
								;[Block]
								StrTemp = " "
								;[End Block]
							Case 9
								;[Block]
								I_294\ToInput = Left(I_294\ToInput, Max(Len(I_294\ToInput) - 1, 0.0))
								;[End Block]
						End Select
					Case 4
						;[Block]
						StrTemp = " "
						;[End Block]
				End Select
			EndIf
			
			I_294\ToInput = I_294\ToInput + StrTemp
			
			If Temp And I_294\ToInput <> "" ; ~ Dispense
				I_294\ToInput = Trim(I_294\ToInput)
				If Left(I_294\ToInput, Min(7, Len(I_294\ToInput))) = "cup of "
					I_294\ToInput = Right(I_294\ToInput, Len(I_294\ToInput) - 7)
				ElseIf Left(I_294\ToInput, Min(9, Len(I_294\ToInput))) = "a cup of "
					I_294\ToInput = Right(I_294\ToInput, Len(I_294\ToInput) - 9)
				EndIf
				
				If S2IMapContains(I_294\DrinksMap, I_294\ToInput)
					Local Drink% = JsonGetArrayValue(I_294\Drinks, S2IMapGet(I_294\DrinksMap, I_294\ToInput))
					
					If (Not JsonIsNull(JsonGetValue(Drink, "dispense_sound"))) Then PlayerRoom\SoundCHN = PlaySound_Strict(LoadTempSound(JsonGetString(JsonGetValue(Drink, "dispense_sound"))))
					
					If me\UsedMastercard
						PlaySound_Strict(LoadTempSound("SFX\SCP\294\PullMasterCard.ogg"))
						
						Local i%
						
						If ItemAmount < MaxItemAmount
							For i = 0 To MaxItemAmount - 1
								If Inventory(i) = Null
									Inventory(i) = CreateItem("Mastercard", "mastercard", 1.0, 1.0, 1.0)
									Inventory(i)\Picked = True
									Inventory(i)\Dropped = -1
									Inventory(i)\ItemTemplate\Found = True
									HideEntity(Inventory(i)\Collider)
									EntityType(Inventory(i)\Collider, HIT_ITEM)
									EntityParent(Inventory(i)\Collider, 0)
									Exit
								EndIf
							Next
						Else
							it.Items = CreateItem("Mastercard", "mastercard", EntityX(me\Collider), EntityY(me\Collider) + 0.3, EntityZ(me\Collider))
							it\ItemTemplate\Found = True
							EntityType(it\Collider, HIT_ITEM)
						EndIf
					EndIf
					
					If JsonGetBool(JsonGetValue(Drink, "explosion"))
						me\ExplosionTimer = 135.0
						If JsonIsNull(JsonGetValue(Drink, "death_message"))
							msg\DeathMsg = ""
						Else
							JsonGetString(JsonGetValue(Drink, "death_message"))
						EndIf
					EndIf
					
					Local DrinkColor% = JsonGetArray(JsonGetValue(Drink, "color"))
					
					R = JsonGetInt(JsonGetArrayValue(DrinkColor, 0))
					G = JsonGetInt(JsonGetArrayValue(DrinkColor, 1))
					B = JsonGetInt(JsonGetArrayValue(DrinkColor, 2))
					
					Alpha = JsonGetFloat(JsonGetValue(Drink, "alpha"))
					Glow = JsonGetBool(JsonGetValue(Drink, "glow"))
					If Glow Then Alpha = -Alpha
					
					it.Items = CreateItem("Cup", "cup", EntityX(PlayerRoom\Objects[1], True), EntityY(PlayerRoom\Objects[1], True), EntityZ(PlayerRoom\Objects[1], True), R, G, B, Alpha)
					it\Name = Drink
					it\DisplayName = Format(GetLocalString("items", "cupof"), I_294\ToInput)
					EntityType(it\Collider, HIT_ITEM)
				Else
					; ~ Out of range
					I_294\ToInput = GetLocalString("misc", "ofr")
					PlayerRoom\SoundCHN = PlaySound_Strict(LoadTempSound("SFX\SCP\294\OutOfRange.ogg"))
				EndIf
			EndIf
		EndIf
		
		If mo\MouseHit2 Lor (Not I_294\Using)
			I_294\Using = False
			I_294\ToInput = ""
			StopMouseMovement()
		EndIf
	Else ; ~ Playing a dispensing sound
		If I_294\ToInput <> GetLocalString("misc", "ofr") Then I_294\ToInput = GetLocalString("misc", "dispensing")
		
		If (Not ChannelPlaying(PlayerRoom\SoundCHN))
			If I_294\ToInput <> GetLocalString("misc", "ofr")
				I_294\Using = False
				me\UsedMastercard = False
				StopMouseMovement()
				
				Local e.Events
				
				For e.Events = Each Events
					If PlayerRoom = e\room
						e\EventState2 = 0.0
						Exit
					EndIf
				Next
			EndIf
			I_294\ToInput = ""
			PlayerRoom\SoundCHN = 0
		EndIf
	EndIf
End Function

Function Render294%()
	Local x#, y#, xTemp%, yTemp%, Temp%
	
	ShowPointer()
	
	x = mo\Viewport_Center_X - (ImageWidth(t\ImageID[Img_SCP_294]) / 2)
	y = mo\Viewport_Center_Y - (ImageHeight(t\ImageID[Img_SCP_294]) / 2)
	DrawBlock(t\ImageID[Img_SCP_294], x, y)
	RenderCursor()
	
	Temp = (PlayerRoom\SoundCHN = 0)
	
	TextEx(x + (905 * MenuScale), y + (185 * MenuScale), Right(I_294\ToInput, 13), True, True)
	
	If Temp
		If mo\MouseHit2 Lor (Not I_294\Using) Then HidePointer()
	Else ; ~ Playing a dispensing sound
		If (Not ChannelPlaying(PlayerRoom\SoundCHN))
			If I_294\ToInput <> GetLocalString("misc", "ofr") Then HidePointer()
		EndIf
	EndIf
End Function

Type SCP1025
	Field State#[8]
End Type

Global I_1025.SCP1025

Function Update1025%()
	Local i%
	Local Factor1025# = fps\Factor[0] * I_1025\State[7]
	
	For i = 0 To 6
		If I_1025\State[i] > 0.0
			Select(i)
				Case 0 ; ~ Common cold
					;[Block]
					UpdateCough(1000)
					me\Stamina = me\Stamina - (Factor1025 * 0.3)
					;[End Block]
				Case 1 ; ~ Chicken pox
					;[Block]
					If Rand(9000) = 1 Then CreateMsg(GetLocalString("msg", "skinitchy"))
					;[End Block]
				Case 2 ; ~ Cancer of the lungs
					;[Block]
					UpdateCough(800)
					me\Stamina = me\Stamina - (Factor1025 * 0.1)
					;[End Block]
				Case 3 ; ~ Appendicitis
					; ~ 0.035 / sec = 2.1 / min
					If (Not I_427\Using) And I_427\Timer < 70.0 * 360.0 Then I_1025\State[i] = I_1025\State[i] + (Factor1025 * 0.0005)
					If I_1025\State[i] > 20.0
						If I_1025\State[i] - Factor1025 <= 20.0 Then CreateMsg(GetLocalString("msg", "stomachunbearable"))
						me\Stamina = me\Stamina - (Factor1025 * 0.3)
					ElseIf I_1025\State[i] > 10.0
						If I_1025\State[i] - Factor1025 <= 10.0 Then CreateMsg(GetLocalString("msg", "stomachaching"))
					EndIf
					;[End Block]
				Case 4 ; ~ Asthma
					;[Block]
					If me\Stamina < 35.0
						UpdateCough(Int(140.0 + me\Stamina * 8.0))
						me\CurrSpeed = CurveValue(0.0, me\CurrSpeed, 10.0 + me\Stamina * 15.0)
					EndIf
					;[End Block]
				Case 5 ; ~ Cardiac arrest
					;[Block]
					If (Not I_427\Using) And I_427\Timer < 70.0 * 360.0 Then I_1025\State[i] = I_1025\State[i] + (Factor1025 * 0.35)
					
					; ~ 35 / sec
					If I_1025\State[i] > 110.0
						me\HeartBeatRate = 0.0
						me\BlurTimer = Max(me\BlurTimer, 500.0)
						If I_1025\State[i] > 140.0
							msg\DeathMsg = GetLocalString("death", "1025")
							Kill()
						EndIf
					Else
						me\HeartBeatRate = Max(me\HeartBeatRate, 70.0 + I_1025\State[i])
						me\HeartBeatVolume = 1.0
					EndIf
					;[End Block]
				Case 6 ; ~ Secondary polycythemia
					;[Block]
					If (Not I_427\Using) And I_427\Timer < 70.0 * 360.0 Then I_1025\State[i] = I_1025\State[i] + 0.00025 * Factor1025 * (100.0 / I_1025\State[i])
					me\Stamina = Min(100.0, me\Stamina + (90.0 - me\Stamina) * I_1025\State[i] * Factor1025 * 0.00008)
					If I_1025\State[i] > 15.0 And I_1025\State[i] - Factor1025 <= 15.0 Then CreateMsg(GetLocalString("msg", "energetic"))
					;[End Block]
			End Select
		EndIf
	Next
End Function

Function TeleportEntity%(Entity%, x#, y#, z#, CustomRadius# = 0.3, IsGlobal% = False, PickRange# = 2.0, Dir% = False)
	Local Pvt%, Pick#
	; ~ Dir = 0 - towards the floor (default)
	; ~ Dir = 1 - towrads the ceiling (mostly for PD decal after leaving dimension)
	
	Pvt = CreatePivot()
	PositionEntity(Pvt, x, y + 0.05, z, IsGlobal)
	If (Not Dir)
		RotateEntity(Pvt, 90.0, 0.0, 0.0)
	Else
		RotateEntity(Pvt, -90.0, 0.0, 0.0)
	EndIf
	Pick = EntityPick(Pvt, PickRange)
	If Pick <> 0
		If (Not Dir)
			PositionEntity(Entity, x, PickedY() + CustomRadius + 0.02, z, IsGlobal)
		Else
			PositionEntity(Entity, x, PickedY() + CustomRadius - 0.02, z, IsGlobal)
		EndIf
	Else
		PositionEntity(Entity, x, y, z, IsGlobal)
	EndIf
	FreeEntity(Pvt) : Pvt = 0
	ResetEntity(Entity)
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D TSS