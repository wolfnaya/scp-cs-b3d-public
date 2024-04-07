
;[Block]
Global Camera%
Global Occupation$
Global InFacility%
Global PlayerFallingPickDistance#
Global ShouldEntitiesFall%
Global HideDistance#
Global CoffinDistance#
Global RemoteDoorOn%
Global SandBoxMenuOpen%
Global HitKeyUse%, DownKeyUse%
;[End Block]

;[Block]
Type Player
	Field KillAnim%, KillAnimTimer#, FallTimer#, DeathTimer#
	Field Sanity#, RestoreSanity%
	Field ForceMove#, ForceAngle#
	Field Playable%
	Field BlinkTimer#, BLINKFREQ#, BlinkEffect#, BlinkEffectTimer#, EyeIrritation#, EyeStuck#
	Field Stamina#, StaminaEffect#, StaminaEffectTimer#, StaminaMax#
	Field CameraShakeTimer#, Shake#, CameraShake#, BigCameraShake#
	Field Vomit%, VomitTimer#, Regurgitate%
	Field HeartBeatRate#, HeartBeatTimer#, HeartBeatVolume#
	Field Bloodloss#, PrevBloodloss#, HealTimer#
	Field DropSpeed#, HeadDropSpeed#, CurrSpeed#
	Field Crouch%, CrouchState#
	Field SndVolume#
	Field SelectedEnding%
	Field BlurVolume#, BlurTimer#
	Field LightBlink#, LightFlash#
	Field CurrCameraZoom#
	Field RefinedItems%
	Field Deaf%, DeafTimer#
	Field Zombie%
	Field Detected%
	Field ExplosionTimer#
	Field Zone%
	Field Collider%, Head%
	Field StopHidingTimer#
	Field Funds%, UsedMastercard%
	Field Health%
	Field CanUseWeapons%, Sprinting%
	Field HeldAmmo%[MaxWeaponCaliberTypes]
	Field ModelPivot%, ModelHead%, ModelTop%, ModelBottom%
	Field ShowHUD%, ShowModel%
End Type
;[End Block]

Global me.Player

;[Block]
Const MaxChapters% = 10
Const CPT_0% = 0, CPT_1% = 1, CPT_2% = 2, CPT_3% = 3, CPT_4% = 4, CPT_5% = 5, CPT_6% = 6, CPT_7% = 7, CPT_8% = 8, CPT_9% = 9
;[End Block]

;[Block]
Type Chapters
	Field ID%
	Field Unlocked%[MaxChapters]
End Type
;[End Block]

Global cpt.Chapters = New Chapters

;[Block]
Type EventConstants
	; ~ TODO
End Type
;[End Block]

Global ecst.EventConstants = New EventConstants

Function LoadData%()
	TextureFilter("", 8192) ; ~ This turns on Anisotropic filtering for textures
	TextureAnisotropic(opt\AnisotropicLevel)
	AntiAlias(opt\AntiAliasing)
	
	SubFile = JsonParseFromFile(SubtitlesFile)
	LocalSubFile = JsonParseFromFile(lang\LanguagePath + SubtitlesFile)
	SubColors = JsonGetValue(SubFile, "colors")
	LocalSubColors = JsonGetValue(LocalSubFile, "colors")
	SubtitlesInit = True
	
	Occupation = GetLocalString("misc", "subject")
	PlayerFallingPickDistance = 10.0
	
	Collisions(HIT_PLAYER, HIT_MAP, 2, 2)
	Collisions(HIT_PLAYER, HIT_PLAYER, 1, 3)
	Collisions(HIT_ITEM, HIT_MAP, 2, 2)
	Collisions(HIT_APACHE, HIT_APACHE, 1, 2)
	Collisions(HIT_DEAD, HIT_MAP, 2, 2)
	
	LoadRoomTemplates("Data\rooms.ini")
	
	ShouldEntitiesFall = True
	HideDistance = 17.0
	CoffinDistance = 100.0
	
	QuickLoadPercent = -1
	
	CanSave = 3
	
	EscapeSecondsTimer = 70.0
	
	chs.Cheats = New Cheats
	me.Player = New Player
	wi.WearableItems = New WearableItems
	
	I_005.SCP005 = New SCP005
	I_008.SCP008 = New SCP008
	I_035.SCP035 = New SCP035
	I_268.SCP268 = New SCP268
	I_294.SCP294 = New SCP294
	Init294Drinks()
	I_409.SCP409 = New SCP409
	I_427.SCP427 = New SCP427
	I_500.SCP500 = New SCP500
	I_714.SCP714 = New SCP714
	I_1025.SCP1025 = New SCP1025
	
	as.AutoSave = New AutoSave
	
	msg.Messages = New Messages
	
	I_Zone.MapZones = New MapZones
	
	bk.BrokenDoor = New BrokenDoor
	
	achv.Achievements = New Achievements
	LoadAchievementsFile()
	igm.InGameMenu = New InGameMenu
	
	t.Textures = New Textures
End Function

Function LoadPlayer%()
	
	;[Block]
	me\Collider = CreatePivot()
	EntityRadius(me\Collider, 0.15, 0.30)
	EntityPickMode(me\Collider, 1)
	EntityType(me\Collider, HIT_PLAYER)
	
	me\Head = CreatePivot()
	EntityRadius(me\Head, 0.15)
	EntityType(me\Head, HIT_PLAYER)
	
	Camera = CreateCamera()
	CameraViewport(Camera, 0, 0, opt\GraphicWidth, opt\GraphicHeight)
	CameraFogMode(Camera, 1)
	CameraFogRange(Camera, 0.1, opt\CameraFogFar)
	CameraFogColor(Camera, 30.0, 30.0, 30.0)
	CameraRange(Camera, 0.01, opt\CameraFogFar)
	CameraClsColor(Camera, 30.0, 30.0, 30.0)
	AmbientLight(30.0, 30.0, 30.0)
	;[End Block]
	
	;[Block]
	me\Health = 100
	me\ModelPivot = CreatePivot()
	;[End Block]
	
	;[Block]
	If (Not SelectedDifficulty\Realism)
		If opt\HUDEnabled
			me\ShowHUD = True
		Else
			me\ShowHUD = False
		EndIf
	Else
		me\ShowHUD = False
	EndIf
	;[End Block]
	
	;[Block]
	cpt\Unlocked[CPT_0] = True : cpt\Unlocked[CPT_1] = True
	;[End Block]
End Function

;[Block]
Const PlayerAnimationsFile$ = "Data\PlayerAnimations.ini"
;[End Block]

