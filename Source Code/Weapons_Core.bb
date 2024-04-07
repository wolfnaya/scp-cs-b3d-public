;! ~ [Weapon Constants]

; ~ Weapon ID Constants
;[Block]
Const Unarmed% = 0
Const Wep_Knife% = 1, Wep_Crowbar% = 2
Const Wep_USP% = 3, Wep_M9% = 4, Wep_FS% = 5, Wep_G17% = 6, Wep_TT33% = 7
Const Wep_P90% = 8, Wep_MP5% = 9, Wep_MP7% = 10, Wep_UMP% = 11
Const Wep_AK% = 12, Wep_AR15% = 13, Wep_Scar% = 14, Wep_FN2000% = 15, Wep_XM29% = 16
Const Wep_M870% = 17, Wep_Spas12% = 18, Wep_M67% = 19, Wep_RGD5% = 20, Wep_RGN% = 21
Const Wep_SCP_127% = 22, Wep_EMRP% = 23
;[End Block]

; ~ Weapon Type ID Constants
;[Block]
Const WeaponType_Melee% = 0, WeaponType_Handgun% = 1, WeaponType_SMG% = 2, WeaponType_Rifle% = 3, WeaponType_Shotgun% = 4
;[End Block]

; ~ Weapon Quick Slot ID Constants
;[Block]
Const QuickSlot_Primary% = 1, QuickSlot_Secondary% = 2, QuickSlot_Holster% = 3, QuickSlot_Scabbard% = 4
;[End Block]

; ~ Weapon State ID Constants
;[Block]
Const Wep_State_Draw% = 0, Wep_State_Idle% = 1, Wep_State_Attack% = 2, Wep_State_Reload% = 3, Wep_State_Inspect% = 4, Wep_State_CheckAmmo% = 5, Wep_State_Jam% = 6, Wep_State_FireMode% = 7, Wep_State_Lower% = 8, Wep_State_Holster% = 9
;[End Block]

; ~ Weapon Reload State ID Constants
;[Block]
Const ReloadState_Start% = 0, ReloadState_Loop% = 1, ReloadState_End% = 2
;[End Block]

; ~ Weapon Attachment ID Constants
;[Block]
Const Att_Barrel_Suppressor% = 0, Att_Barrel_Compensator% = 1, Att_Barrel_Match% = 2
Const Att_Sight_RedDot% = 3, Att_Sight_EoTech% = 4, Att_Sight_Reflex% = 5, Att_Sight_Acog% = 6, Att_Sight_Special% = 7
Const Att_Magazine_Reduced% = 8, Att_Magazine_Extended% = 9
Const Att_Special% = 10
;[End Block]

; ~ Weapon Caliber ID Constants
;[Block]
Const Cal_5_7X28mm% = 0, Cal_9mm% = 1, Cal_10mm% = 2, Cal_45ACP% = 3Const Cal_5_56X45mm% = 4, Cal_7_62X39mm% = 5
Const Cal_12X70mm% = 6
Const Cal_GAUSS% = 7
;[End Block]

; ~ Weapon Fire Modes ID Constants
;[Block]
Const FireMode_Single% = 0, FireMode_Auto% = 1
;[End Block]

; ~ Weapon Aim Cross ID Constants
;[Block]
Const AimCross_Debug% = 0, AimCross_P90% = 1, AimCross_RedDot% = 2, AimCross_EoTech% = 3, AimCross_Reflex% = 4, AimCross_Acog% = 5, AimCross_EMRP% = 6
;[End Block]

; ~ Weapon Max Constants
;[Block]
Const MaxWeaponSlots% = 4
Const MaxWeaponAttachments% = 11
Const MaxWeaponCaliberTypes% = 8
Const MaxWeaponAimCrossImages% = 6
;[End Block]

;! ~ [Weapon Types]

; ~ Weapon Instance Type
;[Block]
Type WeaponInstance
	Field Using%
	Field Pivot%
	Field RotationPitch#, RotationYaw#
	Field CurrentWeaponSlot%[MaxWeaponSlots], SelectedSlot%, MaxSlots%[MaxWeaponSlots], SlotTimer#, HUDTimer#
	Field Light%, LightTimer%
	Field AnimFlag%, ChangeFlag%
	Field AimCrossIMG%[MaxWeaponAimCrossImages]
	Field IronSight%, IronSightAnim%
	Field Sound%, SoundCHN%, SelectSFX%, DenySFX%
End Type
;[End Block]

Global wep_I.WeaponInstance

; ~ Weapon Type
;[Block]
Type Weapons
	Field ID%, WeaponType%, Name$
	Field Model%, WModel%, HandsModel%, IronSightCoords.Vector3D
	Field State#, ReloadState#, FrameSeparation%
	Field CurrentAmmo%, MaximumCurrentAmmo%, Caliber%, BulletAmount%, FireMode%
	Field Damage#, Accuracy#, Range#, FireRate#, AttackState#, AttackTimer#
	Field Knockback#, VerticalRecoil#
	Field HasAttachment%[MaxWeaponAttachments], CanHaveAttachment%[MaxWeaponAttachments]
	Field MuzzleFlash%, AimCross%, CanAim%, Found%, PlayingAnimation%
	Field Temperature#, MaximumTemperature#, CoolTimer#;, CurrentAnimation.Vector3D
	Field AttackSounds%, ReloadSounds%
End Type
;[End Block]

; ~ Weapon Pivot Type
;[Block]
Type WeaponPivot
	Field PickPivot%
	Field Pivot_Y#
	Field Pivot_YSide% = 0
	Field Pivot_X#
	Field Pivot_XSide% = 0
End Type
;[End Block]

Global wep_p.WeaponPivot = New WeaponPivot

; ~ Weapon Iron Sight Type
;[Block]
Type IronSight
	Field Pivot%, Pivot2%
	Field Timer#, ShowingAimCross%
End Type
;[End Block]

Global wep_is.IronSight = New IronSight

;! ~ [Weapon Functions]