Function AnimateByName%(Obj%, AnimationList$, AnimationSection$, AnimationName$, Loop% = False, Reverse% = False)
	Local FrameStart#, FrameEnd#, Speed#, StrTemp$
	
	StrTemp = IniGetString(AnimationList, AnimationSection, AnimationName)
	
	If Reverse
		FrameStart = Float(Piece(StrTemp, 2, "|"))
		FrameEnd = Float(Piece(StrTemp, 1, "|"))
		Speed = -Float(Piece(StrTemp, 3, "|"))
	Else
		FrameStart = Float(Piece(StrTemp, 1, "|"))
		FrameEnd = Float(Piece(StrTemp, 2, "|"))
		Speed = Float(Piece(StrTemp, 3, "|"))
	EndIf
	
	If StrTemp <> ""
		Animate2(Obj, AnimTime(Obj), FrameStart, FrameEnd, Speed, Loop)
	EndIf
	
End Function

Function LoadPlayerModel()
	Local ModelScale# = (0.033 / 2.5)
	
	;[Block]
	me\ModelHead = LoadAnimMesh_Strict("GFX\Player\Body\Guard_Playermodel_Head.b3d", me\ModelPivot)
	ScaleEntity(me\ModelHead, ModelScale, ModelScale, ModelScale)
	MeshCullBox (me\ModelHead, -MeshWidth(me\ModelHead), -MeshHeight(me\ModelHead), -MeshDepth(me\ModelHead) * 5, MeshWidth(me\ModelHead) * 2, MeshHeight(me\ModelHead) * 2, MeshDepth(me\ModelHead) * 10)
	PositionEntity(me\ModelHead, 0, 0, -0.1)
	HideEntity(me\ModelHead)
	
	me\ModelTop = LoadAnimMesh_Strict("GFX\Player\Body\Guard_Playermodel_Top.b3d", me\ModelPivot)
	ScaleEntity(me\ModelTop, ModelScale, ModelScale, ModelScale)
	MeshCullBox (me\ModelTop, -MeshWidth(me\ModelTop), -MeshHeight(me\ModelTop), -MeshDepth(me\ModelTop) * 5, MeshWidth(me\ModelTop) * 2, MeshHeight(me\ModelTop) * 2, MeshDepth(me\ModelTop) * 10)
	PositionEntity(me\ModelTop, 0, 0, -0.1)
	HideEntity(me\ModelTop)
	
	me\ModelBottom = LoadAnimMesh_Strict("GFX\Player\Body\Guard_Playermodel_Bottom.b3d", me\ModelPivot)
	ScaleEntity(me\ModelBottom, ModelScale, ModelScale, ModelScale)
	MeshCullBox (me\ModelBottom, -MeshWidth(me\ModelBottom), -MeshHeight(me\ModelBottom), -MeshDepth(me\ModelBottom) * 5, MeshWidth(me\ModelBottom) * 2, MeshHeight(me\ModelBottom) * 2, MeshDepth(me\ModelBottom) * 10)
	PositionEntity(me\ModelBottom, 0, 0, -0.1)
	HideEntity(me\ModelBottom)
	;[End Block]
	
End Function

Function UpdatePlayerModel()
	Local HandRBone$, HandLBone$
	Local TopModel%, BottomModel%
	Local AnimationName$, Loop%, Reverse%
	Local PressedForwardKey%, PressedBackwardKey%, PressedLeftKey%, PressedRightKey%
	
	; ~ Determining Options
	;[Block]
	If opt\ShowPlayerModel
		me\ShowModel = True
	Else
		me\ShowModel = False
	EndIf
	;[End Block]
	
	; ~ Applying Key Locals
	;[Block]
	PressedForwardKey = KeyDown(key\MOVEMENT_UP)
	PressedBackwardKey = KeyDown(key\MOVEMENT_DOWN)
	PressedLeftKey = KeyDown(key\MOVEMENT_LEFT)
	PressedRightKey = KeyDown(key\MOVEMENT_RIGHT)
	;[End Block]
	
	; ~ Going Out Of The Function And Hiding Models If Option [me\ShowModel] Is False
	;[Block]
	If (Not me\ShowModel) Then HideEntity(me\ModelTop) : HideEntity(me\ModelBottom) : Return
	;[End Block]
	
	; ~ Applying Local Variables
	;[Block]
	If HandRBone = "" Then HandRBone = FindChild(me\ModelTop, "ValveBiped.Bip01_R_UpperArm")
	If HandLBone = "" Then HandLBone = FindChild(me\ModelTop, "ValveBiped.Bip01_L_UpperArm")
	If TopModel = 0 Then TopModel = me\ModelTop
	If BottomModel = 0 Then BottomModel = me\ModelBottom
	;[End Block]
	
	; ~ Removing Hand Bones If Item Is Held
	;[Block]
	If SelectedItem <> Null Lor wep_I\Using <> Unarmed
		;Animate2(TopModel, AnimTime(TopModel), 1190.0, 1469.0, 0.3)
		ScaleEntity(HandRBone, 0, 0, 0)
		ScaleEntity(HandLBone, 0, 0, 0)
	Else
		ScaleEntity(HandRBone, 1, 1, 1)
		ScaleEntity(HandLBone, 1, 1, 1)
	EndIf
	;[End Block]
	
	; ~ Making "Top" Model Animations Follow "Bottom" Model Animations
	;[Block]
	SetAnimTime(TopModel, AnimTime(BottomModel))
	;[End Block]
	
	; ~ Showing The Models
	;[Block]
	ShowEntity(me\ModelTop) : ShowEntity(me\ModelBottom)
	;[End Block]
	
	; ~ Attaching Head Model To Torso
	;[Block]
	EntityParent(me\ModelHead, FindChild(TopModel, "ValveBiped.Bip01_Head"))
	;[End Block]
	
	; ~ Updating The Animations
	;[Block]
	If me\ShowModel
		
		; ~ Updating Models' Position
		;[Block]
		PositionEntity(me\ModelPivot, EntityX(Camera), EntityY(Camera) - 0.87, EntityZ(Camera))
		RotateEntity(me\ModelPivot, 0.0, EntityYaw(Camera), 0.0)
		;[End Block]
		
		If me\Health > 0
			; ~ Normal Walking
			If (PressedForwardKey And (Not me\Sprinting) And (Not me\Crouch)) Lor me\ForceMove <> 0.0
				AnimationName = "anim_walk_forward" : Loop = True : Reverse = False
			ElseIf PressedBackwardKey And (Not me\Sprinting) And (Not me\Crouch)
				AnimationName = "anim_walk_backward" : Loop = True : Reverse = False
			ElseIf PressedLeftKey And (Not me\Sprinting) And (Not me\Crouch)
				AnimationName = "anim_walk_left" : Loop = True : Reverse = False
			ElseIf PressedRightKey And (Not me\Sprinting) And (Not me\Crouch)
				AnimationName = "anim_walk_right" : Loop = True : Reverse = False
			ElseIf PressedForwardKey And PressedLeftKey And (Not me\Sprinting) And (Not me\Crouch)
				AnimationName = "anim_walk_forward_left" : Loop = True : Reverse = False
			ElseIf PressedForwardKey And PressedRightKey And (Not me\Sprinting) And (Not me\Crouch)
				AnimationName = "anim_walk_forward_right" : Loop = True : Reverse = False
			ElseIf PressedBackwardKey And PressedLeftKey And (Not me\Sprinting) And (Not me\Crouch)
				AnimationName = "anim_walk_backward_left" : Loop = True : Reverse = False
			ElseIf PressedBackwardKey And PressedRightKey And (Not me\Sprinting) And (Not me\Crouch)
				AnimationName = "anim_walk_backward_right" : Loop = True : Reverse = False
			; ~ Crouch Walking
			ElseIf PressedForwardKey And me\Crouch
				AnimationName = "anim_crouch_walk_forward" : Loop = True : Reverse = False
			ElseIf PressedBackwardKey And me\Crouch
				AnimationName = "anim_crouch_walk_backward" : Loop = True : Reverse = False
			ElseIf PressedLeftKey And me\Crouch
				AnimationName = "anim_crouch_walk_left" : Loop = True : Reverse = False
			ElseIf PressedRightKey And me\Crouch
				AnimationName = "anim_crouch_walk_right" : Loop = True : Reverse = False
			ElseIf PressedForwardKey And PressedLeftKey And me\Crouch
				AnimationName = "anim_crouch_walk_forward_left" : Loop = True : Reverse = False
			ElseIf PressedForwardKey And PressedRightKey And me\Crouch
				AnimationName = "anim_crouch_walk_forward_right" : Loop = True : Reverse = False
			ElseIf PressedBackwardKey And PressedLeftKey And me\Crouch
				AnimationName = "anim_crouch_walk_backward_left" : Loop = True : Reverse = False
			ElseIf PressedBackwardKey And PressedRightKey And me\Crouch
				AnimationName = "anim_crouch_walk_backward_right" : Loop = True : Reverse = False
			; ~ Sprinting
			ElseIf PressedForwardKey And me\Sprinting And (Not me\Crouch)
				AnimationName = "anim_sprint_forward" : Loop = True : Reverse = False
			ElseIf PressedBackwardKey And me\Sprinting And (Not me\Crouch)
				AnimationName = "anim_sprint_backward" : Loop = True : Reverse = False
			ElseIf PressedLeftKey And me\Sprinting And (Not me\Crouch)
				AnimationName = "anim_sprint_left" : Loop = True : Reverse = False
			ElseIf PressedRightKey And me\Sprinting And (Not me\Crouch)
				AnimationName = "anim_sprint_right" : Loop = True : Reverse = False
			ElseIf PressedForwardKey And PressedLeftKey And me\Sprinting And (Not me\Crouch)
				AnimationName = "anim_sprint_forward_left" : Loop = True : Reverse = False
			ElseIf PressedForwardKey And PressedRightKey And me\Sprinting And (Not me\Crouch)
				AnimationName = "anim_sprint_forward_right" : Loop = True : Reverse = False
			ElseIf PressedBackwardKey And PressedLeftKey And me\Sprinting And (Not me\Crouch)
				AnimationName = "anim_sprint_backward_left" : Loop = True : Reverse = False
			ElseIf PressedBackwardKey And PressedRightKey And me\Sprinting And (Not me\Crouch)
				AnimationName = "anim_sprint_backward_right" : Loop = True : Reverse = False
			; ~ Normal Idle
			ElseIf me\CurrSpeed = 0.0 And (Not me\Crouch)
				AnimationName = "anim_idle" : Loop = True : Reverse = False
			; ~ Crouch Idle
			ElseIf me\CurrSpeed = 0.0 And me\Crouch
				AnimationName = "anim_crouch_idle" : Loop = True : Reverse = False
			; ~ Started Crouching
			ElseIf me\CurrSpeed = 0.0 And KeyDown(key\CROUCH)
				AnimationName = "anim_crouch_start" : Loop = False : Reverse = False
			; ~ Stopped Crouching
			ElseIf me\CurrSpeed = 0.0 And (Not KeyDown(key\CROUCH))
				AnimationName = "anim_crouch_stop" : Loop = False : Reverse = False
			EndIf
		Else
			AnimationName = "anim_idle" : Loop = True : Reverse = False
		EndIf
		
		; ~ Actual Animation Updating
		;[Block]
		AnimateByName(BottomModel, PlayerAnimationsFile, "Player", AnimationName, Loop, Reverse)
		;[End Block]
		
	EndIf
	;[End Block]
	
End Function