; ~ Function To Create Weapons
;[Block]
Function CreateWeapon.Weapons(ID%, Model$, WeaponType%, CanAim%, FrameSeparation%, BulletAmount%, Caliber%, MaximumCurrentAmmo%, Damage#, Accuracy#, Range#, Firerate#, KnockBack#, VerticalRecoil#, MaximumTemperature#)
	CatchErrors("CreateWeapon.Weapons()")
	
	Local wep.Weapons = New Weapons
	Local StrTemp$, VectorString$, TexString$, SndString%
	
	; ~ Assigning All Variables Here
	;[Block]
	wep\ID = ID : wep\WeaponType = WeaponType : wep\CanAim = CanAim : wep\Name = Model
	wep\BulletAmount = BulletAmount : wep\Caliber = Caliber : wep\MaximumCurrentAmmo = MaximumCurrentAmmo
	wep\Damage = Damage : wep\Accuracy = Accuracy : wep\Range = Range : wep\FireRate = Firerate
	wep\Knockback = KnockBack : wep\VerticalRecoil = VerticalRecoil : wep\FrameSeparation = FrameSeparation : wep\MaximumTemperature = MaximumTemperature
	;[End Block]
	
	; ~ Loading The View Model
	;[Block]
	wep\Model = LoadAnimMesh_Strict("GFX\Items\Weapons\" + Model + "_V.b3d", wep_I\Pivot)
	ScaleEntity(wep\Model, 0.005, 0.005, 0.005)
	MeshCullBox(wep\Model, -MeshWidth(wep\Model) * 3, -MeshHeight(wep\Model) * 3, -MeshDepth(wep\Model) * 3, MeshWidth(wep\Model) * 6,MeshHeight(wep\Model) * 6, MeshDepth(wep\Model) * 6)
	HideEntity(wep\Model)
	;[End Block]
	
	; ~ Loading The World Model
	;[Block]
	StrTemp = IniGetInt("Data\Weapons.ini", Model, "world_scale")
	wep\WModel = LoadAnimMesh_Strict("GFX\Items\Weapons\" + Model + "_W.b3d")
	ScaleEntity(wep\WModel, StrTemp, StrTemp, StrTemp)
	HideEntity(wep\WModel)
	;[End Block]
	
	; ~ Loading Hands Model
	;[Block]
	wep\HandsModel = LoadAnimMesh_Strict("GFX\Player\Hands\Guard_Hands.b3d", wep_I\Pivot)
	ScaleEntity(wep\HandsModel, 0.005, 0.005, 0.005)
	MeshCullBox(wep\HandsModel, -MeshWidth(wep\HandsModel) * 3, -MeshHeight(wep\HandsModel) * 3, -MeshDepth(wep\HandsModel) * 3, MeshWidth(wep\HandsModel) * 6, MeshHeight(wep\HandsModel) * 6, MeshDepth(wep\HandsModel) * 6)
	HideEntity(wep\HandsModel)
	;[End Block]
	
	; ~ Texturing The Hands
	;[Block]
	If gm\ID < GM_CLASS_D
		If gm\ID = GM_RYAN
			TexString = LoadTexture_Strict("GFX\Player\Hands\Guard_Hands.png")
		ElseIf gm\ID = GM_NTF
			TexString = LoadTexture_Strict("GFX\Player\Hands\NTF_Hands.png")
		EndIf
		EntityTexture(wep\HandsModel, TexString)
	EndIf
	;[End Block]
	
	; ~ Creating Muzzle Flash
	;[Block]
	wep\MuzzleFlash = CreateSprite(wep\Model)	
	EntityFX(wep\MuzzleFlash, 1) : SpriteViewMode(wep\MuzzleFlash, 2)
	HideEntity(wep\MuzzleFlash)
	;[End Block]
	
	; ~ Creating Aim Cross
	;[Block]
	wep\AimCross = CreateSprite(wep\Model)	
	EntityFX(wep\AimCross, 1) : SpriteViewMode(wep\AimCross, 2)
	HideEntity(wep\AimCross)
	;[End Block]
	
	; ~ Parenting Weapon Model To Hands
	;[Block]
;	EntityParent(wep\Model, FindChild(wep\HandsModel, "ValveBiped.Bip01_R_Hand"))
;	PositionEntity(wep\Model, EntityX(wep\HandsModel, True), EntityY(wep\HandsModel, True), EntityZ(wep\HandsModel, True))
;	RotateEntity(wep\Model, EntityPitch(wep\HandsModel, True), EntityYaw(wep\HandsModel, True), EntityRoll(wep\HandsModel, True))
	;[End Block]
	
	; ~ Assigning Weapon Position
	;[Block]
	VectorString = IniGetString("Data\Weapons.ini", Model, "weapon_offset","")
	MoveEntity wep\Model, Piece(VectorString, 1, "|"), Piece(VectorString, 2, "|"), Piece(VectorString, 3, "|")
	MoveEntity wep\HandsModel, Piece(VectorString, 1, "|"), Piece(VectorString, 2, "|"), Piece(VectorString, 3, "|")
	;[End Block]
	
	; ~ Assigning Weapon Iron Sight Position
	;[Block]
	VectorString = IniGetString("Data\Weapons.ini", Model, "weapon_aiming_offset","")
	If VectorString <> "" Then wep\IronSightCoords = CreateVector3D(Piece(VectorString, 1, "|"),Piece(VectorString, 2, "|"),Piece(VectorString, 3, "|"))
	;[End Block]
	
	; ~ Parenting Muzzle Flash
	;[Block]
	wep\MuzzleFlash = CreateSprite()
	EntityFX(wep\MuzzleFlash, 1) : SpriteViewMode(wep\MuzzleFlash, 2)
	EntityParent(wep\MuzzleFlash, wep\Model)
	;[End Block]
	
	; ~ Determinig Sound Amounts
	;[Block]
	SndString = IniGetInt("Data\Weapons.ini", Model, "weapon_attack_sounds", 1)
	wep\AttackSounds = SndString
	SndString = IniGetInt("Data\Weapons.ini", Model, "weapon_reload_sounds", 1)
	wep\ReloadSounds = SndString
	;[End Block]
	
	Return(wep)
	
	CatchErrors("Uncaught: CreateWeapon.Weapons()")
End Function
;[End Block]

; ~ Loading Weapons
;[Block]
Function LoadWeapons()
	CatchErrors("LoadWeapons()")
	
	Local it.ItemTemplates, wep.Weapons, i%
	
	wep_I.WeaponInstance = New WeaponInstance
	
	; ~ Assigning Pivots
	;[Block]
	wep_I\Pivot = CreatePivot()
	wep_p\PickPivot = CreatePivot(wep_I\Pivot) : wep_is\Pivot = CreatePivot(wep_I\Pivot) : wep_is\Pivot2 = CreatePivot(wep_I\Pivot)
	;[End Block]
	
	; ~ Creating Weapon Light
	;[Block]
	wep_I\Light = CreateLight(2, wep_I\Pivot)
	LightRange(wep_I\Light, 0.4)
	HideEntity(wep_I\Light)
	wep_I\LightTimer = 0.0
	;[End Block]
	
	; ~ Assigning Variables
	;[Block]
	wep_I\AnimFlag = False
	wep_I\ChangeFlag = False
	For i = 0 To MaxWeaponSlots - 1
		wep_I\CurrentWeaponSlot[i] = 0
	Next
	wep_I\RotationPitch = 0
	wep_I\RotationYaw = 0
	me\CanUseWeapons = True
	wep_I\IronSight = False : wep_I\IronSightAnim = False
	;[End Block]
	
	; ~ Loading Scope Dots
	;[Block]
	wep_I\AimCrossIMG[AimCross_Debug] = LoadImage_Strict("GFX\HUD\Aim_Cross.png") : MidHandle wep_I\AimCrossIMG[AimCross_Debug]
	wep_I\AimCrossIMG[AimCross_RedDot] = LoadTexture_Strict("GFX\Items\Weapons\Scopes\wpn_red_dot.png", 1 + 2, DeleteAllTextures)
	wep_I\AimCrossIMG[AimCross_Reflex] = LoadTexture_Strict("GFX\Items\Weapons\Scopes\wpn_red_dot.png", 1 + 2, DeleteAllTextures)
	wep_I\AimCrossIMG[AimCross_EoTech] = LoadTexture_Strict("GFX\Items\Weapons\Scopes\wpn_eot_dot.png", 1 + 2, DeleteAllTextures)
	wep_I\AimCrossIMG[AimCross_Acog] = LoadTexture_Strict("GFX\Items\Weapons\Scopes\wpn_acog_dot.png", 1 + 2, DeleteAllTextures)
	wep_I\AimCrossIMG[AimCross_P90] = LoadTexture_Strict("GFX\Items\Weapons\Scopes\wpn_p90_dot.png", 1 + 2, DeleteAllTextures)
	wep_I\AimCrossIMG[AimCross_EMRP] = LoadTexture_Strict("GFX\Items\Weapons\Scopes\wpn_emrp_dot.png", 1 + 2, DeleteAllTextures)
	;[End Block]
	
	; ~ Loading UI Sounds
	;[Block]
	wep_I\SelectSFX = LoadSound_Strict("SFX\Weapons\SelectSFX.ogg")
	wep_I\DenySFX = LoadSound_Strict("SFX\Weapons\DenySFX.ogg")
	;[End Block]
	
	; ~ Creating Weapons
	;[Block]
	wep.Weapons = CreateWeapon(Wep_Knife, "Knife", WeaponType_Melee, False, 200, 0, -1, 0, 10.0, 1.0, 1.0, 4.2, 0.0, 0.0, -1)
	wep.Weapons = CreateWeapon(Wep_Crowbar, "Crowbar", WeaponType_Melee, False, 118, 0, -1, 0, 30.0, 0.7, 3.0, 2.2, 0.0, 0.0, -1)
	wep.Weapons = CreateWeapon(Wep_USP, "Usp", WeaponType_Handgun, True, 1356, 1, Cal_45ACP, 12, 20.0, 0.9, 8000.0, 8.0, 2.1, 4.3, Rand(400, 800)) : wep\FireMode = FireMode_Single
	wep.Weapons = CreateWeapon(Wep_P90, "P90", WeaponType_SMG, True, 0, 1, Cal_5_7X28mm, 50, 11.0, 0.8, 7000.0, 4.0, 0.5, 1.6, Rand(1000, 2000)) : wep\FireMode = FireMode_Auto
	wep.Weapons = CreateWeapon(Wep_Spas12, "Spas12", WeaponType_Shotgun, True, 0, 6, Cal_12X70mm, 8, 31.0, 5.0, 5000.0, 12.0, 1.1, 2.1, Rand(1200, 2100)) : wep\FireMode = FireMode_Single
	;[End Block]
	
	; ~ Creating Items
	;[Block]
	it.ItemTemplates = CreateItemTemplate(GetLocalString("items", "knife"), "Combat Knife", "knife", "Weapons\Knife_W.b3d", "Weapons\INVknife.jpg", "", 0.006, 66, 0.8, -1, "", "Weapons\Slots\slot_knife.png") : it\IsWeapon = Wep_Knife : it\ISScabbard = True
	it.ItemTemplates = CreateItemTemplate(GetLocalString("items", "crowbar"), "Crowbar", "crowbar", "Weapons\Crowbar_W.b3d", "Weapons\INVcrowbar.jpg", "", 0.006, 66, 1.4, -1, "", "Weapons\Slots\slot_crowbar.png") : it\IsWeapon = Wep_Crowbar
	it.ItemTemplates = CreateItemTemplate("H&K USP", "H&K USP", "usp", "Weapons\USP_W.b3d", "Weapons\INVusp.jpg", "", 0.02, 66, 3.5, -1, "", "Weapons\Slots\slot_usp.png") : it\IsWeapon = Wep_USP : it\IsHolster = True
	it.ItemTemplates = CreateItemTemplate("FN P90", "FN P90", "p90", "Weapons\P90_W.b3d", "Weapons\INVp90.jpg", "", 0.02, 66, 5.2, -1, "", "Weapons\Slots\slot_p90.png") : it\IsWeapon = Wep_P90
	it.ItemTemplates = CreateItemTemplate("Franchi SPAS-12", "Franchi SPAS-12", "spas12", "Weapons\Spas12_W.b3d", "Weapons\INVspas12.jpg", "", 0.006, 66, 7.2, -1, "", "Weapons\Slots\slot_spas12.png") : it\IsWeapon = Wep_Spas12
	;[End Block]
	
	; ~ Removing All Weapon Attachments
	;[Block]
	For wep.Weapons = Each Weapons
		If FindChild(wep\Model, "att_suppressor") <> 0 Then ScaleEntity(FindChild(wep\Model, "att_suppressor"), 0.0, 0.0, 0.0)
		If FindChild(wep\Model, "att_rail") <> 0 Then ScaleEntity(FindChild(wep\Model, "att_rail"), 0.0, 0.0, 0.0)
		If FindChild(wep\Model, "att_weapon_scope") <> 0 Then ScaleEntity(FindChild(wep\Model, "att_weapon_scope"), 0.0, 0.0, 0.0)
	Next
	;[End Block]
	
	CatchErrors("Uncaught: LoadWeapons()")
End Function
;[End Block]

; ~ Function To Play Specific Weapon Animation
;[Block]
Function AnimateWeapon(wep.Weapons, Name$, AnimCount% = 1, Loop% = False, Reverse% = False, StateChange% = -1, SoundName$ = "", SoundAmount% = 1)
	CatchErrors("AnimateWeapon()")
	
	Local FrameStart#, FrameEnd#, Speed#, StrTemp$
	
	If AnimCount = 1
		StrTemp = IniGetString("Data\Weapons.ini", wep\Name, Name)
	Else
		StrTemp = IniGetString("Data\Weapons.ini", wep\Name, Name + Str(Rand(1, AnimCount)))
	EndIf
	
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
		Animate2(wep\HandsModel, AnimTime(wep\HandsModel), FrameStart, FrameEnd, Speed, Loop)
;		If wep\CurrentAnimation <> Null Then FreeVector3D(wep\CurrentAnimation) ; ~ TODO: Find A Better Solution - Wolfnaya
;		wep\CurrentAnimation = CreateVector3D(FrameStart, FrameEnd, Speed)
;		Animate2(wep\HandsModel, AnimTime(wep\HandsModel), wep\CurrentAnimation\x, wep\CurrentAnimation\y, wep\CurrentAnimation\z, Loop)
	EndIf
	If AnimTime(wep\HandsModel) = FrameStart And SoundName <> "" Then PlayWeaponSound(wep, SoundName, SoundAmount)
	
	If StateChange <> -1 And wep\State <> StateChange And (Not Loop)
		If AnimTime(wep\HandsModel) >= (FrameEnd - 0.5) Then wep\State = StateChange
	EndIf
	
	If AnimTime(wep\HandsModel) < (FrameEnd - 0.4)
		wep\PlayingAnimation = True
	Else
		wep\PlayingAnimation = False
	EndIf
	
	CatchErrors("Uncaught: AnimateWeapon()")
End Function
;[End Block]

; ~ Function To Play Specific Weapon Sound
;[Block]
Function PlayWeaponSound(wep.Weapons, Name$, Max_Amount% = 1)
	CatchErrors("Uncaught: PlayWeaponSound()")
	
	If Max_Amount = 1
		wep_I\Sound = LoadSound_Strict("SFX\Weapons\" + wep\Name + "\" + Name + ".ogg")
	Else
		wep_I\Sound = LoadSound_Strict("SFX\Weapons\" + wep\Name + "\" + Name + Rand(1, Max_Amount) + ".ogg")
	EndIf
	wep_I\SoundCHN = PlaySound_Strict(wep_I\Sound)
		
	CatchErrors("Uncaught: PlayWeaponSound()")
End Function
;[End Block]

; ~ Updating Weapons
;[Block]
Function UpdateWeapons()
	CatchErrors("UpdateWeapons()")
	
	Local wep.Weapons, wep2.Weapons, n.NPCs, p.Particles
	Local StrTemp$, i%, j%, Attacking%, AttackCondition%, ReloadCondition%, IsFullReload%
	Local PressedAttack% = mo\MouseHit1, HeldAttack% = mo\MouseDown1
	Local PressedReload% = KeyHit(key\RELOAD)
	Local PressedHolster% = KeyHit(key\HOLSTER)
	Local PressedInspect% = KeyHit(key\INSPECT)
	Local PressedCheckAmmo% = KeyHit(key\CHECKAMMO)
	Local PressedLower% = KeyHit(key\LOW)
	Local PressedFiremode% = KeyHit(key\FIREMODE)
	
	; ~ Updating Weapon Sway
	;[Block]
	Local CamPitch#, CamYaw#
	
	CamPitch# = EntityPitch(Camera) + 180
	CamYaw# = EntityYaw(Camera)
	
	Local WepPivotPitch#, WepPivotYaw#, Pitch#, Yaw#
	
	WepPivotPitch# = EntityPitch(wep_I\Pivot) + 180
	WepPivotYaw# = EntityYaw(wep_I\Pivot)
	
	If SelectedDifficulty\Realism
		If opt\InvertMouseY
			wep_I\RotationPitch = Clamp(wep_I\RotationPitch - mo\Mouse_Y_Speed_1/4, -10, 10)
		Else
			wep_I\RotationPitch = Clamp(wep_I\RotationPitch + mo\Mouse_Y_Speed_1/4, -10, 10)
		EndIf
		If opt\InvertMouseX
			wep_I\RotationYaw = Clamp(wep_I\RotationYaw - mo\Mouse_X_Speed_1/4, -10, 10)
		Else
			wep_I\RotationYaw = Clamp(wep_I\RotationYaw + mo\Mouse_X_Speed_1/4, -10, 10)
		EndIf
		
		Pitch = (EntityPitch(Camera) + wep_I\RotationPitch)
		Yaw = (EntityYaw(Camera) - wep_I\RotationYaw)
		
		RotateEntity wep_I\Pivot, Pitch, Yaw, 0
	Else
		If wep_I\IronSight
			If KeyDown(key\SPRINT) Then
				Pitch = CamPitch
				Yaw = CamYaw
			Else
				Pitch# = Clamp(CurveAngle(CamPitch, WepPivotPitch, 2.1), CamPitch - 5, CamPitch + 5)
				Yaw# = CurveAngle(CamYaw, WepPivotYaw, 2.1)
				Yaw = ClampAngle(Yaw, CamYaw, 5)
			EndIf
		Else
			Pitch# = Clamp(CurveAngle(CamPitch, WepPivotPitch, 10.0), CamPitch - 5, CamPitch + 5)
			Yaw# = CurveAngle(CamYaw, WepPivotYaw, 10.0)
			Yaw = ClampAngle(Yaw, CamYaw, 5)
		EndIf
		RotateEntity wep_I\Pivot, Pitch-180, Yaw, 0
	EndIf
	;[End Block]
	
	; ~ Player Is Sprinting
	;[Block]
	Local WeaponSprintRotation%, OldWeaponSprintRotation%
	
	If me\Sprinting
		WeaponSprintRotation = Min(WeaponSprintRotation + 10, 180)
		OldWeaponSprintRotation = Max(OldWeaponSprintRotation - 10, 0)
	Else
		OldWeaponSprintRotation = Min(OldWeaponSprintRotation + 10, 180)
		WeaponSprintRotation = Max(WeaponSprintRotation - 10, 0)
	EndIf
	
;	DebugLog WeaponSprintRotation
	
	If me\Sprinting Then RotateEntity wep_I\Pivot, EntityPitch(wep_I\Pivot), EntityYaw(wep_I\Pivot) + CurveValue(WeaponSprintRotation, OldWeaponSprintRotation, 10.0), 0.0
	;[End Block]
	
	; ~ Setting the variables that are evaluated only when weapon is idling
	;[Block]
	wep_I\AnimFlag = True
	Attacking = False
	IsFullReload = False
	;[End Block]
	
	; ~ Calculating Weapon Light Color
	;[Block]
	For wep = Each Weapons
		If wep_I\Using = wep\ID Then
			If wep\ID = Wep_EMRP Then
				LightColor(wep_I\Light, 55.0, 55.0, 235.0)
			Else
				If wep\ID <> Wep_SCP_127 Then
					LightColor(wep_I\Light, 235.0, 55.0, 0.0)
				EndIf
			EndIf
		EndIf
	Next
	;[End Block]
	
	ShowEntity(wep_I\Pivot)
	
	; ~ Calculating Weapon Light Timer
	;[Block]
	If wep_I\LightTimer > 0.0 And wep_I\LightTimer < 2.5
		wep_I\LightTimer = wep_I\LightTimer + fps\Factor[0]
		ShowEntity wep_I\Light
	Else
		wep_I\LightTimer = 0.0
		HideEntity wep_I\Light
	EndIf
	;[End Block]
	
	; ~ Main Weapon Update Code
	;[Block]
	For wep = Each Weapons
		HideEntity wep\MuzzleFlash
		
		If (Not wep_I\ChangeFlag)
			For wep2 = Each Weapons
				If wep2\ID <> wep_I\Using
					SetAnimTime(wep2\Model, 0) : HideEntity wep2\Model
				Else
					ShowEntity wep2\Model
				EndIf
			Next
			DeselectIronSight()
			mo\MouseHit1 = False
			mo\MouseDown1 = False
			MouseHit(1)
			PressedAttack = False : HeldAttack = False
			PressedCheckAmmo = False
			PressedHolster = False
			PressedInspect = False
			PressedLower = False
			PressedReload = False
			PressedFiremode = False
			If wep\WeaponType = WeaponType_Melee
				mo\MouseHit2 = False
				mo\MouseDown2 = False
				MouseHit(2)
			EndIf
			wep_I\ChangeFlag = True
			wep_I\IronSight = False
		EndIf
		
		If me\Health > 0
			If wep\ID = wep_I\Using
				
				; ~ Determining Muzzle Flash Bone Name
				;[Block]
				If wep\HasAttachment[Att_Barrel_Suppressor] Then StrTemp = "weapon_muzzle_suppressor" Else StrTemp = "weapon_muzzle"
				;[End Block]
				
				; ~ Positioning Muzzle Flash
				;[Block]
				If wep\WeaponType <> WeaponType_Melee Then PositionEntity(wep\MuzzleFlash, EntityX(FindChild(wep\Model, StrTemp), True), EntityY(FindChild(wep\Model, StrTemp), True), EntityZ(FindChild(wep\Model, StrTemp), True), True)
				;[End Block]
				
				; ~ Determining Aim Cross Bone Name
				;[Block]
				For i = Att_Sight_RedDot To Att_Sight_Special
					Select(wep\HasAttachment[i])
						Case Att_Sight_RedDot
							;[Block]
							StrTemp = "att_reddot_scope_dot"
							;[End Block]
						Case Att_Sight_EoTech
							;[Block]
							StrTemp = "att_eotech_scope_dot"
							;[End Block]
						Case Att_Sight_Reflex
							;[Block]
							StrTemp = "att_reflex_scope_dot"
							;[End Block]
						Case Att_Sight_Acog
							;[Block]
							StrTemp = "att_acog_scope_dot"
							;[End Block]
						Case Att_Sight_Special
							;[Block]
							StrTemp = "weapon_scope_dot"
							;[End Block]
					End Select
				Next
				;[End Block]
				
				; ~ Positioning Aim Cross
				;[Block]
				If wep\WeaponType <> WeaponType_Melee Then PositionEntity(wep\AimCross,EntityX(FindChild(wep\Model, StrTemp), True), EntityY(FindChild(wep\Model, StrTemp), True), EntityZ(FindChild(wep\Model, StrTemp), True), True)
				;[End Block]
				
				; ~ Determining When Player Can Attack
				;[Block]
				AttackCondition = ((Not MenuOpen) And (Not ConsoleOpen) And (Not SandBoxMenuOpen) And (Not InvOpen))
				;[End Block]
				
				; ~ Setting Some Local Variables
				;[Block]
				If Attacking Then wep_I\AnimFlag = True Else wep_I\AnimFlag = False
				If me\HeldAmmo[wep\Caliber] <> 0 And wep\CurrentAmmo < (wep\MaximumCurrentAmmo + 1) Then ReloadCondition = True
				;[End Block]
				
				; ~ Updating Weapon Depending On It's State
				;[Block]
				Select(wep\State)
					Case Wep_State_Draw
						;[Block]
						If wep\Found Lor wep\WeaponType = WeaponType_Melee
							If wep\CurrentAmmo > 0 Lor wep\WeaponType = WeaponType_Melee
								AnimateWeapon(wep, "anim_deploy", 1, False, False, Wep_State_Idle, "deploy")
							Else
								AnimateWeapon(wep, "anim_deploy_empty", 1, False, False, Wep_State_Idle, "deploy")
							EndIf
						Else
							If wep\CurrentAmmo > 0
								AnimateWeapon(wep, "anim_ready", 1, False, False, Wep_State_Idle, "ready")
							Else
								AnimateWeapon(wep, "anim_ready_empty", 1, False, False, Wep_State_Idle, "ready")
							EndIf
						EndIf
						;[End Block]
					Case Wep_State_Idle
						;[Block]
						If wep\CurrentAmmo > 0 Lor wep\WeaponType = WeaponType_Melee
							AnimateWeapon(wep, "anim_idle", 1, True)
						Else
							AnimateWeapon(wep, "anim_idle_empty", 1, True)
						EndIf
						
						;[Block]
						wep\Found = True
						;[End Block]
						
						;[Block]
						If wep\WeaponType <> WeaponType_Melee
							If wep\FireMode = FireMode_Auto
								If HeldAttack And AttackCondition And wep\CurrentAmmo > 0
									If wep\AttackState < wep\FireRate
										wep\AttackState = wep\AttackState + fps\Factor[0]
									Else
										wep\AttackState = 0.0
									EndIf
								Else
									wep\AttackState = 1.0
								EndIf
								If wep\AttackState = 0.0
									wep\AttackTimer = 1.0
									AnimateWeapon(wep, "anim_attack_auto", 1, False, False, -1)
									;wep\State = Wep_State_Attack
								EndIf
							Else
								If PressedAttack And AttackCondition And wep\CurrentAmmo > 0
									wep\AttackTimer = 1.0
									wep\State = Wep_State_Attack
								EndIf
							EndIf
						Else
							If (PressedAttack Lor HeldAttack) And AttackCondition Then wep\State = Wep_State_Attack
						EndIf
						
						If wep\AttackTimer = 1.0
							If (Not SelectedDifficulty\Realism)
								wep_I\HUDTimer = 70.0 * 5.0
							Else
								wep_I\HUDTimer = 0.0
							EndIf
							Attacking = True
							For j = 1 To wep\BulletAmount
								AttackWeapon(wep)
							Next
							PlayWeaponSound(wep, "attack", wep\AttackSounds)
							If wep\WeaponType <> WeaponType_Melee
								wep\CurrentAmmo = Max(wep\CurrentAmmo - 1, 0)
								me\CameraShake = wep\Knockback
								CameraPitch = CameraPitch - wep\VerticalRecoil
								If SelectedDifficulty\Realism Then wep_I\RotationPitch = wep_I\RotationPitch - wep\VerticalRecoil
								
								; ~ Adding Recoil Properties
								;[Block]
								If Attacking And wep\WeaponType <> WeaponType_Melee
									
									; ~ Procedural Recoil
									;[Block]
									If (Not SelectedDifficulty\Realism)
										If (Not wep_I\IronSight) Then
											TurnEntity(wep_I\Pivot,CurveValue(Rand(-(wep\Knockback+wep\Knockback)/2,(wep\Knockback+wep\Knockback)*2),Rand((wep\Knockback+wep\Knockback)*2,-(wep\Knockback+wep\Knockback)/2),5.0),CurveValue(Rand(-(wep\Knockback+wep\Knockback)/2,(wep\Knockback+wep\Knockback)*2),Rand((wep\Knockback+wep\Knockback)*2,-(wep\Knockback+wep\Knockback)/2),5.0),CurveValue(Rand(-(wep\Knockback+wep\Knockback)/2,(wep\Knockback+wep\Knockback)*2),Rand((wep\Knockback+wep\Knockback)*2,-(wep\Knockback+wep\Knockback)/2),5.0))
										EndIf
									Else
										If (Not wep_I\IronSight) Then
											TurnEntity(wep_I\Pivot,CurveValue(Rand(-(wep\Knockback+wep\Knockback)/2,(wep\Knockback+wep\Knockback)*2),Rand((wep\Knockback+wep\Knockback)*2,-(wep\Knockback+wep\Knockback)/2),5.0),CurveValue(Rand(-(wep\Knockback+wep\Knockback)/2,(wep\Knockback+wep\Knockback)*2),Rand((wep\Knockback+wep\Knockback)*2,-(wep\Knockback+wep\Knockback)/2),5.0),CurveValue(Rand(-(wep\Knockback+wep\Knockback)/2,(wep\Knockback+wep\Knockback)*2),Rand((wep\Knockback+wep\Knockback)*2,-(wep\Knockback+wep\Knockback)/2),5.0))
										Else
											TurnEntity(wep_I\Pivot,CurveValue(Rand(-(wep\Knockback+wep\Knockback)/4.2,(wep\Knockback+wep\Knockback)*1.8),Rand((wep\Knockback+wep\Knockback)*1.8,-(wep\Knockback+wep\Knockback)/4.2),5.0),CurveValue(Rand(-(wep\Knockback+wep\Knockback)/4.2,(wep\Knockback+wep\Knockback)*1.8),Rand((wep\Knockback+wep\Knockback)*1.8,-(wep\Knockback+wep\Knockback)/4.2),5.0),CurveValue(Rand(-(wep\Knockback+wep\Knockback)/4.2,(wep\Knockback+wep\Knockback)*1.8),Rand((wep\Knockback+wep\Knockback)*1.8,-(wep\Knockback+wep\Knockback)/4.2),5.0))
										EndIf
									EndIf
									;[End Block]
									
									; ~ Texturing Muzzle Flash
									;[Block]
									wep_I\LightTimer = fps\Factor[0]
									EntityTexture wep\MuzzleFlash, p_I\ParticleTextureID[PARTICLE_FLASH]
									ShowEntity wep\MuzzleFlash
									TurnEntity wep\MuzzleFlash, 0, 0, Rnd(360)
									ScaleSprite wep\MuzzleFlash, Rnd(0.025, 0.03),Rnd(0.025, 0.03)
									;[End Block]
								EndIf
								;[End Block]
							EndIf
							wep\AttackTimer = 0.0
						EndIf
						;[End Block]
						
						If wep\WeaponType <> WeaponType_Melee
							If PressedReload And ReloadCondition Then wep\State = Wep_State_Reload
							If PressedCheckAmmo Then wep\State = Wep_State_CheckAmmo
							If PressedLower Then wep\State = Wep_State_Lower
							If PressedFiremode And wep\WeaponType <> WeaponType_Melee And wep\WeaponType <> WeaponType_Handgun Then wep\State = Wep_State_FireMode
						EndIf
						If PressedInspect Then wep\State = Wep_State_Inspect
						If PressedHolster Then wep\State = Wep_State_Holster
						;[End Block]
					Case Wep_State_FireMode
						;[Block]
						If (Not SelectedDifficulty\Realism) Then wep_I\HUDTimer = 70.0 * 5.0
						AnimateWeapon(wep, "anim_fire_mode", 1, False, False, Wep_State_Idle, "..\fire_mode_change")
						If (Not wep\PlayingAnimation)
;						If AnimTime(wep\HandsModel) > (wep\CurrentAnimation\y - 0.5)
							If wep\FireMode < FireMode_Auto
								wep\FireMode = wep\FireMode + 1
							Else
								wep\FireMode = FireMode_Single
							EndIf
						EndIf
						;[End Block]
					Case Wep_State_CheckAmmo
						;[Block]
						wep_I\HUDTimer = 70.0 * 5.0
						If wep\CurrentAmmo > 0
							AnimateWeapon(wep, "anim_ammo_check", 1, False, False, Wep_State_Idle, "ammo_check")
						Else
							AnimateWeapon(wep, "anim_ammo_check_empty", 1, False, False, Wep_State_Idle, "ammo_check_empty")
						EndIf
						If PressedHolster Then wep\State = Wep_State_Holster
						;[End Block]
					Case Wep_State_Attack
						;[Block]
						If wep\CurrentAmmo > 1 Lor wep\WeaponType = WeaponType_Melee
							AnimateWeapon(wep, "anim_attack", 1, False, False, Wep_State_Idle)
						Else
							AnimateWeapon(wep, "anim_attack_empty", 1, False, False, Wep_State_Idle)
						EndIf
						;[End Block]
					Case Wep_State_Reload
						;[Block]
						If (Not SelectedDifficulty\Realism) Then wep_I\HUDTimer = 70.0 * 5.0
						If wep\CurrentAmmo = 0 Then IsFullReload = True
						
						If wep\WeaponType <> WeaponType_Shotgun
							If (Not IsFullReload)
								AnimateWeapon(wep, "anim_reload", 1, False, False, Wep_State_Idle, "reload", wep\ReloadSounds)
							Else
								AnimateWeapon(wep, "anim_reload_empty", 1, False, False, Wep_State_Idle, "reload_empty", wep\ReloadSounds)
							EndIf
							Local AmmoDelta% = wep\MaximumCurrentAmmo - wep\CurrentAmmo
							If (Not wep\PlayingAnimation)
;							If AnimTime(wep\HandsModel) > (wep\CurrentAnimation\y - 0.5)
								If me\HeldAmmo[wep\Caliber] <> 0
									If IsFullReload
										wep\CurrentAmmo = wep\CurrentAmmo + Min(AmmoDelta, me\HeldAmmo[wep\Caliber])
									Else
										wep\CurrentAmmo = wep\CurrentAmmo + (Min(AmmoDelta, me\HeldAmmo[wep\Caliber]) + 1) : me\HeldAmmo[wep\Caliber] = Max(me\HeldAmmo[wep\Caliber] - 1, 0)
									EndIf
								Else
									wep\CurrentAmmo = wep\CurrentAmmo + Min(AmmoDelta, me\HeldAmmo[wep\Caliber])
								EndIf
								me\HeldAmmo[wep\Caliber] = Max(me\HeldAmmo[wep\Caliber] - AmmoDelta, 0)
							EndIf
						Else
							Local MaxAmmo%
							If IsFullReload
								MaxAmmo = wep\MaximumCurrentAmmo
							Else
								MaxAmmo = (wep\MaximumCurrentAmmo + 1)
							EndIf
							Select(wep\ReloadState)
								Case ReloadState_Start
									If IsFullReload
										AnimateWeapon(wep, "anim_reload_start_empty", 1, False, False, -1, "reload_start")
										If (Not wep\PlayingAnimation)
											wep\CurrentAmmo = wep\CurrentAmmo + 1
											me\HeldAmmo[wep\Caliber] = me\HeldAmmo[wep\Caliber] - 1
										EndIf
									Else
										AnimateWeapon(wep, "anim_reload_start", 1, False, False, -1, "inspect")
									EndIf
									If (Not wep\PlayingAnimation) Then wep\ReloadState = ReloadState_Loop
								Case ReloadState_Loop
									AnimateWeapon(wep, "anim_reload", 1, True, False, -1, "reload", wep\ReloadSounds)
									If (Not wep\PlayingAnimation)
										wep\CurrentAmmo = wep\CurrentAmmo + 1
										me\HeldAmmo[wep\Caliber] = Max(me\HeldAmmo[wep\Caliber] - 1, 0)
									EndIf
									If wep\CurrentAmmo = MaxAmmo And (Not wep\PlayingAnimation) Then wep\ReloadState = ReloadState_End
								Case ReloadState_End
									AnimateWeapon(wep, "anim_reload_stop", 1, False, False, -1, "inspect")
									If (Not wep\PlayingAnimation)
										wep\ReloadState = ReloadState_Start
										wep\State = Wep_State_Idle
									EndIf
							End Select
						EndIf
						;[End Block]
					Case Wep_State_Lower
						;[Block]
						If wep\CurrentAmmo > 0 Lor wep\WeaponType = WeaponType_Melee
							AnimateWeapon(wep, "anim_lower", 1, True)
						Else
							AnimateWeapon(wep, "anim_lower_empty", 1, True)
						EndIf
						
						If wep\WeaponType <> WeaponType_Melee
							If PressedReload And ReloadCondition Then wep\State = Wep_State_Reload
							If PressedCheckAmmo Then wep\State = Wep_State_CheckAmmo
							If PressedFiremode And wep\WeaponType <> WeaponType_Handgun Then wep\State = Wep_State_FireMode
						EndIf
						If PressedInspect Then wep\State = Wep_State_Inspect
						If PressedLower Then wep\State = Wep_State_Idle
						If PressedHolster Then wep\State = Wep_State_Holster
						;[End Block]
					Case Wep_State_Inspect
						;[Block]
						If wep\CurrentAmmo > 0 Lor wep\WeaponType = WeaponType_Melee
							AnimateWeapon(wep, "anim_inspect", 1, False, False, Wep_State_Idle, "inspect")
						Else
							AnimateWeapon(wep, "anim_inspect_empty", 1, False, False, Wep_State_Idle, "inspect")
						EndIf
						If wep\WeaponType = WeaponType_Melee
							If PressedAttack And AttackCondition Then wep\State = Wep_State_Idle
						Else
							If PressedAttack And AttackCondition And wep\CurrentAmmo > 0 Then wep\State = Wep_State_Idle
						EndIf
						If PressedHolster Then wep\State = Wep_State_Holster
						If wep\WeaponType <> WeaponType_Melee And PressedReload And ReloadCondition Then wep\State = Wep_State_Reload
						;[End Block]
					Case Wep_State_Jam
						;[Block]
						AnimateWeapon(wep, "anim_jam", 1, False, False, Wep_State_Idle, "jam")
						;[End Block]
					Case Wep_State_Holster
						;[Block]
						If wep\CurrentAmmo > 0 Lor wep\WeaponType = WeaponType_Melee
							AnimateWeapon(wep, "anim_holster", 1, False, False, Wep_State_Draw, "holster")
						Else
							AnimateWeapon(wep, "anim_holster_empty", 1, False, False, Wep_State_Draw, "holster")
						EndIf
						If (Not wep\PlayingAnimation) Then HolsterWeapon()
;						If AnimTime(wep\HandsModel) > (wep\CurrentAnimation\y - 0.5) Then HolsterWeapon()
						;[End Block]
				End Select
				;[End Block]
				
				; ~ Showing The Models
				;[Block]
				ShowEntity(wep\Model) : ShowEntity(wep\HandsModel)
				;[End Block]
				
				; ~ Setting Weapon Model Animations
				;[Block]
				SetAnimTime(wep\Model, (AnimTime(wep\HandsModel) + wep\FrameSeparation))
				;[End Block]
				
				; ~ Jamming Logic
				;[Block]
				
				Local RandomJam1%, RandomJam2%, RandomJam3%, RandomJam4%
				
				If SelectedDifficulty = difficulties[SAFE]
					RandomJam1 = 70
					RandomJam2 = 120
					RandomJam3 = 200
					RandomJam4 = 400
				ElseIf SelectedDifficulty = difficulties[EUCLID]
					RandomJam1 = 20
					RandomJam2 = 50
					RandomJam3 = 100
					RandomJam4 = 300
				ElseIf SelectedDifficulty = difficulties[KETER] Lor SelectedDifficulty = difficulties[APOLLYON]
					RandomJam2 = 20
					RandomJam3 = 50
					RandomJam4 = 200
				EndIf
				
				wep\CoolTimer = Max(wep\CoolTimer - fps\Factor[0], 0.0)
				
				If wep\MaximumTemperature <> -1
					If Attacking
						wep\CoolTimer = 70.0 * 3.0
						wep\Temperature = Min(wep\Temperature + Rand(0.5, 2.0), wep\MaximumTemperature)
					Else
						If wep\CoolTimer = 0.0 Then wep\Temperature = Max(wep\Temperature - 0.05, 0.0)
					EndIf
					If wep\Temperature >= wep\MaximumTemperature
						wep\State = Wep_State_Jam
					ElseIf wep\Temperature >= (wep\MaximumTemperature / 1.3) And wep\Temperature < wep\MaximumTemperature
						If Attacking
							If Rand(0, RandomJam1) = 1 Then wep\State = Wep_State_Jam
						EndIf
					ElseIf wep\Temperature >= (wep\MaximumTemperature / 2) And wep\Temperature < (wep\MaximumTemperature / 1.3)
						If Attacking
							If Rand(0, RandomJam2) = 1 Then wep\State = Wep_State_Jam
						EndIf
					ElseIf wep\Temperature >= (wep\MaximumTemperature / 5.5) And wep\Temperature < (wep\MaximumTemperature / 2)
						If Attacking
							If Rand(0, RandomJam3) = 1 Then wep\State = Wep_State_Jam
						EndIf
					Else
						If Attacking
							If Rand(0, RandomJam4) = 1 Then wep\State = Wep_State_Jam
						EndIf
					EndIf
				EndIf
				;[End Block]
				
			Else
				; ~ Hiding The Models
				;[Block]
				HideEntity(wep\Model) : HideEntity(wep\HandsModel)
				;[End Block]
			EndIf
		Else
			HolsterWeapon() ; ~ Holstering The Weapon If Player Is Dead
		EndIf
	Next
	;[End Block]
	
	CatchErrors("Uncaught: UpdateWeapons()")
End Function
;[End Block]

; ~ Toggling Weapons
;[Block]
Function ToggleWeapons()
	Local wep.Weapons
	Local KeyPressed%[MaxWeaponSlots], i%
	
	For i = 0 To MaxWeaponSlots - 1
		KeyPressed[i] = KeyHit(i + 2)
	Next
	
	If me\KillAnimTimer <= 0 And me\CanUseWeapons And fps\Factor[0] > 0.0 And (Not wep_I\IronSight)
		For i = 0 To MaxWeaponSlots - 1
			For wep.Weapons = Each Weapons
				If wep <> Null
					If wep_I\Using <> wep\ID Then HideEntity(wep\Model) : HideEntity(wep\HandsModel)
				EndIf
			Next
			If KeyPressed[i]
				If wep_I\CurrentWeaponSlot[i] <> Unarmed
					For wep.Weapons = Each Weapons
						If wep <> Null
							wep\State = Wep_State_Holster
							wep_I\ChangeFlag = False
							wep\State = Wep_State_Draw
							If wep\ID = wep_I\CurrentWeaponSlot[i] Then wep_I\Using = wep\ID : Exit
						EndIf
					Next
					wep_I\SelectedSlot = (i + 1)
					wep_I\SlotTimer = 70.0 * 3.0
					wep_I\HUDTimer = 0.0
				EndIf
			EndIf
		Next
	EndIf
	
End Function
;[End Block]

; ~ Holstering The Weapon
;[Block]
Function HolsterWeapon()
	Local wep.Weapons
	
	For wep = Each Weapons
		If wep\ID = wep_I\Using
			If AnimTime(wep\HandsModel) = 0 Lor me\Health = 0
				HideEntity(wep\Model) : HideEntity(wep\HandsModel)
				wep\State = Wep_State_Draw
			EndIf
			wep_I\ChangeFlag = False : wep_I\SlotTimer = 0.0 : wep_I\HUDTimer = 0.0
			wep_I\Using = Unarmed : wep_I\SelectedSlot = Unarmed
		EndIf
	Next

End Function
;[End Block]

; ~ Updating The Breathing
;[Block]
Function UpdateWeaponBreathing()
	Local WepPosY#, WepTimeY#
	
	If (Not wep_I\AnimFlag) And (me\CurrSpeed = 0.0) And (Not me\Sprinting) And (Not wep_I\IronSight)
		WepPosY = -0.005
		WepTimeY = 0.00005
		If wep_p\Pivot_YSide = 0
			If wep_p\Pivot_Y > WepPosY
				wep_p\Pivot_Y = wep_p\Pivot_Y - (WepTimeY*fps\Factor[0])
			Else
				wep_p\Pivot_Y = WepPosY
				wep_p\Pivot_YSide = 1
			EndIf
		Else
			If wep_p\Pivot_Y < 0.0
				wep_p\Pivot_Y = wep_p\Pivot_Y + (WepTimeY*fps\Factor[0])
			Else
				wep_p\Pivot_Y = 0.0
				wep_p\Pivot_YSide = 0
			EndIf
		EndIf
		
		If wep_p\Pivot_X < 0.0
			wep_p\Pivot_X = Min(wep_p\Pivot_X + (0.0001*fps\Factor[0]),0.0)
		ElseIf wep_p\Pivot_X > 0.0
			wep_p\Pivot_X = Max(wep_p\Pivot_X - (0.0001*fps\Factor[0]),0.0)
		EndIf
	ElseIf (Not wep_I\AnimFlag) And (me\CurrSpeed = 0.0) And (Not me\Sprinting) And wep_I\IronSight
		If KeyDown(key\SPRINT)
			WepPosY = -0.0001
			WepTimeY = 0.000001
		Else
			WepPosY = -0.0005
			WepTimeY = 0.00001
		EndIf
		If wep_p\Pivot_YSide = 0
			If wep_p\Pivot_Y > WepPosY
				wep_p\Pivot_Y = wep_p\Pivot_Y - (WepTimeY*fps\Factor[0])
			Else
				wep_p\Pivot_Y = WepPosY
				wep_p\Pivot_YSide = 1
			EndIf
		Else
			If wep_p\Pivot_Y < 0.0
				wep_p\Pivot_Y = wep_p\Pivot_Y + (WepTimeY*fps\Factor[0])
			Else
				wep_p\Pivot_Y = 0.0
				wep_p\Pivot_YSide = 0
			EndIf
		EndIf
		
		If wep_p\Pivot_X < 0.0
			wep_p\Pivot_X = Min(wep_p\Pivot_X + (0.0001*fps\Factor[0]),0.0)
		ElseIf wep_p\Pivot_X > 0.0
			wep_p\Pivot_X = Max(wep_p\Pivot_X - (0.0001*fps\Factor[0]),0.0)
		EndIf
	ElseIf (Not wep_I\AnimFlag) And (me\CurrSpeed <> 0.0) And (Not me\Sprinting) And (Not wep_I\IronSight)
		If wep_p\Pivot_XSide = 0
			If wep_p\Pivot_X > -0.0025
				wep_p\Pivot_X = wep_p\Pivot_X - (0.000075/(1.0 + me\Crouch)*fps\Factor[0])
				If wep_p\Pivot_X > -0.00125
					wep_p\Pivot_Y = Min(wep_p\Pivot_Y + (0.000125/(1.0 + me\Crouch)*fps\Factor[0]),0.001)
				Else
					wep_p\Pivot_Y = Max(wep_p\Pivot_Y - (0.000125/(1.0 + me\Crouch)*fps\Factor[0]),0.0)
				EndIf
			Else
				wep_p\Pivot_X = -0.0025
				wep_p\Pivot_Y = 0.0
				wep_p\Pivot_XSide = 1
			EndIf
		Else
			If wep_p\Pivot_X < 0.0
				wep_p\Pivot_X = wep_p\Pivot_X + (0.000075/(1.0 + me\Crouch)*fps\Factor[0])
				If wep_p\Pivot_X < -0.00125
					wep_p\Pivot_Y = Min(wep_p\Pivot_Y + (0.000125/(1.0 + me\Crouch)*fps\Factor[0]),0.001)
				Else
					wep_p\Pivot_Y = Max(wep_p\Pivot_Y - (0.000125/(1.0 + me\Crouch)*fps\Factor[0]),0.0)
				EndIf
			Else
				wep_p\Pivot_X = 0.0
				wep_p\Pivot_Y = 0.0
				wep_p\Pivot_XSide = 0
			EndIf
		EndIf
	ElseIf (Not wep_I\AnimFlag) And (me\CurrSpeed <> 0.0) And me\Sprinting And (Not wep_I\IronSight)
		If wep_p\Pivot_XSide = 0
			If wep_p\Pivot_X > -0.003
				wep_p\Pivot_X = wep_p\Pivot_X - (0.00008 * fps\Factor[0])
				If wep_p\Pivot_X > -0.0013
					wep_p\Pivot_Y = Min(wep_p\Pivot_Y + (0.00013 * fps\Factor[0]),0.001)
				Else
					wep_p\Pivot_Y = Max(wep_p\Pivot_Y - (0.00013 * fps\Factor[0]),0.0)
				EndIf
			Else
				wep_p\Pivot_X = -0.003
				wep_p\Pivot_Y = 0.0
				wep_p\Pivot_XSide = 1
			EndIf
		Else
			If wep_p\Pivot_X < 0.0
				wep_p\Pivot_X = wep_p\Pivot_X + (0.00008 * fps\Factor[0])
				If wep_p\Pivot_X < -0.0013
					wep_p\Pivot_Y = Min(wep_p\Pivot_Y + (0.00013 * fps\Factor[0]),0.001)
				Else
					wep_p\Pivot_Y = Max(wep_p\Pivot_Y - (0.00013 * fps\Factor[0]),0.0)
				EndIf
			Else
				wep_p\Pivot_X = 0.0
				wep_p\Pivot_Y = 0.0
				wep_p\Pivot_XSide = 0
			EndIf
		EndIf
	Else
		If wep_p\Pivot_Y < 0.0
			wep_p\Pivot_Y = Max(wep_p\Pivot_Y + (0.0001 * fps\Factor[0]),0.0)
		Else
			wep_p\Pivot_Y = 0.0
		EndIf
		
		If wep_p\Pivot_X < 0.0
			wep_p\Pivot_X = Min(wep_p\Pivot_X + (0.0001 * fps\Factor[0]),0.0)
		ElseIf wep_p\Pivot_X > 0.0
			wep_p\Pivot_X = Max(wep_p\Pivot_X - (0.0001 * fps\Factor[0]),0.0)
		EndIf
	EndIf
	
	PositionEntity wep_I\Pivot, EntityX(Camera), EntityY(Camera) + wep_p\Pivot_Y, EntityZ(Camera)
	MoveEntity wep_I\Pivot, wep_p\Pivot_X, 0, 0
	
End Function
;[End Block]

; ~ Updating Weapon Iron Sight
;[Block]
Function UpdateIronSight()
	Local Pvt%, wep.Weapons, HasIronSight%, PrevIronSight%
	Local CurrentWeapon.Weapons, AimCondition%
	
	For wep.Weapons = Each Weapons
		If wep\ID = wep_I\Using
			If wep\State <> Wep_State_Idle And wep\State <> Wep_State_Attack And wep\State <> Wep_State_Reload And wep\State <> Wep_State_FireMode Then DeselectIronSight() : Return
		EndIf
	Next
		
	If me\Sprinting Then DeselectIronSight()
	
	For wep.Weapons = Each Weapons
		If wep\ID = wep_I\Using
			If wep\WeaponType <> WeaponType_Melee
				HasIronSight = True
				If wep_I\IronSight Lor wep_I\IronSightAnim
					EntityParent(wep\Model, wep_is\Pivot2) : EntityParent(wep\HandsModel, wep_is\Pivot2)
				EndIf
				CurrentWeapon = wep
				Exit
			Else
				HasIronSight = False
				Exit
			EndIf
		EndIf
	Next
	
	If (Not HasIronSight) Then Return
	
	If wep_I\IronSight
		wep_is\Timer = wep_is\Timer + fps\Factor[0]
		If wep_I\IronSightAnim = 2
			If CurrentWeapon <> Null
				PositionEntity(wep_is\Pivot, CurrentWeapon\IronSightCoords\x, CurrentWeapon\IronSightCoords\y, CurrentWeapon\IronSightCoords\z)
			Else
				PositionEntity(wep_is\Pivot, 0, 0, 0)
			EndIf
			wep_I\IronSightAnim = 1
		EndIf
	Else
		wep_is\Timer = 0
		If wep_I\IronSightAnim = 2
			PositionEntity(wep_is\Pivot, 0, 0, 0)
			wep_I\IronSightAnim = 1
		EndIf
	EndIf
	
	If wep\State = Wep_State_Idle Lor wep\State = Wep_State_Attack Lor wep\State = Wep_State_Reload Lor wep\State = Wep_State_FireMode Then AimCondition = True
	
	If AimCondition
		If wep_is\Timer >= 15
			wep_is\ShowingAimCross = True
		Else
			wep_is\ShowingAimCross = False
		EndIf
	EndIf
	
	PositionEntity(wep_is\Pivot2, CurveValue(EntityX(wep_is\Pivot), EntityX(wep_is\Pivot2), 5.0), CurveValue(EntityY(wep_is\Pivot), EntityY(wep_is\Pivot2), 5.0), CurveValue(EntityZ(wep_is\Pivot), EntityZ(wep_is\Pivot2), 5.0))
	If EntityX(wep_is\Pivot2) <= 0.001 And EntityX(wep_is\Pivot2) >= -0.001
		wep_I\IronSightAnim = 0
	EndIf
	If CurrentWeapon <> Null
		If EntityX(wep_is\Pivot2) <= CurrentWeapon\IronSightCoords\x + 0.001 And EntityX(wep_is\Pivot2) >= CurrentWeapon\IronSightCoords\x - 0.001
			wep_I\IronSightAnim = 0
		EndIf
	Else
		wep_I\IronSightAnim = 0
	EndIf
	
	If (Not InvOpen)
		If (Not opt\ClickToAim)
			If (Not wep_I\IronSightAnim) And HasIronSight
				PrevIronSight = wep_I\IronSight
				wep_I\IronSight% = mo\MouseDown2
				If wep_I\IronSight <> PrevIronSight
					wep_I\IronSightAnim = 2
				EndIf
			EndIf
		Else
			If mo\MouseHit2 Then
				If SelectedItem = Null
					If (Not wep_I\IronSightAnim) And HasIronSight
						wep_I\IronSight% = (Not wep_I\IronSight%)
						wep_I\IronSightAnim = 2
					EndIf
				EndIf
			EndIf
		EndIf
	EndIf
	
End Function
;[End Block]

; ~ Deselecting Iron Sight
;[Block]
Function DeselectIronSight()
	Local wep.Weapons
	
	wep_I\IronSight = 0
	wep_I\IronSightAnim = 0
	PositionEntity(wep_is\Pivot, 0, 0, 0) : PositionEntity(wep_is\Pivot2, 0, 0, 0)
	For wep = Each Weapons
		EntityParent(wep\Model, wep_I\Pivot) : EntityParent(wep\HandsModel, wep_I\Pivot)
	Next
	
End Function
;[End Block]

; ~ Attacking Logic for weapons
;[Block]
Function AttackWeapon%(wep.Weapons)
	Local Temp%, n.NPCs, p.Particles, de.Decals, Ent_Pick%, i%, j%
	Local HitNPC.NPCs
	
	;me\attacking = True
	
	If wep\WeaponType <> WeaponType_Melee And (Not wep\HasAttachment[Att_Barrel_Suppressor]) And wep\ID <> Wep_SCP_127 Then
		;LightVolume = TempLightVolume * 1.2 ; ~ TODO: Fix This - Wolfnaya
		ShowEntity wep_I\Light
		wep_I\LightTimer = fps\Factor[0]
	EndIf
	
	If SelectedDifficulty\Realism Then
		RotateEntity wep_p\PickPivot, Rnd(- wep\Accuracy, wep\Accuracy) / (1.0 + (3.0)), Rnd(-wep\Accuracy, wep\Accuracy) / (1.0 + (3.0)), 0
	Else
		RotateEntity wep_p\PickPivot, Rnd(- wep\Accuracy, wep\Accuracy) / (1.0 + (3.0 * wep_I\IronSight)), Rnd(-wep\Accuracy, wep\Accuracy) / (1.0 + (3.0 * wep_I\IronSight)), 0
	EndIf
	
	HideEntity(me\Head)
	EntityPick(wep_p\PickPivot, wep\Range)
	Ent_Pick% = PickedEntity()
	
	If Ent_Pick% <> 0
		For n.NPCs = Each NPCs
			For j = 0 To MaxHitBoxes - 1
				; ~ Head Has Been Shot
				;[Block]
				If Ent_Pick = n\HitBox1[j]
					If wep_I\Using = wep\ID And wep\WeaponType = WeaponType_Shotgun Lor wep_I\Using = Wep_EMRP Then
						If n\Boss <> n Then
							n\HP = 0
						Else
							n\HP = n\HP - wep\Damage * 4
						EndIf
					Else
						n\HP = n\HP - wep\Damage * 4
					EndIf
					
					HitNPC = n
					n\GotHit = True
					
					If n\HP <= 0 Then
						If wep_I\Using = wep\ID And wep\WeaponType = WeaponType_Shotgun Lor wep_I\Using = Wep_EMRP Then
							If n\Boss <> n Then
								n\HeadShot = True
							EndIf
						EndIf
					EndIf
					Exit
				EndIf
				;[End Block]
				; ~ Body Has Been Shot
				;[Block]
				If Ent_Pick = n\HitBox2[j] Then
					n\HP = n\HP - wep\Damage
					HitNPC = n
					n\GotHit = True
					Exit
				EndIf
				;[End Block]
				; ~ Arms Or Legs Have Been Shot
				;[Block]
				If Ent_Pick = n\HitBox3[j] Then
					n\HP = n\HP - (wep\Damage / 2)
					HitNPC = n
					n\GotHit = True
					Exit
				EndIf
				;[End Block]
			Next
			If HitNPC <> Null Then Exit
		Next
		
		Local wa.Water, hitwater.Water
		
		For wa.Water = Each Water
			If Ent_Pick% = wa\OBJ Then
				hitwater = wa
				PlaySound2(LoadTempSound("SFX\General\Water_Splash.ogg"),Camera,wa\OBJ,50)
				
				p.Particles = CreateParticle(PARTICLE_WATER_SPLASH,PickedX(), PickedY(), PickedZ(), 0.2, 0.2, 80)
				p\Speed = 0.001
				p\SizeChange = 0.003
				p\Alpha = 0.8
				p\AlphaChange = -0.02
				RotateEntity p\OBJ,0,0,0
			EndIf
			If hitwater <> Null Then Exit
		Next
	EndIf
	
	If HitNPC <> Null
		p.Particles = CreateParticle(PARTICLE_BLOOD,PickedX(),PickedY(),PickedZ(), 0.06, 0.2, 80)
		p\Speed = 0.001
		p\SizeChange = 0.003
		p\Alpha = 0.8
		p\AlphaChange = -0.02
		
		If wep\WeaponType = WeaponType_Melee Then PlayWeaponSound(wep, "..\hit_body")
		
;		de.Decals = CreateDecal(DECAL_BULLET_HOLE_1, PickedX(),PickedY(),PickedZ(), 0,0,0,Rnd(0.028,0.034),1.0,1+8,2)
;		AlignToVector de\OBJ,-PickedNX(),-PickedNY(),-PickedNZ(),3
;		MoveEntity de\OBJ, 0,0,-0.001
;		de\LifeTime = 70*20
;		EntityParent de\OBJ, Ent_Pick
	ElseIf Ent_Pick <> 0 And hitwater = Null
		p.Particles = CreateParticle(PARTICLE_BLACK_SMOKE,PickedX(),PickedY(),PickedZ(), 0.03, 0, 80)
		p\Speed = 0.001
		p\SizeChange = 0.003
		p\Alpha = 0.8
		p\AlphaChange = -0.01
		RotateEntity p\Pvt, EntityPitch(wep_I\Pivot) - 180, EntityYaw(wep_I\Pivot), 0
		
		If wep\WeaponType <> WeaponType_Melee
			PlaySound2(Gunshot3SFX, Camera, p\Pvt, 0.4, Rnd(0.8,1.0))
		Else
			PlayWeaponSound(wep, "..\hit_wall")
		EndIf
		
		For i = 0 To Rand(2,3)
			p.Particles = CreateParticle(PARTICLE_BLACK_SMOKE,PickedX(),PickedY(),PickedZ(), 0.006, 0.003, 80)
			p\Speed = 0.02
			p\Alpha = 0.8
			p\AlphaChange = -0.01
			RotateEntity p\Pvt, EntityPitch(wep_I\Pivot)+Rnd(170,190), EntityYaw(wep_I\Pivot)+Rnd(-10,10),0
		Next
		
		Local DecalID% = 0
;		Select(g\DecalType
;			Case GUNDECAL_DEFAULT
;				DecalID = DECAL_TYPE_BULLETHOLE
;			Case GUNDECAL_SLASH
;				DecalID = DECAL_TYPE_SLASHHOLE
;			Case GUNDECAL_SMASH
;				DecalID = DECAL_TYPE_SMASHHOLE
;		End Select
		
		;de.Decals = CreateDecal(GetRandomDecalID(DecalID), PickedX(),PickedY(),PickedZ(), 0,0,0,Rnd(0.028,0.034),1.0,1+8,2)
		de.Decals = CreateDecal(DECAL_BULLET_HOLE_1, PickedX(),PickedY(),PickedZ(), 0,0,0,Rnd(0.028,0.034),1.0,1+8,2)
		AlignToVector de\OBJ,-PickedNX(),-PickedNY(),-PickedNZ(),3
		MoveEntity de\OBJ, 0,0,-0.001
		de\LifeTime = 70*20
		EntityParent de\OBJ, Ent_Pick
	EndIf
	
End Function
;[End Block]

;~IDEal Editor Parameters:
;~C#Blitz3D TSS