Function HealPlayer(Amount#)
	me\Health = Min(me\Health + Amount, 100)
End Function

Function DamagePlayer%(Damage#, Blur# = 0.0, VestFactor# = 0.0, HelmetFactor# = 0.0)
	me\Health = Max(me\Health - Damage - ((wi\BallisticVest = 1) * VestFactor) - ((wi\BallisticVest = 2) * VestFactor * 1.4) - (me\Crouch * wi\BallisticHelmet * HelmetFactor), 0)
	me\BlurTimer = me\BlurTimer + Blur
End Function

Function Kill%(IsBloody% = False)
	If chs\GodMode Then Return
	
	Local de.Decals
	
	StopBreathSound()
	
	If me\Health > 0
		If IsBloody
			If EntityHidden(t\OverlayID[Orl_blood]) Then ShowEntity(t\OverlayID[Orl_blood])
			de.Decals = CreateDecal(DECAL_BLOOD_6, PickedX(), PickedY() + 0.005, PickedZ(), 90.0, Rnd(360.0), 0.0, 0.1)
			de\SizeChange = 0.0025
			EntityParent(de\OBJ, PlayerRoom\OBJ)
		EndIf
		
		me\KillAnim = Rand(0, 1) : me\ForceMove = 0.0
		PlaySound_Strict(DamageSFX[0])
		If SelectedDifficulty\SaveType => SAVE_ON_QUIT
			DeleteGame(CurrSave)
			GameSaved = False
			LoadSavedGames()
		EndIf
		
		me\Health = 0
		ShowEntity(me\Head)
		PositionEntity(me\Head, EntityX(Camera, True), EntityY(Camera, True), EntityZ(Camera, True), True)
		ResetEntity(me\Head)
		RotateEntity(me\Head, 0.0, EntityYaw(Camera), 0.0)
	EndIf
End Function

Function UpdateCough%(Chance_%)
	If me\Health > 0
		If Rand(Chance_) = 1
			If (Not ChannelPlaying(CoughCHN))
				CoughCHN = PlaySound_Strict(CoughSFX((wi\GasMask > 0) Lor (wi\HazmatSuit > 0), Rand(0, 2)), True)
				me\SndVolume = Max(4.0, me\SndVolume)
			EndIf
		EndIf
	EndIf
	If ChannelPlaying(CoughCHN) Then StopBreathSound()
End Function

Function MakeMeUnplayable%()
	If me\Playable
		NullSelectedStuff()
		me\Playable = False
	EndIf
End Function

Function InteractObject%(OBJ%, Dist#, Arrow% = False, ArrowID% = 0, MouseDown_% = False)
	If MenuOpen Lor InvOpen Lor ConsoleOpen Lor I_294\Using Lor OtherOpen <> Null Lor d_I\SelectedDoor <> Null Lor SelectedScreen <> Null Lor me\Health = 0 Then Return
	
	If EntityDistanceSquared(me\Collider, OBJ) < Dist
		If EntityInView(OBJ, Camera)
			DrawArrowIcon[ArrowID] = Arrow
			DrawHandIcon = True
			If MouseDown_
				If DownKeyUse Then Return(True)
			Else
				If HitKeyUse Then Return(True)
			EndIf
		EndIf
	EndIf
	Return(False)
End Function

Function SetCrouch%(NewCrouch%)
	Local Temp%
	
	If NewCrouch <> me\Crouch
		PlaySound_Strict(CrouchSFX)
		me\SndVolume = Max(2.0, me\SndVolume)
		If (Not NewCrouch) And me\Stamina > 0.0
			me\Stamina = me\Stamina - Rnd(8.0, 16.0)
			If me\Stamina < 10.0
				If (Not ChannelPlaying(BreathCHN))
					Temp = 0
					If wi\GasMask > 0 Then Temp = 1
					BreathCHN = PlaySound_Strict(BreathSFX((Temp), 0), True)
				EndIf
			EndIf
		EndIf
		me\Crouch = NewCrouch
	EndIf
End Function

Function UpdateMoving%()
	CatchErrors("UpdateMoving()")
	
	Local de.Decals
	Local Sprint# = 1.0, Speed# = 0.018
	Local Pvt%, i%, Angle#
	
	If chs\SuperMan
		Speed = Speed * 3.0
		
		chs\SuperManTimer = chs\SuperManTimer + fps\Factor[0]
		
		me\CameraShake = Sin(chs\SuperManTimer / 5.0) * (chs\SuperManTimer / 1500.0)
		
		If chs\SuperManTimer > 70.0 * 50.0
			msg\DeathMsg = GetLocalString("console", "superman")
			Kill()
			If EntityHidden(t\OverlayID[Orl_Vignette]) Then ShowEntity(t\OverlayID[Orl_Vignette])
		Else
			me\BlurTimer = 500.0
			If (Not EntityHidden(t\OverlayID[Orl_Vignette])) Then HideEntity(t\OverlayID[Orl_Vignette])
		EndIf
	EndIf
	
	If me\DeathTimer > 0.0
		me\DeathTimer = me\DeathTimer - fps\Factor[0]
		If me\DeathTimer < 1.0 Then me\DeathTimer = -1.0
	ElseIf me\DeathTimer < 0.0 
		Kill()
	EndIf
	
	If me\Stamina < me\StaminaMax
		If me\CurrSpeed > 0.0
			me\Stamina = Min(me\Stamina + (0.15 * fps\Factor[0] / 1.25), 100.0)
		Else
			me\Stamina = Min(me\Stamina + (0.15 * fps\Factor[0] * 1.25), 100.0)
		EndIf
	EndIf
	
	If me\StaminaEffectTimer > 0.0
		me\StaminaEffectTimer = me\StaminaEffectTimer - (fps\Factor[0] / 70.0)
	Else
		me\StaminaEffect = 1.0
	EndIf
	
	Local SprintKeyAssigned = False
	
	If KeyDown(key\SPRINT) Then SprintKeyAssigned = True
	
	If (Not me\Crouch) And (SprintKeyAssigned) And me\Stamina > 0.0 And me\CurrSpeed > 0.0 And (Not me\Zombie) And wep_I\IronSight = 0 And (Not SandBoxMenuOpen) And (Not InvOpen) And OtherOpen = Null Then
		me\Sprinting = True
	Else
		me\Sprinting = False
	EndIf
	
	Local Temp#, Temp3%
	Local RID% = PlayerRoom\RoomTemplate\RoomID
	
	If me\Health > 0 And (Not chs\NoClip) And (RID <> r_dimension_106) And (me\Sprinting And (Not InvOpen) And OtherOpen = Null)
		If me\Stamina < 5.0
			If (Not ChannelPlaying(BreathCHN))
				Temp3 = 0
				If wi\GasMask > 0 Then Temp3 = 1
				BreathCHN = PlaySound_Strict(BreathSFX((Temp3), 0), True)
				ChannelVolume(BreathCHN, opt\VoiceVolume * opt\MasterVolume)
			EndIf
		ElseIf me\Stamina < 40.0
			If (Not ChannelPlaying(BreathCHN))
				Temp3 = 0
				If wi\GasMask > 0 Then Temp3 = 1
				BreathCHN = PlaySound_Strict(BreathSFX((Temp3), Rand(3)), True)
				ChannelVolume(BreathCHN, Min((70.0 - me\Stamina) / 70.0, 1.0) * opt\VoiceVolume * opt\MasterVolume)
			EndIf
		EndIf
	EndIf
	
	me\StaminaMax = 100.0
	
	If I_714\Using = 2
		me\Stamina = CurveValue(Min(10.0, me\Stamina), me\Stamina, 10.0)
		me\StaminaMax = Min(me\StaminaMax, 10.0)
		me\Sanity = Max(-850.0, me\Sanity)
	ElseIf I_714\Using = 1
		me\Stamina = CurveValue(Min(25.0, me\Stamina), me\Stamina, 15.0)
		me\StaminaMax = Min(me\StaminaMax, 25.0)
	Else
		If wi\BallisticVest = 2 Lor wi\HazmatSuit = 1
			me\Stamina = CurveValue(Min(60.0, me\Stamina), me\Stamina, 20.0)
			me\StaminaMax = Min(me\StaminaMax, 60.0)
		EndIf
		If wi\GasMask = 3 Lor wi\HazmatSuit = 3 Then me\Stamina = Min(100.0, me\Stamina + (100.0 - me\Stamina) * 0.002 * fps\Factor[0])
		If wi\GasMask = 4 Lor wi\HazmatSuit = 4 Then me\Stamina = Min(100.0, me\Stamina + (100.0 - me\Stamina) * 0.01 * fps\Factor[0])
	EndIf
	
	If me\Zombie
		If me\Crouch Then SetCrouch(False)
	EndIf
	
	If Abs(me\CrouchState - me\Crouch) < 0.001
		me\CrouchState = me\Crouch
	Else
		me\CrouchState = CurveValue(me\Crouch, me\CrouchState, 10.0)
	EndIf
	
	If (Not (d_I\SelectedDoor <> Null Lor SelectedScreen <> Null Lor I_294\Using))
		If (Not chs\NoClip)
			If me\Playable And me\FallTimer >= 0.0 And me\Health > 0
				If (KeyDown(key\MOVEMENT_DOWN) Xor KeyDown(key\MOVEMENT_UP)) Lor (KeyDown(key\MOVEMENT_RIGHT) Xor KeyDown(key\MOVEMENT_LEFT)) Lor me\ForceMove > 0.0 
					If (Not me\Crouch) And (me\Sprinting And (Not InvOpen) And OtherOpen = Null) And me\Stamina > 0.0
						me\Stamina = me\Stamina - (fps\Factor[0] * 0.4 * me\StaminaEffect)
						If me\Stamina <= 0.0 Then me\Stamina = -20.0
						Sprint = 2.5
					EndIf
					
					If RID = r_dimension_106
						Local PlayerPosY# = EntityY(me\Collider)
						
						If PlayerPosY < 2000.0 * RoomScale Lor PlayerPosY > 2608.0 * RoomScale
							Speed = 0.015
							If me\Stamina > 0.0
								me\Stamina = me\Stamina - (fps\Factor[0] * 0.5)
							Else
								me\Stamina = -20.0
							EndIf
						EndIf
					EndIf
					
					If InvOpen Lor OtherOpen <> Null Then Speed = 0.009
					
					If me\ForceMove > 0.0 Then Speed = Speed * me\ForceMove
					
					If SelectedItem <> Null
						If (SelectedItem\ItemTemplate\TempName = "firstaid" Lor SelectedItem\ItemTemplate\TempName = "finefirstaid" Lor SelectedItem\ItemTemplate\TempName = "firstaid2") And wi\HazmatSuit = 0 Then Sprint = 0.0
					EndIf
					
					Temp = (me\Shake Mod 360.0)
					
					If me\Playable Then me\Shake = ((me\Shake + fps\Factor[0] * Min(Sprint, 1.5) * 7.0) Mod 720.0)
					If Temp < 180.0 And (me\Shake Mod 360.0) >= 180.0
						Temp = GetStepSound(me\Collider)
						If DecalStep = 1
							Temp = 2
						ElseIf forest_event <> Null
							If forest_event\room = PlayerRoom And forest_event\EventState = 1.0 Then Temp = 4 ; ~ Improve somehow in future
						EndIf
						
						Local TempCHN% = 0, TempCHN2% = 0
						Local HasSprint% = True, StepRand% = Rand(0, 7)
						
						Select(Temp)
							Case 2, 3, 4
								;[Block]
								HasSprint = False
								StepRand = Rand(0, 2)
								;[End Block]
						End Select
						
						TempCHN = PlaySound_Strict(StepSFX(Temp, (Sprint = 2.5 And HasSprint), StepRand))
						ChannelVolume(TempCHN, (1.0 - (me\Crouch * 0.6)) * opt\SFXVolume * opt\MasterVolume)
						If DecalStep = 2
							TempCHN2 = PlaySound_Strict(Step2SFX[Rand(10, 11)])
							ChannelVolume(TempCHN2, (1.0 - (me\Crouch * 0.6)) * opt\SFXVolume * opt\MasterVolume)
						EndIf
						
						If Sprint = 2.5
							me\SndVolume = Max(4.0, me\SndVolume)
						Else
							me\SndVolume = Max(2.5 - (me\Crouch * 0.6), me\SndVolume)
						EndIf
					EndIf
				EndIf
				If KeyHit(key\CROUCH) And (Not me\Zombie) And me\Bloodloss < 60.0 And I_427\Timer < 70.0 * 390.0 And (SelectedItem = Null Lor (SelectedItem\ItemTemplate\TempName <> "firstaid" And SelectedItem\ItemTemplate\TempName <> "finefirstaid" And SelectedItem\ItemTemplate\TempName <> "firstaid2")) Then SetCrouch((Not me\Crouch))
			EndIf
		Else
			If (KeyDown(key\SPRINT) And (Not InvOpen) And OtherOpen = Null)
				Sprint = 2.5
			ElseIf KeyDown(key\CROUCH)
				Sprint = 0.5
			EndIf
		EndIf
		
		Local Temp2# = (Speed * Sprint) / (1.0 + me\CrouchState)
		
		If chs\NoClip
			me\Shake = 0.0
			me\CurrSpeed = 0.0
			me\Crouch = False
			
			RotateEntity(me\Collider, WrapAngle(EntityPitch(Camera)), WrapAngle(EntityYaw(Camera)), 0.0)
			
			Temp2 = Temp2 * chs\NoClipSpeed
			
			If KeyDown(key\MOVEMENT_DOWN) Then MoveEntity(me\Collider, 0.0, 0.0, (-Temp2) * fps\Factor[0])
			If KeyDown(key\MOVEMENT_UP) Then MoveEntity(me\Collider, 0.0, 0.0, Temp2 * fps\Factor[0])
			
			If KeyDown(key\MOVEMENT_LEFT) Then MoveEntity(me\Collider, (-Temp2) * fps\Factor[0], 0.0, 0.0)
			If KeyDown(key\MOVEMENT_RIGHT) Then MoveEntity(me\Collider, Temp2 * fps\Factor[0], 0.0, 0.0)
			
			ResetEntity(me\Collider)
		Else
			Temp = False
			If (Not me\Zombie) And me\FallTimer >= 0.0
				If KeyDown(key\MOVEMENT_DOWN) And me\Playable
					If (Not KeyDown(key\MOVEMENT_UP))
						Temp = True
						Angle = 180.0
						If KeyDown(key\MOVEMENT_LEFT)
							If (Not KeyDown(key\MOVEMENT_RIGHT)) Then Angle = 135.0
						ElseIf KeyDown(key\MOVEMENT_RIGHT)
							Angle = -135.0
						EndIf
					Else
						If KeyDown(key\MOVEMENT_LEFT)
							If (Not KeyDown(key\MOVEMENT_RIGHT))
								Temp = True
								Angle = 90.0
							EndIf
						ElseIf KeyDown(key\MOVEMENT_RIGHT)
							Temp = True
							Angle = -90.0
						EndIf
					EndIf
				ElseIf KeyDown(key\MOVEMENT_UP) And me\Playable
					Temp = True
					Angle = 0.0
					If KeyDown(key\MOVEMENT_LEFT)
						If (Not KeyDown(key\MOVEMENT_RIGHT)) Then Angle = 45.0
					ElseIf KeyDown(key\MOVEMENT_RIGHT)
						Angle = -45.0
					EndIf
				ElseIf me\ForceMove > 0.0
					Temp = True
					Angle = me\ForceAngle
				ElseIf me\Playable
					If KeyDown(key\MOVEMENT_LEFT)
						If (Not KeyDown(key\MOVEMENT_RIGHT))
							Temp = True
							Angle = 90.0
						EndIf
					ElseIf KeyDown(key\MOVEMENT_RIGHT)
						Temp = True
						Angle = -90.0
					EndIf
				EndIf
			Else
				Temp = True
				Angle = me\ForceAngle
			EndIf
			
			Angle = WrapAngle(EntityYaw(me\Collider, True) + Angle + 90.0)
			
			If Temp
				me\CurrSpeed = CurveValue(Temp2, me\CurrSpeed, 20.0)
			Else
				me\CurrSpeed = Max(CurveValue(0.0, me\CurrSpeed - 0.1, 1.0), 0.0)
			EndIf
			
			If me\Playable Then TranslateEntity(me\Collider, Cos(Angle) * me\CurrSpeed * fps\Factor[0], 0.0, Sin(Angle) * me\CurrSpeed * fps\Factor[0], True)
			
			Local CollidedFloor% = False
			
			For i = 1 To CountCollisions(me\Collider)
				If CollisionY(me\Collider, i) < EntityY(me\Collider) - 0.25
					CollidedFloor = True
					Exit
				EndIf
			Next
			
			If CollidedFloor
				If me\DropSpeed < -0.07
					Temp = GetStepSound(me\Collider)
					If DecalStep = 1
						Temp = 2
					ElseIf forest_event <> Null
						If forest_event\room = PlayerRoom And forest_event\EventState = 1.0 Then Temp = 4 ; ~ Improve somehow in future
					EndIf
					
					TempCHN = 0 : TempCHN2 = 0
					HasSprint = True : StepRand = Rand(0, 7)
					
					Select(Temp)
						Case 2, 3, 4
							;[Block]
							HasSprint = False
							StepRand = Rand(0, 2)
							;[End Block]
					End Select
					
					TempCHN = PlaySound_Strict(StepSFX(Temp, (Sprint = 2.5 And HasSprint), StepRand))
					ChannelVolume(TempCHN, (1.0 - (me\Crouch * 0.6)) * opt\SFXVolume * opt\MasterVolume)
					If DecalStep = 2
						TempCHN2 = PlaySound_Strict(Step2SFX[Rand(10, 11)])
						ChannelVolume(TempCHN2, (1.0 - (me\Crouch * 0.6)) * opt\SFXVolume * opt\MasterVolume)
					EndIf
				EndIf
				me\DropSpeed = 0.0
			Else
				If PlayerFallingPickDistance <> 0.0
					Local Pick# = LinePick(EntityX(me\Collider), EntityY(me\Collider), EntityZ(me\Collider), 0.0, -PlayerFallingPickDistance, 0.0)
					
					If Pick
						me\DropSpeed = Min(Max(me\DropSpeed - (0.006 * fps\Factor[0]), -2.0), 0.0)
					Else
						me\DropSpeed = 0.0
					EndIf
				Else
					me\DropSpeed = Min(Max(me\DropSpeed - (0.006 * fps\Factor[0]), -2.0), 0.0)
				EndIf
			EndIf
			PlayerFallingPickDistance = 10.0
			
			If me\Playable And ShouldEntitiesFall Then TranslateEntity(me\Collider, 0.0, me\DropSpeed * fps\Factor[0], 0.0)
		EndIf
		me\ForceMove = 0.0
	EndIf
	
	If me\Health < 50
		Temp2 = me\Bloodloss
		me\BlurTimer = Max(Max(Sin(MilliSec / 100.0) * me\Bloodloss * 30.0, me\Bloodloss * 2.0 * (2.0 - me\CrouchState)), me\BlurTimer)
		If (Not I_427\Using) And I_427\Timer < 70.0 * 360.0 Then me\Bloodloss = Min(me\Bloodloss + (Max(me\Health, 15) / 3000.0) * fps\Factor[0], 100.0)
		If Temp2 <= 60.0 And me\Bloodloss > 60.0 Then CreateMsg(GetLocalString("msg", "bloodloss"))
	EndIf
	
	Update008()
	Update409()
	
	If me\Bloodloss > 0.0 And me\VomitTimer >= 0.0
		If Rnd(200.0) < Max(me\Health, 1)
			Pvt = CreatePivot()
			PositionEntity(Pvt, EntityX(me\Collider) + Rnd(-0.05, 0.05), EntityY(me\Collider) - 0.05, EntityZ(me\Collider) + Rnd(-0.05, 0.05))
			TurnEntity(Pvt, 90.0, 0.0, 0.0)
			EntityPick(Pvt, 0.3)
			
			de.Decals = CreateDecal(Rand(DECAL_BLOOD_DROP_1, DECAL_BLOOD_DROP_2), PickedX(), PickedY() + 0.005, PickedZ(), 90.0, Rnd(360.0), 0.0, Rnd(0.03, 0.08) * Max(me\Health, 10))
			de\SizeChange = Rnd(0.001, 0.0015) : de\MaxSize = de\Size + Rnd(0.008, 0.009)
			EntityParent(de\OBJ, PlayerRoom\OBJ)
			TempCHN = PlaySound_Strict(DripSFX[Rand(0, 3)])
			ChannelVolume(TempCHN, Rnd(0.0, 0.8) * opt\SFXVolume * opt\MasterVolume)
			ChannelPitch(TempCHN, Rand(20000, 30000))
			
			FreeEntity(Pvt) : Pvt = 0
		EndIf
		
		me\CurrCameraZoom = Max(me\CurrCameraZoom, (Sin(Float(MilliSec) / 20.0) + 1.0) * me\Bloodloss * 0.2)
		
		If me\Bloodloss > 60.0 And (Not chs\NoClip)
			If (Not me\Crouch) Then SetCrouch(True)
		EndIf
		If me\Bloodloss >= 100.0
			me\HeartBeatVolume = 0.0
			Kill(True)
		ElseIf me\Bloodloss > 80.0
			me\HeartBeatRate = Max(150.0 - (me\Bloodloss - 80.0) * 5.0, me\HeartBeatRate)
			me\HeartBeatVolume = Max(me\HeartBeatVolume, 0.75 + (me\Bloodloss - 80.0) * 0.0125)
		ElseIf me\Bloodloss > 35.0
			me\HeartBeatRate = Max(70.0 + me\Bloodloss, me\HeartBeatRate)
			me\HeartBeatVolume = Max(me\HeartBeatVolume, (me\Bloodloss - 35.0) / 60.0)
		EndIf
	EndIf
	
	If me\HealTimer > 0.0
		Local FPSFactorEx# = fps\Factor[0] / 70.0
		
		me\HealTimer = Max(me\HealTimer - FPSFactorEx, 0.0)
		me\Bloodloss = Min(me\Bloodloss + FPSFactorEx / 3.0, 100.0)
		HealPlayer(FPSFactorEx / 30.0)
	EndIf
	
	If me\Playable
		If KeyHit(key\BLINK) Then me\BlinkTimer = 0.0
		If KeyDown(key\BLINK) And me\BlinkTimer < -10.0 Then me\BlinkTimer = -10.0
	EndIf
	
	If me\HeartBeatVolume > 0.0
		If me\HeartBeatTimer <= 0.0
			TempCHN = PlaySound_Strict(HeartBeatSFX)
			ChannelVolume(TempCHN, me\HeartBeatVolume * opt\SFXVolume * opt\MasterVolume)
			
			me\HeartBeatTimer = 70.0 * (60.0 / Max(me\HeartBeatRate, 1.0))
		Else
			me\HeartBeatTimer = me\HeartBeatTimer - fps\Factor[0]
		EndIf
		me\HeartBeatVolume = Max(me\HeartBeatVolume - fps\Factor[0] * 0.05, 0.0)
	EndIf
	
	CatchErrors("Uncaught: UpdateMoving()")
End Function

Type WearableItems
	Field GasMask%, GasMaskFogTimer#
	Field HazmatSuit%
	Field BallisticVest%
	Field BallisticHelmet%
	Field NightVision%, NVGTimer#, IsNVGBlinking%
	Field SCRAMBLE%
	Field Backpack%
	Field HDS%
End Type

Global wi.WearableItems

Global CameraPitch#

Function UpdateMouseLook%()
	CatchErrors("UpdateMouseLook()")
	
	Local p.Particles, wep.Weapons, CurrWep.Weapons
	Local i%
	Local FPSFactorEx# = fps\Factor[0] / 10.0
	
	me\CameraShake = Max(me\CameraShake - FPSFactorEx, 0.0)
	me\BigCameraShake = Max(me\BigCameraShake - FPSFactorEx, 0.0)
	
	For wep = Each Weapons
		If wep\ID = wep_I\Using Then
			If wep\WeaponType <> WeaponType_Melee Then
				CurrWep = wep
			EndIf
			Exit
		EndIf
	Next
	
	Local IronSight_AddFOV# = 0.0
	
	If CurrWep <> Null Then
		If KeyDown(key\SPRINT) And wep_I\IronSight Then
			IronSight_AddFOV = Abs(EntityX(wep_is\Pivot2%)/CurrWep\IronSightCoords\x) * 0.3
		Else
			IronSight_AddFOV = Abs(EntityX(wep_is\Pivot2%)/CurrWep\IronSightCoords\x) * 0.15
		EndIf
	EndIf
	
	CameraZoom(Camera, (Min(1.0 + (me\CurrCameraZoom / 400.0), 1.1) + IronSight_AddFOV) / (Tan((2.0 * ATan(Tan((opt\FOV) / 2.0) * (Float(opt\RealGraphicWidth) / Float(opt\RealGraphicHeight)))) / 2.0)))
	me\CurrCameraZoom = Max(me\CurrCameraZoom - fps\Factor[0], 0.0)
	
	If me\Health > 0 And me\FallTimer >= 0.0
		me\HeadDropSpeed = 0.0
		
		If IsNaN(EntityX(me\Collider))
			PositionEntity(me\Collider, EntityX(Camera, True), EntityY(Camera, True) - 0.5, EntityZ(Camera, True), True)
			CreateConsoleMsg(Format(GetLocalString("console", "xyz.reset"), EntityX(me\Collider)))
		EndIf
		
		Local Up# = (Sin(me\Shake) / (20.0 + me\CrouchState * 20.0)) * 0.6
		Local Roll#
		If SelectedDifficulty\Realism
			Roll# = Max(Min(Sin(me\Shake / 2.0) * 2.5 * 0.5, 8.0), -8.0)
		Else
			Roll# = Max(Min(Sin(me\Shake / 2.0) * 2.5 * 0.3, 8.0), -8.0)
		EndIf
		
		PositionEntity(Camera, EntityX(me\Collider), EntityY(me\Collider) + Up + 0.6 + me\CrouchState * (-0.3), EntityZ(me\Collider))
		RotateEntity(Camera, 0.0, EntityYaw(me\Collider), Roll * 0.5)
		
		; ~ Update the smoothing que to smooth the movement of the mouse
		Local Temp# = (opt\MouseSensitivity + 0.5)
		Local Temp2# = (5.0 / (opt\MouseSensitivity + 1.0)) * opt\MouseSmoothing
		
		If opt\InvertMouseX
			mo\Mouse_X_Speed_1 = CurveValue(-MouseXSpeed() * Temp, mo\Mouse_X_Speed_1, Temp2)
		Else
			mo\Mouse_X_Speed_1 = CurveValue(MouseXSpeed() * Temp, mo\Mouse_X_Speed_1, Temp2)
		EndIf
		If IsNaN(mo\Mouse_X_Speed_1) Then mo\Mouse_X_Speed_1 = 0.0
		If opt\InvertMouseY
			mo\Mouse_Y_Speed_1 = CurveValue(-MouseYSpeed() * Temp, mo\Mouse_Y_Speed_1, Temp2)
		Else
			mo\Mouse_Y_Speed_1 = CurveValue(MouseYSpeed() * Temp, mo\Mouse_Y_Speed_1, Temp2)
		EndIf
		If IsNaN(mo\Mouse_Y_Speed_1) Then mo\Mouse_Y_Speed_1 = 0.0
		
		If InvOpen Lor I_294\Using Lor OtherOpen <> Null Lor d_I\SelectedDoor <> Null Lor SelectedScreen <> Null Then StopMouseMovement()
		
		Local The_Yaw# = ((mo\Mouse_X_Speed_1)) * mo\Mouselook_X_Inc / (1.0 + wi\BallisticVest)
		Local The_Pitch# = ((mo\Mouse_Y_Speed_1)) * mo\Mouselook_Y_Inc / (1.0 + wi\BallisticVest)
		
		TurnEntity(me\Collider, 0.0, -The_Yaw, 0.0) ; ~ Turn the user on the Y (Yaw) axis
		CameraPitch = CameraPitch + The_Pitch
		; ~ Limit the user's camera to within 180.0 degrees of pitch rotation. Returns useless values so we need to use a variable to keep track of the camera pitch
		If CameraPitch > 70.0 Then CameraPitch = 70.0
		If CameraPitch < -70.0 Then CameraPitch = -70.0
		
		Local ShakeTimer# = me\CameraShake + me\BigCameraShake
		
		RotateEntity(Camera, WrapAngle(CameraPitch + Rnd(-ShakeTimer, ShakeTimer)), WrapAngle(EntityYaw(me\Collider) + Rnd(-ShakeTimer, ShakeTimer)), Roll) ; ~ Pitch the user's camera up and down
		
		If PlayerRoom\RoomTemplate\RoomID = r_dimension_106
			Local PlayerPosY# = EntityY(me\Collider)
			
			If PlayerPosY < 2000.0 * RoomScale Lor PlayerPosY > 2608.0 * RoomScale Then RotateEntity(Camera, WrapAngle(EntityPitch(Camera)), WrapAngle(EntityYaw(Camera)), Roll + WrapAngle(Sin(MilliSec / 150.0) * 30.0)) ; ~ Pitch the user's camera up and down
		EndIf
	Else
		If (Not EntityHidden(me\Collider)) Then HideEntity(me\Collider)
		PositionEntity(Camera, EntityX(me\Head), EntityY(me\Head), EntityZ(me\Head))
		
		Local CollidedFloor% = False
		
		For i = 1 To CountCollisions(me\Head)
			If CollisionY(me\Head, i) < EntityY(me\Head) - 0.01
				CollidedFloor = True
				Exit
			EndIf
		Next
		
		If CollidedFloor
			me\HeadDropSpeed = 0.0
		Else
			If (Not me\KillAnim)
				MoveEntity(me\Head, 0.0, 0.0, me\HeadDropSpeed)
				RotateEntity(me\Head, CurveAngle(-90.0, EntityPitch(me\Head), 20.0), EntityYaw(me\Head), EntityRoll(me\Head))
				RotateEntity(Camera, CurveAngle(EntityPitch(me\Head) - 40.0, EntityPitch(Camera), 40.0), EntityYaw(Camera), EntityRoll(Camera))
			Else
				MoveEntity(me\Head, 0.0, 0.0, -me\HeadDropSpeed)
				RotateEntity(me\Head, CurveAngle(90.0, EntityPitch(me\Head), 20.0), EntityYaw(me\Head), EntityRoll(me\Head))
				RotateEntity(Camera, CurveAngle(EntityPitch(me\Head) + 40.0, EntityPitch(Camera), 40.0), EntityYaw(Camera), EntityRoll(Camera))
			EndIf
			me\HeadDropSpeed = me\HeadDropSpeed - (0.002 * fps\Factor[0])
		EndIf
	EndIf
	
	UpdateDust()
	
	; ~ Limit the mouse's movement. Using this method produces smoother mouselook movement than centering the mouse each loop
	If (Not (MenuOpen Lor InvOpen Lor ConsoleOpen Lor I_294\Using Lor OtherOpen <> Null Lor d_I\SelectedDoor <> Null))
		If (MousePosX > mo\Mouse_Right_Limit) Lor (MousePosX < mo\Mouse_Left_Limit) Lor (MousePosY > mo\Mouse_Bottom_Limit) Lor (MousePosY < mo\Mouse_Top_Limit) Then MoveMouse(mo\Viewport_Center_X, mo\Viewport_Center_Y)
	EndIf
	
	If wi\GasMask > 0 Lor wi\HazmatSuit > 0
		If wi\HazmatSuit > 0
			If EntityHidden(t\OverlayID[Orl_hazmat]) Then ShowEntity(t\OverlayID[Orl_hazmat])
		Else
			If EntityHidden(t\OverlayID[Orl_GasMask]) Then ShowEntity(t\OverlayID[Orl_GasMask])
		EndIf
		
		If me\Health > 0
			If (Not ChannelPlaying(BreathCHN))
				If (Not ChannelPlaying(BreathGasRelaxedCHN))
					BreathGasRelaxedCHN = PlaySound_Strict(BreathGasRelaxedSFX, True)
					ChannelVolume(BreathGasRelaxedCHN, opt\VoiceVolume * opt\MasterVolume)
				EndIf
			Else
				If ChannelPlaying(BreathGasRelaxedCHN) Then StopChannel(BreathGasRelaxedCHN) : BreathGasRelaxedCHN = 0
			EndIf
		EndIf
		
		If wi\GasMask <> 2 And wi\GasMask <> 4 And wi\HazmatSuit <> 2 And wi\HazmatSuit <> 4
			If ChannelPlaying(BreathCHN)
				wi\GasMaskFogTimer = Min(wi\GasMaskFogTimer + (fps\Factor[0] * Rnd(0.5, 1.6)), 100.0)
			Else
				wi\GasMaskFogTimer = Max(0.0, wi\GasMaskFogTimer - (fps\Factor[0] * 0.3))
			EndIf
			If EntityHidden(t\OverlayID[Orl_gasmask_fog]) Then ShowEntity(t\OverlayID[Orl_gasmask_fog])
			EntityAlpha(t\OverlayID[Orl_gasmask_fog], Min(PowTwo(wi\GasMaskFogTimer * 0.2) / 1000.0, 0.45))
		EndIf
	Else
		If ChannelPlaying(BreathGasRelaxedCHN) Then StopChannel(BreathGasRelaxedCHN) : BreathGasRelaxedCHN = 0
		wi\GasMaskFogTimer = Max(0.0, wi\GasMaskFogTimer - (fps\Factor[0] * 0.3))
		If (Not EntityHidden(t\OverlayID[Orl_GasMask])) Then HideEntity(t\OverlayID[Orl_GasMask])
		If (Not EntityHidden(t\OverlayID[Orl_hazmat])) Then HideEntity(t\OverlayID[Orl_hazmat])
		If (Not EntityHidden(t\OverlayID[Orl_gasmask_fog])) Then HideEntity(t\OverlayID[Orl_gasmask_fog])
	EndIf
	
	If wi\BallisticHelmet
		If EntityHidden(t\OverlayID[Orl_helmet]) Then ShowEntity(t\OverlayID[Orl_helmet])
	Else
		If (Not EntityHidden(t\OverlayID[Orl_helmet])) Then HideEntity(t\OverlayID[Orl_helmet])
	EndIf
	
	If wi\NightVision > 0 Lor wi\SCRAMBLE > 0
		If EntityHidden(t\OverlayID[Orl_NVG]) Then ShowEntity(t\OverlayID[Orl_NVG])
		If (Not EntityHidden(t\OverlayID[Orl_Vignette])) Then HideEntity(t\OverlayID[Orl_Vignette])
		If wi\NightVision = 2
			EntityColor(t\OverlayID[Orl_NVG], 0.0, 100.0, 200.0)
		ElseIf wi\NightVision = 3
			EntityColor(t\OverlayID[Orl_NVG], 200.0, 0.0, 0.0)
		ElseIf wi\NightVision = 1
			EntityColor(t\OverlayID[Orl_NVG], 0.0, 200.0, 0.0)
		Else
			EntityColor(t\OverlayID[Orl_NVG], 200.0, 200.0, 200.0)
		EndIf
	Else
		If (Not EntityHidden(t\OverlayID[Orl_NVG])) Then HideEntity(t\OverlayID[Orl_NVG])
		If EntityHidden(t\OverlayID[Orl_Vignette]) Then ShowEntity(t\OverlayID[Orl_Vignette])
	EndIf
	
	Update1025()
	
	CatchErrors("Uncaught: UpdateMouseLook()")
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D TSS