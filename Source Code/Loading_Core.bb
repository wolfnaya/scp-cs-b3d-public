Const MaxDecalTextureIDAmount% = 21

Type DecalInstance
	Field DecalTextureID%[MaxDecalTextureIDAmount]
End Type

Global de_I.DecalInstance

; ~ Decal Texture ID Constants
;[Block]
Const DECAL_CORROSIVE_1% = 0
Const DECAL_CORROSIVE_2% = 1
Const DECAL_BLOOD_1% = 2
Const DECAL_BLOOD_2% = 3
Const DECAL_BLOOD_3% = 4
Const DECAL_BLOOD_4% = 5
Const DECAL_BLOOD_5% = 6
Const DECAL_BLOOD_6% = 7
Const DECAL_PD_1% = 8
Const DECAL_PD_2% = 9
Const DECAL_PD_3% = 10
Const DECAL_PD_4% = 11
Const DECAL_PD_5% = 12
Const DECAL_PD_6% = 13
Const DECAL_BULLET_HOLE_1% = 14
Const DECAL_BULLET_HOLE_2% = 15
Const DECAL_BLOOD_DROP_1% = 16
Const DECAL_BLOOD_DROP_2% = 17
Const DECAL_427% = 18
Const DECAL_409% = 19
Const DECAL_WATER% = 20
;[End Block]

Function LoadDecals%()
	Local i%
	
	de_I.DecalInstance = New DecalInstance
	
	For i = DECAL_CORROSIVE_1 To DECAL_CORROSIVE_2
		de_I\DecalTextureID[i] = LoadTexture_Strict("GFX\Decals\corrosive_decal(" + i + ").png", 1 + 2, DeleteAllTextures)
	Next
	
	For i = DECAL_BLOOD_1 To DECAL_BLOOD_6
		de_I\DecalTextureID[i] = LoadTexture_Strict("GFX\Decals\blood_decal(" + (i - DECAL_BLOOD_1) + ").png", 1 + 2, DeleteAllTextures)
	Next
	
	For i = DECAL_PD_1 To DECAL_PD_6
		de_I\DecalTextureID[i] = LoadTexture_Strict("GFX\Decals\pd_decal(" + (i - DECAL_PD_1) + ").png", 1 + 2, DeleteAllTextures)
	Next
	
	For i = DECAL_BULLET_HOLE_1 To DECAL_BULLET_HOLE_2
		de_I\DecalTextureID[i] = LoadTexture_Strict("GFX\Decals\bullet_hole_decal(" + (i - DECAL_BULLET_HOLE_1) + ").png", 1 + 2, DeleteAllTextures)
	Next
	
	For i = DECAL_BLOOD_DROP_1 To DECAL_BLOOD_DROP_2
		de_I\DecalTextureID[i] = LoadTexture_Strict("GFX\Decals\blood_drop_decal(" + (i - DECAL_BLOOD_DROP_1) + ").png", 1 + 2, DeleteAllTextures)
	Next
	
	de_I\DecalTextureID[DECAL_409] = LoadTexture_Strict("GFX\Decals\scp_409_decal.png", 1 + 2, DeleteAllTextures)
	
	de_I\DecalTextureID[DECAL_427] = LoadTexture_Strict("GFX\Decals\scp_427_decal.png", 1 + 2, DeleteAllTextures)
	
	de_I\DecalTextureID[DECAL_WATER] = LoadTexture_Strict("GFX\Decals\water_decal.png", 1 + 2, DeleteAllTextures)
End Function

Function RemoveDecalInstances%()
	Local i%
	
	For i = 0 To MaxDecalTextureIDAmount - 1
		de_I\DecalTextureID[i] = 0
	Next
	Delete Each DecalInstance
End Function

Const MaxParticleTextureIDAmount% = 10

Type ParticleInstance
	Field ParticleTextureID%[MaxParticleTextureIDAmount]
End Type

Global p_I.ParticleInstance

; ~ Particles ID Constants
;[Block]
Const PARTICLE_BLACK_SMOKE% = 0, PARTICLE_WHITE_SMOKE% = 1
Const PARTICLE_FLASH% = 2
Const PARTICLE_DUST% = 3
Const PARTICLE_SHADOW% = 4
Const PARTICLE_SUN% = 5
Const PARTICLE_BLOOD% = 6
Const PARTICLE_SPARK% = 7
Const PARTICLE_WATER_CIRCLE% = 8, PARTICLE_WATER_SPLASH% = 9
;[End Block]

Function LoadParticles%()
	p_I.ParticleInstance = New ParticleInstance
	
	p_I\ParticleTextureID[PARTICLE_BLACK_SMOKE] = LoadTexture_Strict("GFX\Particles\smoke(0).png", 1 + 2, DeleteAllTextures)
	p_I\ParticleTextureID[PARTICLE_WHITE_SMOKE] = LoadAnimTexture_Strict("GFX\Particles\smoke(1).png", 1 + 2, 256, 256, 0, 4, DeleteAllTextures)
	p_I\ParticleTextureID[PARTICLE_FLASH] = LoadTexture_Strict("GFX\Particles\flash.png", 1 + 2, DeleteAllTextures)
	p_I\ParticleTextureID[PARTICLE_DUST] = LoadTexture_Strict("GFX\Particles\dust.png", 1 + 2, DeleteAllTextures)
	p_I\ParticleTextureID[PARTICLE_SHADOW] = LoadTexture_Strict("GFX\NPCs\hg.pt", 1 + 2, DeleteAllTextures)
	p_I\ParticleTextureID[PARTICLE_SUN] = LoadTexture_Strict("GFX\Map\Textures\sun.png", 1 + 2, DeleteAllTextures)
	p_I\ParticleTextureID[PARTICLE_BLOOD] = LoadTexture_Strict("GFX\Particles\blood.png", 1 + 2, DeleteAllTextures)
	p_I\ParticleTextureID[PARTICLE_SPARK] = LoadTexture_Strict("GFX\Particles\spark.png", 1 + 2, DeleteAllTextures)
	p_I\ParticleTextureID[PARTICLE_WATER_CIRCLE] = LoadTexture_Strict("GFX\Particles\water_circle.png", 1 + 2, DeleteAllTextures)
	p_I\ParticleTextureID[PARTICLE_WATER_SPLASH] = LoadTexture_Strict("GFX\Particles\water_splash.png", 1 + 2, DeleteAllTextures)
End Function

Function RemoveParticleInstances%()
	Local i%
	
	For i = 0 To MaxParticleTextureIDAmount - 1
		p_I\ParticleTextureID[i] = 0
	Next
	Delete Each ParticleInstance
End Function

Const MaxDoorModelIDAmount% = 14
Const MaxDoorFrameModelIDAmount% = 8
Const MaxButtonModelIDAmount% = 6
Const MaxButtonTextureIDAmount% = 3
Const MaxElevatorPanelTextureIDAmount% = 3

Type DoorInstance
	Field DoorModelID%[MaxDoorModelIDAmount]
	Field DoorFrameModelID%[MaxDoorFrameModelIDAmount]
	Field ButtonModelID%[MaxButtonModelIDAmount]
	Field ButtonTextureID%[MaxButtonTextureIDAmount]
	Field ElevatorPanelModel%
	Field ElevatorPanelTextureID%[MaxElevatorPanelTextureIDAmount%]
	Field SelectedDoor.Doors, ClosestDoor.Doors
	Field ClosestButton%, AnimButton%
End Type

Global d_I.DoorInstance

; ~ Door Model ID Constants
;[Block]
Const DOOR_DEFAULT_MODEL% = 0
Const DOOR_ELEVATOR_MODEL% = 1
Const DOOR_HEAVY_MODEL_1% = 2
Const DOOR_HEAVY_MODEL_2% = 3
Const DOOR_BIG_MODEL_1% = 4
Const DOOR_BIG_MODEL_2% = 5
Const DOOR_OFFICE_MODEL% = 6
Const DOOR_WOODEN_MODEL% = 7
Const DOOR_ONE_SIDED_MODEL% = 8
Const DOOR_LCZ_MODEL% = 9, DOOR_HCZ_MODEL% = 10, DOOR_EZ_MODEL% = 11, DOOR_RCZ_MODEL_1% = 12, DOOR_RCZ_MODEL_2% = 13
;[End Block]

; ~ Door Frame Model ID Constants
;[Block]
Const DOOR_DEFAULT_FRAME_MODEL% = 0
Const DOOR_BIG_FRAME_MODEL% = 1
Const DOOR_OFFICE_FRAME_MODEL% = 2
Const DOOR_WOODEN_FRAME_MODEL% = 3
Const DOOR_LCZ_FRAME_MODEL% = 4, DOOR_HCZ_FRAME_MODEL% = 5, DOOR_EZ_FRAME_MODEL% = 6, DOOR_RCZ_FRAME_MODEL% = 7
;[End Block]

; ~ Button Model ID Constants
;[Block]
Const BUTTON_DEFAULT_MODEL% = 0
Const BUTTON_KEYCARD_MODEL% = 1
Const BUTTON_KEYPAD_MODEL% = 2
Const BUTTON_SCANNER_MODEL% = 3
Const BUTTON_ELEVATOR_MODEL% = 4
Const BUTTON_MT_ELEVATOR_MODEL% = 5
;[End Block]

; ~ Button Texture ID Constants
;[Block]
Const BUTTON_GREEN_TEXTURE% = 0
Const BUTTON_YELLOW_TEXTURE% = 1
Const BUTTON_RED_TEXTURE% = 2
Const BUTTON_106_TEXTURE% = 3
;[End Block]

; ~ Elevator Panel Texture ID Constants
;[Block]
Const ELEVATOR_PANEL_UP% = 0
Const ELEVATOR_PANEL_DOWN% = 1
Const ELEVATOR_PANEL_IDLE% = 2
;[End Block]

Function LoadDoors%()
	Local i%
	
	d_I.DoorInstance = New DoorInstance
	
	d_I\DoorModelID[DOOR_DEFAULT_MODEL] = LoadMesh_Strict("GFX\Map\Props\Door01.x")
	
	d_I\DoorModelID[DOOR_ELEVATOR_MODEL] = LoadMesh_Strict("GFX\Map\Props\ElevatorDoor.b3d")
	
	d_I\DoorModelID[DOOR_HEAVY_MODEL_1] = LoadMesh_Strict("GFX\Map\Props\HeavyDoor1.x")
	d_I\DoorModelID[DOOR_HEAVY_MODEL_2] = LoadMesh_Strict("GFX\Map\Props\HeavyDoor2.x")
	
	d_I\DoorModelID[DOOR_BIG_MODEL_1] = LoadMesh_Strict("GFX\Map\Props\contdoorleft.x")
	d_I\DoorModelID[DOOR_BIG_MODEL_2] = LoadMesh_Strict("GFX\Map\Props\contdoorright.x")
	
	d_I\DoorModelID[DOOR_OFFICE_MODEL] = LoadAnimMesh_Strict("GFX\Map\Props\officedoor.b3d")
	
	d_I\DoorModelID[DOOR_WOODEN_MODEL] = LoadMesh_Strict("GFX\Map\Props\DoorWooden.b3d")
	
	d_I\DoorModelID[DOOR_ONE_SIDED_MODEL] = LoadMesh_Strict("GFX\Map\Props\Door02.x")
	
	d_I\DoorModelID[DOOR_LCZ_MODEL] = LoadMesh_Strict("GFX\Map\Props\DoorLCZ.b3d")
	
	d_I\DoorModelID[DOOR_HCZ_MODEL] = LoadAnimMesh_Strict("GFX\Map\Props\DoorHCZ.b3d")
	
	d_I\DoorModelID[DOOR_EZ_MODEL] = LoadMesh_Strict("GFX\Map\Props\DoorEZ.b3d")
	
	d_I\DoorModelID[DOOR_RCZ_MODEL_1] = LoadMesh_Strict("GFX\Map\Props\DoorRCZ1.b3d")
	
	d_I\DoorModelID[DOOR_RCZ_MODEL_2] = LoadMesh_Strict("GFX\Map\Props\DoorRCZ2.b3d")
	
	For i = 0 To MaxDoorModelIDAmount - 1
		HideEntity(d_I\DoorModelID[i])
	Next
	
	d_I\DoorFrameModelID[DOOR_DEFAULT_FRAME_MODEL] = LoadMesh_Strict("GFX\Map\Props\DoorFrame.b3d")
	
	d_I\DoorFrameModelID[DOOR_BIG_FRAME_MODEL] = LoadMesh_Strict("GFX\Map\Props\ContDoorFrame.b3d")
	
	d_I\DoorFrameModelID[DOOR_OFFICE_FRAME_MODEL] = LoadMesh_Strict("GFX\Map\Props\officedoorframe.b3d")
	
	d_I\DoorFrameModelID[DOOR_WOODEN_FRAME_MODEL] = LoadMesh_Strict("GFX\Map\Props\DoorWoodenFrame.b3d")
	
	d_I\DoorFrameModelID[DOOR_LCZ_FRAME_MODEL] = LoadMesh_Strict("GFX\Map\Props\DoorFrameLCZ.b3d")
	
	d_I\DoorFrameModelID[DOOR_HCZ_FRAME_MODEL] = LoadMesh_Strict("GFX\Map\Props\DoorFrameHCZ.b3d")
	
	d_I\DoorFrameModelID[DOOR_EZ_FRAME_MODEL] = LoadMesh_Strict("GFX\Map\Props\DoorFrameEZ.b3d")
	
	d_I\DoorFrameModelID[DOOR_RCZ_FRAME_MODEL] = LoadAnimMesh_Strict("GFX\Map\Props\DoorFrameRCZ.b3d")
	
	For i = 0 To MaxDoorFrameModelIDAmount - 2 Step 2
		HideEntity(d_I\DoorFrameModelID[i])
		HideEntity(d_I\DoorFrameModelID[i + 1])
	Next
	
	d_I\ElevatorPanelModel = LoadMesh_Strict("GFX\Map\Props\elevator_panel.b3d")
	HideEntity(d_I\ElevatorPanelModel)
	
	d_I\ElevatorPanelTextureID[ELEVATOR_PANEL_DOWN] = LoadTexture_Strict("GFX\Map\Textures\elevator_panel_down.png", 1, DeleteAllTextures)
	d_I\ElevatorPanelTextureID[ELEVATOR_PANEL_UP] = LoadTexture_Strict("GFX\Map\Textures\elevator_panel_up.png", 1, DeleteAllTextures)
	d_I\ElevatorPanelTextureID[ELEVATOR_PANEL_IDLE] = LoadTexture_Strict("GFX\Map\Textures\elevator_panel_idle.png", 1, DeleteAllTextures)
	
	If opt\Atmosphere
		For i = ELEVATOR_PANEL_DOWN To ELEVATOR_PANEL_IDLE
			TextureBlend(d_I\ElevatorPanelTextureID[i], 5)
		Next
	EndIf
	
	d_I\ButtonModelID[BUTTON_DEFAULT_MODEL] = LoadAnimMesh_Strict("GFX\Map\Props\Button.b3d")
	
	d_I\ButtonModelID[BUTTON_KEYCARD_MODEL] = LoadMesh_Strict("GFX\Map\Props\ButtonKeycard.b3d")
	
	d_I\ButtonModelID[BUTTON_KEYPAD_MODEL] = LoadMesh_Strict("GFX\Map\Props\ButtonCode.b3d")
	
	d_I\ButtonModelID[BUTTON_SCANNER_MODEL] = LoadMesh_Strict("GFX\Map\Props\ButtonScanner.b3d")
	
	d_I\ButtonModelID[BUTTON_ELEVATOR_MODEL] = LoadAnimMesh_Strict("GFX\Map\Props\ButtonElevator.b3d")
	
	d_I\ButtonModelID[BUTTON_MT_ELEVATOR_MODEL] = LoadAnimMesh_Strict("GFX\Map\Props\ButtonElevatorMT.b3d")
	
	For i = 0 To MaxButtonModelIDAmount - 1
		HideEntity(d_I\ButtonModelID[i])
	Next
	
	d_I\ButtonTextureID[BUTTON_GREEN_TEXTURE] = LoadTexture_Strict("GFX\Map\Textures\keypad.jpg", 1, DeleteAllTextures)
	d_I\ButtonTextureID[BUTTON_YELLOW_TEXTURE] = LoadTexture_Strict("GFX\Map\Textures\keypad_using.png", 1, DeleteAllTextures)
	d_I\ButtonTextureID[BUTTON_RED_TEXTURE] = LoadTexture_Strict("GFX\Map\Textures\keypad_locked.png", 1, DeleteAllTextures)
	d_I\ButtonTextureID[BUTTON_106_TEXTURE] = LoadTexture_Strict("GFX\Map\Textures\keypad_106.png", 1, DeleteAllTextures)
	
	elev_I = New ElevatorInstance
	elev_I\ButtonTexture[0] = LoadAnimTexture("GFX\HUD\Elevator_HUD.png", 1 + 2, 64, 64, 0, 3)
;	elev_I\ButtonTexture[1] = LoadAnimTexture("GFX\HUD\Elevator_HUD_Zones.png", 1 + 2, 64, 64, 0, 3)
;	elev_I\ButtonTexture[2] = LoadAnimTexture("GFX\HUD\Elevator_HUD_Zones_2.png", 1 + 2, 64, 64, 0, 3)
	
	If opt\Atmosphere
		For i = BUTTON_GREEN_TEXTURE To BUTTON_106_TEXTURE
			TextureBlend(d_I\ButtonTextureID[i], 5)
		Next
	EndIf
End Function

Function RemoveDoorInstances%()
	Local i%
	
	For i = 0 To MaxDoorModelIDAmount - 1
		FreeEntity(d_I\DoorModelID[i]) : d_I\DoorModelID[i] = 0
	Next
	For i = 0 To MaxDoorFrameModelIDAmount - 1
		FreeEntity(d_I\DoorFrameModelID[i]) : d_I\DoorFrameModelID[i] = 0
	Next
	FreeEntity(d_I\ElevatorPanelModel) : d_I\ElevatorPanelModel = 0
	For i = 0 To MaxButtonModelIDAmount - 1
		FreeEntity(d_I\ButtonModelID[i]) : d_I\ButtonModelID[i] = 0
	Next
	For i = ELEVATOR_PANEL_DOWN To ELEVATOR_PANEL_IDLE
		d_I\ElevatorPanelTextureID[i] = 0
	Next
	For i = BUTTON_GREEN_TEXTURE To BUTTON_106_TEXTURE
		d_I\ButtonTextureID[i] = 0
	Next
	Delete Each DoorInstance
End Function

Const MaxLeverModelIDAmount% = 2

Type LeverInstance
	Field LeverModelID%[MaxLeverModelIDAmount]
End Type

Global lvr_I.LeverInstance

; ~ Lever Model ID Constants
;[Block]
Const LEVER_BASE_MODEL% = 0
Const LEVER_HANDLE_MODEL% = 1
;[End Block]

Function LoadLevers%()
	Local i%
	
	lvr_I.LeverInstance = New LeverInstance
	
	lvr_I\LeverModelID[LEVER_BASE_MODEL] = LoadMesh_Strict("GFX\Map\Props\LeverBase.b3d")
	
	lvr_I\LeverModelID[LEVER_HANDLE_MODEL] = LoadMesh_Strict("GFX\Map\Props\LeverHandle.b3d")
	
	For i = 0 To MaxLeverModelIDAmount - 1
		HideEntity(lvr_I\LeverModelID[i])
	Next
End Function

Function RemoveLeverInstances%()
	Local i%
	
	For i = 0 To MaxLeverModelIDAmount - 1
		FreeEntity(lvr_I\LeverModelID[i]) : lvr_I\LeverModelID[i] = 0
	Next
	Delete Each LeverInstance
End Function

Const MaxCamModelIDAmount% = 2
Const MaxCamTextureIDAmount% = 2

Type SecurityCamInstance
	Field CamModelID%[MaxCamModelIDAmount]
	Field CamTextureID%[MaxCamTextureIDAmount]
	Field ScreenTex%
	Field SelectedMonitor.SecurityCams
	Field CoffinCam.SecurityCams
End Type

Global sc_I.SecurityCamInstance

; ~ Cam Model ID Constants
;[Block]
Const CAM_BASE_MODEL% = 0
Const CAM_HEAD_MODEL% = 1
;[End Block]

; ~ Cam Texture ID Constants
;[Block]
Const CAM_HEAD_DEFAULT_TEXTURE% = 0
Const CAM_HEAD_RED_LIGHT_TEXTURE% = 1
;[End Block]

Function LoadSecurityCams%()
	Local i%
	
	sc_I.SecurityCamInstance = New SecurityCamInstance
	
	sc_I\CamModelID[CAM_BASE_MODEL] = LoadMesh_Strict("GFX\Map\Props\CamBase.b3d")
	sc_I\CamModelID[CAM_HEAD_MODEL] = LoadMesh_Strict("GFX\Map\Props\CamHead.b3d")
	
	For i = 0 To MaxCamModelIDAmount - 1
		HideEntity(sc_I\CamModelID[i])
	Next
	
	sc_I\ScreenTex = CreateTextureUsingCacheSystem(512, 512)
	
	For i = CAM_HEAD_DEFAULT_TEXTURE To CAM_HEAD_RED_LIGHT_TEXTURE
		sc_I\CamTextureID[i] = LoadTexture_Strict("GFX\Map\Textures\camera(" + (i + 1) + ").png", 1, DeleteAllTextures)
		If opt\Atmosphere Then TextureBlend(sc_I\CamTextureID[i], 5)
	Next
End Function

Function RemoveSecurityCamInstances%()
	Local i%
	
	For i = 0 To MaxCamModelIDAmount - 1
		FreeEntity(sc_I\CamModelID[i]) : sc_I\CamModelID[i] = 0
	Next
	sc_I\ScreenTex = 0
	For i = CAM_HEAD_DEFAULT_TEXTURE To CAM_HEAD_RED_LIGHT_TEXTURE
		sc_I\CamTextureID[i] = 0
	Next
	Delete Each SecurityCamInstance
End Function

Const MaxMonitorModelIDAmount% = 2
Const MaxMonitorOverlayIDAmount% = 18

Type MonitorInstance
	Field MonitorModelID%[MaxMonitorModelIDAmount]
	Field MonitorOverlayID%[MaxMonitorOverlayIDAmount]
	Field MonitorTimer#[2]
	Field UpdateCheckpoint%[2]
End Type

Global mon_I.MonitorInstance

; ~ Monitor Model ID Constants
;[Block]
Const MONITOR_DEFAULT_MODEL% = 0
Const MONITOR_CHECKPOINT_MODEL% = 1
;[End Block]

; ~ Monitor Overlay ID Constants
;[Block]
Const MONITOR_DEFAULT_OVERLAY% = 0
Const MONITOR_LOCKDOWN_1_OVERLAY% = 1
Const MONITOR_LOCKDOWN_2_OVERLAY% = 2
Const MONITOR_LOCKDOWN_3_OVERLAY% = 3
Const MONITOR_LOCKDOWN_4_OVERLAY% = 4
Const MONITOR_079_OVERLAY_1% = 5
Const MONITOR_079_OVERLAY_2% = 6
Const MONITOR_079_OVERLAY_3% = 7
Const MONITOR_079_OVERLAY_4% = 8
Const MONITOR_079_OVERLAY_5% = 9
Const MONITOR_079_OVERLAY_6% = 10
Const MONITOR_079_OVERLAY_7% = 11
Const MONITOR_895_OVERLAY_1% = 12
Const MONITOR_895_OVERLAY_2% = 13
Const MONITOR_895_OVERLAY_3% = 14
Const MONITOR_895_OVERLAY_4% = 15
Const MONITOR_895_OVERLAY_5% = 16
Const MONITOR_895_OVERLAY_6% = 17
;[End Block]

Function LoadMonitors%()
	Local i%
	
	mon_I.MonitorInstance = New MonitorInstance
	
	mon_I\MonitorModelID[MONITOR_DEFAULT_MODEL] = LoadMesh_Strict("GFX\Map\Props\monitor2.b3d")
	mon_I\MonitorModelID[MONITOR_CHECKPOINT_MODEL] = LoadMesh_Strict("GFX\Map\Props\monitor_checkpoint.b3d")
	
	For i = 0 To MaxMonitorModelIDAmount - 1
		HideEntity(mon_I\MonitorModelID[i])
	Next
	
	mon_I\MonitorOverlayID[MONITOR_DEFAULT_OVERLAY] = LoadTexture_Strict("GFX\Overlays\monitor_overlay.png", 1, DeleteAllTextures)
	For i = MONITOR_LOCKDOWN_1_OVERLAY To MONITOR_LOCKDOWN_3_OVERLAY
		mon_I\MonitorOverlayID[i] = LoadTexture_Strict("GFX\Map\Textures\lockdown_screen(" + i + ").png", 1, DeleteAllTextures)
		If opt\Atmosphere Then TextureBlend(mon_I\MonitorOverlayID[i], 5)
	Next
	mon_I\MonitorOverlayID[MONITOR_LOCKDOWN_4_OVERLAY] = CreateTextureUsingCacheSystem(1, 1)
	SetBuffer(TextureBuffer(mon_I\MonitorOverlayID[MONITOR_LOCKDOWN_4_OVERLAY]))
	ClsColor(0, 0, 0)
	Cls()
	SetBuffer(BackBuffer())
	
	For i = MONITOR_079_OVERLAY_1 To MONITOR_079_OVERLAY_7
		mon_I\MonitorOverlayID[i] = LoadTexture_Strict("GFX\Overlays\scp_079_overlay(" + (i - 4) + ").png", 1, DeleteAllTextures)
	Next
	
	For i = MONITOR_895_OVERLAY_1 To MONITOR_895_OVERLAY_6
		mon_I\MonitorOverlayID[i] = LoadTexture_Strict("GFX\Overlays\scp_895_overlay(" + (i - 11) + ").png", 1, DeleteAllTextures)
	Next
End Function

Function RemoveMonitorInstances%()
	Local i%
	
	For i = 0 To MaxMonitorModelIDAmount - 1
		FreeEntity(mon_I\MonitorModelID[i]) : mon_I\MonitorModelID[i] = 0
	Next
	mon_I\MonitorOverlayID[MONITOR_DEFAULT_OVERLAY] = 0
	For i = MONITOR_LOCKDOWN_1_OVERLAY To MONITOR_LOCKDOWN_3_OVERLAY
		mon_I\MonitorOverlayID[i] = 0
	Next
	For i = MONITOR_079_OVERLAY_1 To MONITOR_079_OVERLAY_7
		mon_I\MonitorOverlayID[i] = 0
	Next
	For i = MONITOR_895_OVERLAY_1 To MONITOR_895_OVERLAY_6
		mon_I\MonitorOverlayID[i] = 0
	Next
	Delete Each MonitorInstance
End Function

Const MaxNPCModelIDAmount% = 31
Const MaxNPCTextureID% = 17

Type NPCInstance
	Field NPCModelID%[MaxNPCModelIDAmount]
	Field NPCTextureID%[MaxNPCTextureID]
	Field Curr173.NPCs
	Field Curr106.NPCs
	Field Curr096.NPCs
	Field Curr513_1.NPCs
	Field Curr049.NPCs
	Field Curr066.NPCs
	Field MTFLeader.NPCs
	Field IsHalloween%, IsNewYear%, IsAprilFools%
End Type

Global n_I.NPCInstance
; ~ NPC Model ID Constants
;[Block]
Const NPC_008_1_MODEL% = 0
Const NPC_035_TENTACLE_MODEL% = 1
Const NPC_049_MODEL% = 2
Const NPC_049_2_MODEL% = 3
Const NPC_066_MODEL% = 4
Const NPC_096_MODEL% = 5
Const NPC_106_MODEL% = 6
Const NPC_173_MODEL% = 7
Const NPC_173_HEAD_MODEL% = 8
Const NPC_173_BOX_MODEL% = 9
Const NPC_205_DEMON_1_MODEL% = 10
Const NPC_205_DEMON_2_MODEL% = 11
Const NPC_205_DEMON_3_MODEL% = 12
Const NPC_205_WOMAN_MODEL% = 13
Const NPC_372_MODEL% = 14
Const NPC_513_1_MODEL% = 15
Const NPC_860_2_MODEL% = 16
Const NPC_939_MODEL% = 17
Const NPC_966_MODEL% = 18
Const NPC_1048_MODEL% = 19
Const NPC_1048_A_MODEL% = 20
Const NPC_APACHE_MODEL% = 21
Const NPC_APACHE_ROTOR_1_MODEL% = 22
Const NPC_APACHE_ROTOR_2_MODEL% = 23
Const NPC_CLERK_MODEL% = 24
Const NPC_CLASS_D_MODEL% = 25
Const NPC_DUCK_MODEL% = 26
Const NPC_GUARD_MODEL% = 27
Const NPC_MTF_MODEL% = 28
Const NPC_NAZI_MODEL% = 29
Const NPC_VEHICLE_MODEL% = 30
;[End Block]

; ~ NPC Texture ID Constants
;[Block]
Const NPC_CLASS_D_GONZALES_TEXTURE% = 0
Const NPC_CLASS_D_BENJAMIN_TEXTURE% = 1
Const NPC_CLASS_D_SCIENTIST_TEXTURE% = 2
Const NPC_CLASS_D_FRANKLIN_TEXTURE% = 3
Const NPC_CLASS_D_MAYNARD_TEXTURE% = 4
Const NPC_CLASS_D_CLASS_D_TEXTURE% = 5
Const NPC_CLASS_D_D9341_TEXTURE% = 6
Const NPC_CLASS_D_BODY_1_TEXTURE% = 7
Const NPC_CLASS_D_BODY_2_TEXTURE% = 8
Const NPC_CLASS_D_JANITOR_1_TEXTURE% = 9
Const NPC_CLASS_D_JANITOR_2_TEXTURE% = 10
Const NPC_CLASS_D_VICTIM_008_TEXTURE% = 11
Const NPC_CLASS_D_VICTIM_035_TEXTURE% = 12
Const NPC_CLASS_D_VICTIM_409_TEXTURE% = 13
Const NPC_CLASS_D_VICTIM_939_1_TEXTURE% = 14
Const NPC_CLASS_D_VICTIM_939_2_TEXTURE% = 15
Const NPC_CLERK_VICTIM_205_TEXTURE% = 16

Const NPC_096_BLOODY_TEXTURE% = 17
;[End Block]

Function LoadNPCs%()
	Local i%
	
	n_I.NPCInstance = New NPCInstance
	
	n_I\NPCModelID[NPC_008_1_MODEL] = LoadAnimMesh_Strict("GFX\NPCs\scp_008_1.b3d")
	
	n_I\NPCModelID[NPC_035_TENTACLE_MODEL] = LoadAnimMesh_Strict("GFX\NPCs\scp_035_tentacle.b3d")
	
	n_I\NPCModelID[NPC_049_MODEL] = LoadAnimMesh_Strict("GFX\NPCs\scp_049.b3d")
	
	n_I\NPCModelID[NPC_049_2_MODEL] = LoadAnimMesh_Strict("GFX\NPCs\scp_049_2.b3d")
	
	n_I\NPCModelID[NPC_066_MODEL] = LoadAnimMesh_Strict("GFX\NPCs\scp_066.b3d")
	
	n_I\NPCModelID[NPC_096_MODEL] = LoadAnimMesh_Strict("GFX\NPCs\scp_096.b3d")
	
	n_I\NPCModelID[NPC_106_MODEL] = LoadAnimMesh_Strict("GFX\NPCs\scp_106.b3d")
	
	n_I\NPCModelID[NPC_173_MODEL] = LoadMesh_Strict("GFX\NPCs\scp_173_body.b3d")
	n_I\NPCModelID[NPC_173_HEAD_MODEL] = LoadMesh_Strict("GFX\NPCs\scp_173_head.b3d")
	n_I\NPCModelID[NPC_173_BOX_MODEL] = LoadMesh_Strict("GFX\NPCs\scp_173_box.b3d")
	
	n_I\NPCModelID[NPC_205_DEMON_1_MODEL] = LoadAnimMesh_Strict("GFX\NPCs\scp_205_demon.b3d")
	n_I\NPCModelID[NPC_205_DEMON_2_MODEL] = LoadAnimMesh_Strict("GFX\NPCs\scp_205_demon(2).b3d")
	n_I\NPCModelID[NPC_205_DEMON_3_MODEL] = LoadAnimMesh_Strict("GFX\NPCs\scp_205_demon(3).b3d")
	n_I\NPCModelID[NPC_205_WOMAN_MODEL] = LoadAnimMesh_Strict("GFX\NPCs\scp_205_woman.b3d")
	
	n_I\NPCModelID[NPC_372_MODEL] = LoadAnimMesh_Strict("GFX\NPCs\scp_372.b3d")
	
	n_I\NPCModelID[NPC_513_1_MODEL] = LoadAnimMesh_Strict("GFX\NPCs\scp_513_1.b3d")
	
	n_I\NPCModelID[NPC_860_2_MODEL] = LoadAnimMesh_Strict("GFX\NPCs\scp_860_2.b3d")
	
	n_I\NPCModelID[NPC_939_MODEL] = LoadAnimMesh_Strict("GFX\NPCs\scp_939.b3d")
	
	n_I\NPCModelID[NPC_966_MODEL] = LoadAnimMesh_Strict("GFX\NPCs\scp_966.b3d")
	
	n_I\NPCModelID[NPC_1048_MODEL] = LoadAnimMesh_Strict("GFX\NPCs\scp_1048.b3d")
	n_I\NPCModelID[NPC_1048_A_MODEL] = LoadAnimMesh_Strict("GFX\NPCs\scp_1048_a.b3d")
	
	n_I\NPCModelID[NPC_APACHE_MODEL] = LoadAnimMesh_Strict("GFX\NPCs\apache.b3d")
	n_I\NPCModelID[NPC_APACHE_ROTOR_1_MODEL] = LoadAnimMesh_Strict("GFX\NPCs\apache_rotor.b3d")
	n_I\NPCModelID[NPC_APACHE_ROTOR_2_MODEL] = LoadAnimMesh_Strict("GFX\NPCs\apache_rotor(2).b3d")
	
	n_I\NPCModelID[NPC_CLERK_MODEL] = LoadAnimMesh_Strict("GFX\NPCs\clerk.b3d")
	
	n_I\NPCModelID[NPC_CLASS_D_MODEL] = LoadAnimMesh_Strict("GFX\NPCs\class_d.b3d")
	
	n_I\NPCModelID[NPC_DUCK_MODEL] = LoadAnimMesh_Strict("GFX\NPCs\duck.b3d")
	
	n_I\NPCModelID[NPC_GUARD_MODEL] = LoadAnimMesh_Strict("GFX\NPCs\guard.b3d")
	
	n_I\NPCModelID[NPC_MTF_MODEL] = LoadAnimMesh_Strict("GFX\NPCs\MTF.b3d")
	
	n_I\NPCModelID[NPC_NAZI_MODEL] = LoadAnimMesh_Strict("GFX\NPCs\nazi_officer.b3d")
	
	n_I\NPCModelID[NPC_VEHICLE_MODEL] = LoadAnimMesh_Strict("GFX\NPCs\vehicle.b3d")
	
	For i = 0 To MaxNPCModelIDAmount - 2 Step 2
		HideEntity(n_I\NPCModelID[i])
		HideEntity(n_I\NPCModelID[i + 1])
	Next
	
	n_I\NPCTextureID[NPC_CLASS_D_GONZALES_TEXTURE] = LoadTexture_Strict("GFX\NPCs\Gonzales.png", 1, DeleteAllTextures)
	n_I\NPCTextureID[NPC_CLASS_D_BENJAMIN_TEXTURE] = LoadTexture_Strict("GFX\NPCs\D_9341(2).png", 1, DeleteAllTextures)
	n_I\NPCTextureID[NPC_CLASS_D_SCIENTIST_TEXTURE] = LoadTexture_Strict("GFX\NPCs\scientist.png", 1, DeleteAllTextures)
	n_I\NPCTextureID[NPC_CLASS_D_FRANKLIN_TEXTURE] = LoadTexture_Strict("GFX\NPCs\Franklin.png", 1, DeleteAllTextures)
	n_I\NPCTextureID[NPC_CLASS_D_MAYNARD_TEXTURE] = LoadTexture_Strict("GFX\NPCs\Maynard.png", 1, DeleteAllTextures)
	n_I\NPCTextureID[NPC_CLASS_D_CLASS_D_TEXTURE] = LoadTexture_Strict("GFX\NPCs\class_d(2).png", 1, DeleteAllTextures)
	If opt\IntroEnabled Then n_I\NPCTextureID[NPC_CLASS_D_D9341_TEXTURE] = LoadTexture_Strict("GFX\NPCs\D_9341.png", 1, DeleteAllTextures)
	n_I\NPCTextureID[NPC_CLASS_D_BODY_1_TEXTURE] = LoadTexture_Strict("GFX\NPCs\body.png", 1, DeleteAllTextures)
	n_I\NPCTextureID[NPC_CLASS_D_BODY_2_TEXTURE] = LoadTexture_Strict("GFX\NPCs\body(2).png", 1, DeleteAllTextures)
	n_I\NPCTextureID[NPC_CLASS_D_JANITOR_1_TEXTURE] = LoadTexture_Strict("GFX\NPCs\janitor.png", 1, DeleteAllTextures)
	n_I\NPCTextureID[NPC_CLASS_D_JANITOR_2_TEXTURE] = LoadTexture_Strict("GFX\NPCs\janitor(2).png", 1, DeleteAllTextures)
	n_I\NPCTextureID[NPC_CLASS_D_VICTIM_008_TEXTURE] = LoadTexture_Strict("GFX\NPCs\scp_008_1_victim.png", 1, DeleteAllTextures)
	n_I\NPCTextureID[NPC_CLASS_D_VICTIM_035_TEXTURE] = LoadTexture_Strict("GFX\NPCs\scp_035_victim.png", 1, DeleteAllTextures)
	n_I\NPCTextureID[NPC_CLASS_D_VICTIM_409_TEXTURE] = LoadTexture_Strict("GFX\NPCs\scp_409_victim.png", 1, DeleteAllTextures)
	n_I\NPCTextureID[NPC_CLASS_D_VICTIM_939_1_TEXTURE] = LoadTexture_Strict("GFX\NPCs\scp_939_victim.png", 1, DeleteAllTextures)
	n_I\NPCTextureID[NPC_CLASS_D_VICTIM_939_2_TEXTURE] = LoadTexture_Strict("GFX\NPCs\scp_939_victim(2).png", 1, DeleteAllTextures)
	n_I\NPCTextureID[NPC_CLERK_VICTIM_205_TEXTURE] = LoadTexture_Strict("GFX\NPCs\clerk2.png", 1, DeleteAllTextures)
	
	n_I\NPCTextureID[NPC_096_BLOODY_TEXTURE] = LoadTexture_Strict("GFX\NPCs\scp_096_bloody.png", 1, DeleteAllTextures)
	
	If opt\Atmosphere
		For i = NPC_CLASS_D_GONZALES_TEXTURE To NPC_096_BLOODY_TEXTURE
			Local Skip%
			
			Skip = ((Not opt\IntroEnabled) And i = NPC_CLASS_D_D9341_TEXTURE)
			If (Not Skip) Then TextureBlend(n_I\NPCTextureID[i], 5)
		Next
	EndIf
End Function

Function RemoveNPCInstances%()
	Local i%
	
	For i = 0 To MaxNPCModelIDAmount - 1
		FreeEntity(n_I\NPCModelID[i]) : n_I\NPCModelID[i] = 0
	Next
	For i = NPC_CLASS_D_GONZALES_TEXTURE To NPC_096_BLOODY_TEXTURE
		n_I\NPCTextureID[i] = 0
	Next
	Delete Each NPCInstance
End Function

Const MaxElevatorModelIDAmount% = 3

Type ElevatorModelInstance
	Field ElevatorModel%[MaxElevatorModelIDAmount]
End Type

Global elev_m_I.ElevatorModelInstance

Function LoadElevatorModels%()
	Local i%
	
	elev_m_I.ElevatorModelInstance = New ElevatorModelInstance
	
	elev_m_I\ElevatorModel[0] = LoadRMesh("GFX\Map\Elevators\Elevator.rmesh", Null)
	elev_m_I\ElevatorModel[1] = LoadRMesh("GFX\Map\Elevators\Elevator_2.rmesh", Null)
	elev_m_I\ElevatorModel[2] = LoadRMesh("GFX\Map\Elevators\Elevator_3.rmesh", Null)
	
	For i = 0 To MaxElevatorModelIDAmount - 1
		HideEntity(elev_m_I\ElevatorModel[i])
	Next
	
End Function

Const MaxMTModelIDAmount% = 7
Const MaxLightSpriteIDAmount% = 3

Type MiscInstance
	Field MTModelID%[MaxMTModelIDAmount]
	Field CupLiquid%
	Field LightSpriteID[MaxLightSpriteIDAmount]
	Field AdvancedLightSprite%
	Field LightCone%
End Type

Global misc_I.MiscInstance

; ~ Light Sprite ID Constants
;[Block]
Const LIGHT_SPRITE_DEFAULT% = 0
Const LIGHT_SPRITE_RED% = 1
;[End Block]

Function LoadMisc%()
	Local i%
	
	misc_I.MiscInstance = New MiscInstance
	
	misc_I\MTModelID[0] = LoadRMesh("GFX\Map\Rooms\MT\mt1.rmesh", Null)
	misc_I\MTModelID[1] = LoadRMesh("GFX\Map\Rooms\MT\mt2.rmesh", Null)
	misc_I\MTModelID[2] = LoadRMesh("GFX\Map\Rooms\MT\mt2C.rmesh", Null)
	misc_I\MTModelID[3] = LoadRMesh("GFX\Map\Rooms\MT\mt3.rmesh", Null)
	misc_I\MTModelID[4] = LoadRMesh("GFX\Map\Rooms\MT\mt4.rmesh", Null)
	misc_I\MTModelID[5] = LoadRMesh("GFX\Map\Rooms\MT\mt2_elevator.rmesh", Null)
	misc_I\MTModelID[6] = LoadRMesh("GFX\Map\Rooms\MT\mt1_generator.rmesh", Null)
	
	For i = 0 To MaxMTModelIDAmount - 1
		HideEntity(misc_I\MTModelID[i])
	Next
	
	misc_I\CupLiquid = LoadMesh_Strict("GFX\Items\Usable\cup_liquid.b3d")
	HideEntity(misc_I\CupLiquid)
	
	misc_I\LightCone = LoadMesh_Strict("GFX\Map\Rooms\Light_Cone.b3d")
	HideEntity(misc_I\LightCone)
	
	For i = LIGHT_SPRITE_DEFAULT To LIGHT_SPRITE_RED
		misc_I\LightSpriteID[i] = LoadTexture_Strict("GFX\Particles\light(" + i + ").png", 1, DeleteAllTextures)
	Next
	misc_I\AdvancedLightSprite = LoadTexture_Strict("GFX\Particles\advanced_light.png", 1, DeleteAllTextures)
End Function

Function RemoveMiscInstances%()
	Local i%
	
	For i = 0 To MaxMTModelIDAmount - 1
		FreeEntity(misc_I\MTModelID[i]) : misc_I\MTModelID[i] = 0
	Next
	FreeEntity(misc_I\CupLiquid) : misc_I\CupLiquid = 0
	For i = LIGHT_SPRITE_DEFAULT To LIGHT_SPRITE_RED
		misc_I\LightSpriteID[i] = 0
	Next
	misc_I\AdvancedLightSprite = 0
	misc_I\LightCone = 0
	Delete Each MiscInstance
End Function

Function LoadMaterials%(File$)
	CatchErrors("LoadMaterials(" + File + ")")
	
	Local Loc$
	Local mat.Materials = Null
	Local StrTemp$ = ""
	Local f% = OpenFile_Strict(File)
	
	While (Not Eof(f))
		Loc = Trim(ReadLine(f))
		If Left(Loc, 1) = "["
			Loc = Mid(Loc, 2, Len(Loc) - 2)
			mat.Materials = New Materials
			mat\Name = Lower(Loc)
			If opt\BumpEnabled
				StrTemp = IniGetString(File, Loc, "bump")
				If StrTemp <> ""
					mat\Bump = LoadTexture_Strict(StrTemp, 256)
					ApplyBumpMap(mat\Bump)
				EndIf
			EndIf
			mat\StepSound = (IniGetInt(File, Loc, "stepsound") + 1)
			mat\IsDiffuseAlpha = IniGetInt(File, Loc, "transparent")
			mat\UseMask = IniGetInt(File, Loc, "masked")
		EndIf
	Wend
	
	CloseFile(f)
	
	CatchErrors("Uncaught: LoadMaterials(" + File + ")")
End Function

Function InitLoadingScreens%(File$)
	Local LocalLoadingScreen% = JsonParseFromFile(lang\LanguagePath + File)
	
	If JsonIsArray(LocalLoadingScreen) ; ~ Has localized loading screens -> Use localized only
		LoadingScreens = JsonGetArray(LocalLoadingScreen)
	Else
		LoadingScreens = JsonGetArray(JsonParseFromFile(File))
	EndIf
End Function

Const ItemsPath$ = "GFX\Items\"
Const ItemHUDTexturePath$ = "GFX\Items\HUD Textures\"
Const ItemTexturePath$ = "GFX\Items\"
Const ItemINVIconPath$ = "GFX\Items\Inventory Icons\"

Function LoadItems%()
	Local it.ItemTemplates, it2.ItemTemplates
	
	;! ~ [ATTACHMENT ITEMS]
	
	; ~ (Barrel Attachments)
	
	; ~ (Sight Attachments)
	
	; ~ (Magazine Attachments)
	
;! ~ [CONSUMABLE ITEMS]
	
	; ~ (Eye Drops)
	
	it.ItemTemplates = CreateItemTemplate(GetLocalString("items", "eyedrops"), "ReVision Eyedrops", "eyedrops", "Usable\eye_drops.b3d", "INV_eye_drops.png", "", 0.0012, 1, 0.05, -1)
	it.ItemTemplates = CreateItemTemplate(GetLocalString("items", "eyedrops_2"), "Eyedrops", "fineeyedrops", "Usable\eye_drops.b3d", "INV_eye_drops.png", "", 0.0012, 1, 0.05, -1)
	it.ItemTemplates = CreateItemTemplate(GetLocalString("items", "eyedrops_2"), "Eyedrops", "supereyedrops", "Usable\eye_drops.b3d", "INV_eye_drops.png", "", 0.0012, 1, 0.05, -1)
	it.ItemTemplates = CreateItemTemplate(GetLocalString("items", "eyedrops.red"), "RedVision Eyedrops", "eyedrops2", "Usable\eye_drops.b3d", "INV_eye_drops_red.png", "", 0.0012, 1, 0.05, -1, "Usable\eye_drops_red.png")
	
	; ~ (First Aid Kits)
	
	it.ItemTemplates = CreateItemTemplate(GetLocalString("items", "fak"), "First Aid Kit", "firstaid", "Usable\first_aid_kit.b3d", "INV_first_aid_kit.png", "", 0.05, 1, 0.8, -1)
	it.ItemTemplates = CreateItemTemplate(GetLocalString("items", "cfak"), "Compact First Aid Kit", "finefirstaid", "Usable\first_aid_kit.b3d", "INV_first_aid_kit.png", "", 0.03, 1, 0.6, -1)
	it.ItemTemplates = CreateItemTemplate(GetLocalString("items", "bfak"), "Blue First Aid Kit", "firstaid2", "Usable\first_aid_kit.b3d", "INV_first_aid_kit(2).png", "", 0.03, 1, 0.9, -1, "Usable\first_aid_kit(2).png")
	it.ItemTemplates = CreateItemTemplate(GetLocalString("items", "sb"), "Strange Bottle", "veryfinefirstaid", "Usable\eye_drops.b3d", "INV_strange_bottle.png", "", 0.002, 1, 0.06, -1, "Usable\strange_bottle.png")
	
	; ~ (Syringes)
	
	it.ItemTemplates = CreateItemTemplate(GetLocalString("items", "syringe"), "Syringe", "syringe", "Usable\syringe.b3d", "INV_syringe.png", "", 0.005, 21, 0.3, -1)
	it.ItemTemplates = CreateItemTemplate(GetLocalString("items", "syringe"), "Syringe", "finesyringe", "Usable\syringe.b3d", "INV_syringe.png", "", 0.005, 21, 0.4, -1)
	it.ItemTemplates = CreateItemTemplate(GetLocalString("items", "syringe"), "Syringe", "veryfinesyringe", "Usable\syringe.b3d", "INV_syringe.png", "", 0.005, 21, 0.4, -1)
	it.ItemTemplates = CreateItemTemplate(GetLocalString("items", "syringe"), "Syringe", "syringeinf", "Usable\syringe.b3d", "INV_syringe_infect.png", "", 0.005, 2, 0.3, -1, "Usable\syringe_infect.png")
	
	; ~ (Cups)
	
	it.ItemTemplates = CreateItemTemplate(GetLocalString("items", "cup"), "Cup", "cup", "Usable\cup.b3d", "INV_cup.png", "", 0.04, 2, 0.31, -1)
	it.ItemTemplates = CreateItemTemplate(GetLocalString("items", "emptycup"), "Empty Cup", "emptycup", "Usable\cup.b3d", "INV_cup.png", "", 0.04, 2, 0.01, -1)
	
;! ~ [ELECTRONIC ITEMS]
	
	; ~ (Batteries)
	
	it.ItemTemplates = CreateItemTemplate(GetLocalString("items", "bat"), "9V Battery", "bat", "Electronics\battery.b3d", "INV_battery_9v.png", "", 0.008, 1, 0.1, -1)
	it.ItemTemplates = CreateItemTemplate(GetLocalString("items", "45bat"), "4.5V Battery", "coarsebat", "Electronics\battery.b3d", "INV_battery_4.5v.png", "", 0.008, 1, 0.1, -1, "Electronics\battery_4.5V.png")
	it.ItemTemplates = CreateItemTemplate(GetLocalString("items", "18bat"), "18V Battery", "finebat", "Electronics\battery.b3d", "INV_battery_18v.png", "", 0.01, 1, 0.1, -1, "Electronics\battery_18V.png")
	it.ItemTemplates = CreateItemTemplate(GetLocalString("items", "999bat"), "999V Battery", "veryfinebat", "Electronics\battery.b3d", "INV_battery_999v.png", "", 0.009, 1, 0.1, -1, "Electronics\battery_999V.png")
	it.ItemTemplates = CreateItemTemplate(GetLocalString("items", "killbat"), "Strange Battery", "killbat", "Electronics\battery.b3d", "INV_strange_battery.png", "", 0.01, 1, 0.1, -1, "Electronics\strange_battery.png")
	
	; ~ (Key Cards)
	
	it.ItemTemplates = CreateItemTemplate(GetLocalString("items", "key0"), "Level 0 Key Card", "key0", "Electronics\key_card.b3d", "INV_key_card_lvl_0.png", "", 0.0004, 1, 0.01, 5)
	it.ItemTemplates = CreateItemTemplate(GetLocalString("items", "key1"), "Level 1 Key Card", "key1", "Electronics\key_card.b3d", "INV_key_card_lvl_1.png", "", 0.0004, 1, 0.01, 5, "Electronics\key_card_lvl_1.png")
	it.ItemTemplates = CreateItemTemplate(GetLocalString("items", "key2"), "Level 2 Key Card", "key2", "Electronics\key_card.b3d", "INV_key_card_lvl_2.png", "", 0.0004, 1, 0.01, 5, "Electronics\key_card_lvl_2.png")
	it.ItemTemplates = CreateItemTemplate(GetLocalString("items", "key3"), "Level 3 Key Card", "key3", "Electronics\key_card.b3d", "INV_key_card_lvl_3.png", "", 0.0004, 1, 0.01, 5, "Electronics\key_card_lvl_3.png")
	it.ItemTemplates = CreateItemTemplate(GetLocalString("items", "key4"), "Level 4 Key Card", "key4", "Electronics\key_card.b3d", "INV_key_card_lvl_4.png", "", 0.0004, 1, 0.01, 5, "Electronics\key_card_lvl_4.png")
	it.ItemTemplates = CreateItemTemplate(GetLocalString("items", "key5"), "Level 5 Key Card", "key5", "Electronics\key_card.b3d", "INV_key_card_lvl_5.png", "", 0.0004, 1, 0.01, 5, "Electronics\key_card_lvl_5.png")
	it.ItemTemplates = CreateItemTemplate(GetLocalString("items", "key6"), "Level 6 Key Card", "key6", "Electronics\key_card.b3d", "INV_key_card_lvl_6.png", "", 0.0004, 1, 0.01, 5, "Electronics\key_card_lvl_6.png")
	it.ItemTemplates = CreateItemTemplate(GetLocalString("items", "keyomni"), "Key Card Omni", "keyomni", "Electronics\key_card.b3d", "INV_key_card_lvl_omni.png", "", 0.0004, 1, 0.01, 5, "Electronics\key_card_lvl_omni.png")
	
	; ~ (Navigators)
	
	it.ItemTemplates = CreateItemTemplate(GetLocalString("items", "nav"), "S-NAV Navigator", "nav", "Electronics\navigator.b3d", "INV_navigator.png", "navigator.png", 0.0008, 1, 0.7, -1)
	it.ItemTemplates = CreateItemTemplate(GetLocalString("items", "navulti"), "S-NAV Navigator Ultimate", "navulti", "Electronics\navigator.b3d", "INV_navigator.png", "navigator.png", 0.0008, 1, 0.7, -1)
	it.ItemTemplates = CreateItemTemplate(GetLocalString("items", "nav300"), "S-NAV 300 Navigator", "nav300", "Electronics\navigator.b3d", "INV_navigator.png", "navigator.png", 0.0008, 1, 0.7, -1)
	it.ItemTemplates = CreateItemTemplate(GetLocalString("items", "nav310"), "S-NAV 310 Navigator", "nav310", "Electronics\navigator.b3d", "INV_navigator.png", "navigator.png", 0.0008, 1, 0.7, -1)
	
	; ~ (Radio Transceivers)
	
	it.ItemTemplates = CreateItemTemplate(GetLocalString("items", "radio"), "Radio Transceiver", "radio", "Electronics\radio.b3d", "INV_radio.png", "Electronics\radio.png", 1.0, 1, 0.9, -1)
	it.ItemTemplates = CreateItemTemplate(GetLocalString("items", "radio"), "Radio Transceiver", "fineradio", "Electronics\radio.b3d", "INV_radio.png", "Electronics\radio.png", 1.0, 1, 0.9, -1)
	it.ItemTemplates = CreateItemTemplate(GetLocalString("items", "radio"), "Radio Transceiver", "veryfineradio", "Electronics\radio.b3d", "INV_radio.png", "Electronics\radio.png", 1.0, 1, 0.9, -1)
	it.ItemTemplates = CreateItemTemplate(GetLocalString("items", "radio"), "Radio Transceiver", "18vradio", "Electronics\radio.b3d", "INV_radio.png", "Electronics\radio.png", 1.02, 1, 0.9, -1)
	
	; ~ (Misc Electronics)
	
	it.ItemTemplates = CreateItemTemplate(GetLocalString("items", "electronics"), "Electronical Components", "electronics", "Electronics\circuits.b3d", "INV_circuits.png", "", 0.0011, 1, 0.1, -1)
	it.ItemTemplates = CreateItemTemplate(GetLocalString("items", "mastercard"), "Mastercard", "mastercard", "Electronics\key_card.b3d", "INV_master_card.png", "", 0.0004, 1, 0.01, -1, "Electronics\master_card.png")
	
;! ~ [PAPER ITEMS]
	
	; ~ (Badges)
	
	it.ItemTemplates = CreateItemTemplate(GetLocalString("items", "badge"), "Emily Ross' Badge", "badge", "Paper\badge.b3d", "INV_Emily_badge.png", "Paper\Emily_badge.png", 0.0001, 1, 0.002, -1)
	
	; ~ (Playing Cards)
	
	it.ItemTemplates = CreateItemTemplate(GetLocalString("items", "playcard"), "Playing Card", "playcard", "Electronics\key_card.b3d", "INV_playing_card.png", "", 0.0004, 1, 0.005, -1, "Paper\playing_card.png")
	
;! ~ [SCP ITEMS]
	
	it.ItemTemplates = CreateItemTemplate("SCP-005", "SCP-005", "scp005", "SCPs\scp_005.b3d", "INV_scp_005.png", "", 0.0004, 1, 0.2, -1)
	it.ItemTemplates = CreateItemTemplate(GetLocalString("items", "148"), "SCP-148 Ingot", "scp148ingot", "SCPs\scp_148.b3d", "INV_scp_148.png", "", RoomScale, 2, 1.2, -1)
	it.ItemTemplates = CreateItemTemplate("SCP-427", "SCP-427", "scp427", "SCPs\scp_427.b3d", "INV_scp_427.png", "", 0.001, 3, 0.2, -1)
	it.ItemTemplates = CreateItemTemplate("SCP-500-01", "SCP-500-01", "scp500pill", "SCPs\pill.b3d", "INV_scp_500_pill.png", "", 0.0001, 2, 0.002, -1) : EntityColor(it\OBJ, 255.0, 0.0, 0.0)
	it.ItemTemplates = CreateItemTemplate("SCP-268", "SCP-268", "scp268", "SCPs\scp_268.b3d", "INV_scp_268.png", "", 0.09, 2, 0.4, -1) : it\IsHead = True
	it.ItemTemplates = CreateItemTemplate("SCP-268", "SCP-268", "scp268fine", "SCPs\scp_268.b3d", "INV_scp_268.png", "", 0.09, 2, 0.4, -1) : it\IsHead = True
	it.ItemTemplates = CreateItemTemplate("SCP-500", "SCP-500", "scp500", "SCPs\scp_500.b3d", "INV_scp_500.png", "", 0.035, 2, 0.2, -1)
	it.ItemTemplates = CreateItemTemplate("SCP-513", "SCP-513", "scp513", "SCPs\scp_513.b3d", "INV_scp_513.png", "", 0.1, 2, 1.2, -1)
	it.ItemTemplates = CreateItemTemplate("SCP-714", "SCP-714", "scp714", "SCPs\scp_714.b3d", "INV_scp_714.png", "", 0.3, 3, 0.08, -1)
	it.ItemTemplates = CreateItemTemplate("SCP-714", "SCP-714", "coarse714", "SCPs\scp_714.b3d", "INV_scp_714_grey.png", "", 0.3, 3, 0.08, -1, "SCPs\scp_714_grey.png")
	it.ItemTemplates = CreateItemTemplate("SCP-714", "SCP-714", "fine714", "SCPs\scp_714.b3d", "INV_scp_714_blue.png", "", 0.3, 3, 0.08, -1, "SCPs\scp_714_blue.png")
	it.ItemTemplates = CreateItemTemplate("SCP-860", "SCP-860", "scp860", "SCPs\scp_860.b3d", "INV_scp_860.png", "", 0.003, 3, 0.09, -1)
	it.ItemTemplates = CreateItemTemplate("SCP-1025", "SCP-1025", "scp1025", "SCPs\scp_1025.b3d", "INV_scp_1025.png", "", 0.1, 0, 2.0, -1)
	it.ItemTemplates = CreateItemTemplate("SCP-1123", "SCP-1123", "scp1123", "SCPs\scp_1123.b3d", "INV_scp_1123.png", "", 0.015, 2, 3.2, -1)
	
;! ~ [FAKE SCP ITEMS]
	
	it.ItemTemplates = CreateItemTemplate(GetLocalString("items", "metalpanel"), "Metal Panel", "scp148", "SCPs\metal_panel.b3d", "INV_metal_panel.png", "", RoomScale, 2, 1.5, -1)
	it.ItemTemplates = CreateItemTemplate(GetLocalString("items", "cap"), "Newsboy Cap", "scp268fake", "SCPs\scp_268.b3d", "INV_scp_268.png", "", 0.09, 2, 0.4, -1) : it\IsHead = True
	it.ItemTemplates = CreateItemTemplate(GetLocalString("items", "joint"), "Joint", "joint", "SCPs\scp_420_j.b3d", "INV_scp_420_j.png", "", 0.0004, 2, 0.03, -1)
	it.ItemTemplates = CreateItemTemplate(GetLocalString("items", "smellyjoint"), "Smelly Joint", "scp420s", "SCPs\scp_420_j.b3d", "INV_scp_420_j.png", "", 0.0004, 2, 0.03, -1)
	it.ItemTemplates = CreateItemTemplate(GetLocalString("items", "420j"), "Some SCP-420-J", "scp420j", "SCPs\scp_420_j.b3d", "INV_scp_420_j.png", "", 0.0005, 2, 0.03, -1)
	it.ItemTemplates = CreateItemTemplate(GetLocalString("items", "500death"), "Upgraded Pill", "scp500pilldeath", "SCPs\pill.b3d", "INV_scp_500_pill.png", "", 0.0001, 2, 0.002, -1) : EntityColor(it\OBJ, 255.0, 0.0, 0.0)
	it.ItemTemplates = CreateItemTemplate(GetLocalString("items", "pill"), "Pill", "pill", "SCPs\pill.b3d", "INV_pill.png", "", 0.0001, 2, 0.002, -1) : EntityColor(it\OBJ, 255.0, 255.0, 255.0)
	it.ItemTemplates = CreateItemTemplate(GetLocalString("items", "ring"), "Green Jade Ring", "ring", "SCPs\scp_714.b3d", "INV_scp_714.png", "", 0.2, 3, 0.002, -1)
	it.ItemTemplates = CreateItemTemplate(GetLocalString("items", "book"), "Book", "book", "SCPs\scp_1025.b3d", "INV_book.png", "", 0.07, 0, 1.5, -1, "SCPs\book.png")
	it.ItemTemplates = CreateItemTemplate(GetLocalString("items", "cigarette"), "Cigarette", "cigarette", "SCPs\scp_420_j.b3d", "INV_scp_420_j.png", "", 0.0004, 2, 0.03, -1)
	
;! ~ [USABLE ITEMS]
	
	; ~ (Storage Items)
	
	it.ItemTemplates = CreateItemTemplate(GetLocalString("items", "clipboard"), "Clipboard", "clipboard", "Usable\clipboard.b3d", "INV_clipboard.png", "", 0.003, 1, 0.3, -1, "", "INV_clipboard(2).png", 1)
	it.ItemTemplates = CreateItemTemplate(GetLocalString("items", "wallet"), "Wallet", "wallet", "Usable\wallet.b3d", "INV_wallet.png", "", 0.0006, 2, 0.4, -1, "", "", 1)
	
	; ~ (Severed Hands)
	
	it.ItemTemplates = CreateItemTemplate(GetLocalString("items", "hand_1"), "White Severed Hand", "hand", "Usable\severed_hand.b3d", "INV_severed_hand(1).png", "", 0.03, 2, 0.7, -1)
	it.ItemTemplates = CreateItemTemplate(GetLocalString("items", "hand_2"), "Black Severed Hand", "hand2", "Usable\severed_hand.b3d", "INV_severed_hand(2).png", "", 0.03, 2, 0.8, -1, "Usable\severed_hand(2).png")
	it.ItemTemplates = CreateItemTemplate(GetLocalString("items", "hand_3"), "Yellow Severed Hand", "hand3", "Usable\severed_hand.b3d", "INV_severed_hand(3).png", "", 0.03, 2, 0.6, -1, "Usable\severed_hand(3).png")
	
;! ~ [WEARABLE ITEMS]
	
	; ~ (Helmets)
	
	it.ItemTemplates = CreateItemTemplate(GetLocalString("items", "helmet"), "Ballistic Helmet", "helmet", "Wearable\helmet.b3d", "INV_helmet.png", "", 0.0375/2.5, 2, 2.0, -1) : it\IsHead = True : it\IsCoveringFace = True
	
	; ~ (Vests)
	
	it.ItemTemplates = CreateItemTemplate(GetLocalString("items", "vest"), "Ballistic Vest", "vest", "Wearable\vest.b3d", "INV_vest.png", "", 0.02, 2, 6.0, -1) : it\IsTorso = True
	it.ItemTemplates = CreateItemTemplate(GetLocalString("items", "vest2"), "Bulky Ballistic Vest", "vest3", "Wearable\vest.b3d", "INV_vest.png", "", 0.025, 2, 7.4, -1, "Wearable\Heavy_Vest.png") : it\IsTorso = True
	it.ItemTemplates = CreateItemTemplate(GetLocalString("items", "vest3"), "Heavy Ballistic Vest", "vest2", "Wearable\vest.b3d", "INV_vest (2).png", "", 0.022, 2, 10.2, -1, "Wearable\Fine_Vest.png") : it\IsTorso = True
	it.ItemTemplates = CreateItemTemplate(GetLocalString("items", "vest4"), "Corrosive Ballistic Vest", "vest4", "Wearable\vest.b3d", "INV_vest.png", "", 0.02, 2, 5.4, -1, "Wearable\vest_corrosive.png") : it\IsTorso = True
	
	; ~ (Gas Masks)
	
	it.ItemTemplates = CreateItemTemplate(GetLocalString("items", "mask"), "Gas Mask", "gasmask", "Wearable\gas_mask.b3d", "INV_gas_mask.png", "", 0.019, 2, 2.0, -1) : it\IsHead = True : it\IsCoveringFace = True
	it.ItemTemplates = CreateItemTemplate(GetLocalString("items", "mask"), "Gas Mask", "gasmask2", "Wearable\gas_mask.b3d", "INV_gas_mask.png", "", 0.019, 2, 2.0, -1) : it\IsHead = True : it\IsCoveringFace = True
	it.ItemTemplates = CreateItemTemplate(GetLocalString("items", "mask"), "Gas Mask", "gasmask3", "Wearable\gas_mask.b3d", "INV_gas_mask.png", "", 0.02, 2, 2.0, -1) : it\IsHead = True : it\IsCoveringFace = True
	it.ItemTemplates = CreateItemTemplate(GetLocalString("items", "mask148"), "Heavy Gas Mask", "gasmask4", "Wearable\gas_mask_heavy.b3d", "INV_gas_mask_heavy.png", "", 0.02, 2, 3.4, -1) : it\IsHead = True : it\IsCoveringFace = True
	
	; ~ (Hazmat Suits)
	
	it.ItemTemplates = CreateItemTemplate(GetLocalString("items", "suit"), "Hazmat Suit", "hazmat", "Wearable\hazmat_suit.b3d", "INV_hazmat.png", "", 0.0375/2.5, 2, 8.0, -1, "", "", 1) : it\IsTorso = True : it\IsFullBody = True : it\IsCoveringFace = True
	it.ItemTemplates = CreateItemTemplate(GetLocalString("items", "suit"), "Hazmat Suit", "hazmat2", "Wearable\hazmat_suit.b3d", "INV_hazmat.png", "", 0.0375/2.5, 2, 8.0, -1, "", "", 1) : it\IsTorso = True : it\IsFullBody = True : it\IsCoveringFace = True
	it.ItemTemplates = CreateItemTemplate(GetLocalString("items", "suit"), "Hazmat Suit", "hazmat3", "Wearable\hazmat_suit.b3d", "INV_hazmat.png", "", 0.0375/2.5, 2, 8.0, -1, "", "", 1) : it\IsTorso = True : it\IsFullBody = True : it\IsCoveringFace = True
	it.ItemTemplates = CreateItemTemplate(GetLocalString("items", "suit148"), "Heavy Hazmat Suit", "hazmat4", "Wearable\hazmat_suit.b3d", "INV_hazmat_heavy.png", "", 0.0375/2.5, 2, 11.1, -1, "Wearable\hazmat_suit_heavy.png", "", 1) : it\IsTorso = True : it\IsFullBody = True : it\IsCoveringFace = True
	
	; ~ (Night Vision Goggles)
	
	it.ItemTemplates = CreateItemTemplate(GetLocalString("items", "nvg"), "Night Vision Goggles", "nvg", "Wearable\NVG.b3d", "INV_night_vision_goggles.png", "", 0.02, 2, 1.2, -1, "Wearable\nvg_green.png") : it\IsHead = True
	it.ItemTemplates = CreateItemTemplate(GetLocalString("items", "nvg"), "Night Vision Goggles", "nvg2", "Wearable\NVG.b3d", "INV_night_vision_goggles(2).png", "", 0.02, 2, 1.2, -1, "Wearable\nvg_red.png") : it\IsHead = True
	it.ItemTemplates = CreateItemTemplate(GetLocalString("items", "nvg"), "Night Vision Goggles", "nvg3", "Wearable\NVG.b3d", "INV_night_vision_goggles(3).png", "", 0.02, 2, 1.2, -1, "Wearable\nvg_blue.png") : it\IsHead = True
	
	; ~ (SCRAMBLE Gears)
	
	it.ItemTemplates = CreateItemTemplate(GetLocalString("items", "scramble"), "SCRAMBLE Gear", "scramble", "Wearable\SCRAMBLE_gear.b3d", "INV_SCRAMBLE_gear.png", "", 0.02, 2, 2.2, -1) : it\IsHead = True
	it.ItemTemplates = CreateItemTemplate(GetLocalString("items", "scramble"), "SCRAMBLE Gear", "scramble2", "Wearable\SCRAMBLE_gear.b3d", "INV_SCRAMBLE_gear.png", "", 0.02, 2, 2.2, -1) : it\IsHead = True
	
	; ~ (Backpacks)
	
	it.ItemTemplates = CreateItemTemplate(GetLocalString("items", "backpack"), "Backpack", "backpack", "Wearable\Backpack.b3d", "INV_Backpack.png", "", 0.02, 2, 1.0, -1) : it\IsBackPack = True
	
;! ~ [MISC ITEMS]
	
	; ~ (Keys)
	
	it.ItemTemplates = CreateItemTemplate(GetLocalString("items", "key"), "Lost Key", "key", "Misc\key.b3d", "INV_key.png", "", 0.003, 3, 0.1, -1)
	
	; ~ (Coins)
	
	it.ItemTemplates = CreateItemTemplate(GetLocalString("items", "25ct"), "Quarter", "25ct", "Misc\coin.b3d", "INV_coin.png", "", 0.0005, 3, 0.05, -1)
	it.ItemTemplates = CreateItemTemplate(GetLocalString("items", "coin"), "Coin", "coin", "Misc\coin.b3d", "INV_coin_rusty.png", "", 0.0005, 3, 0.05, -1, "Misc\coin_rusty.png")
	
;! ~ [END]
	
	For it.ItemTemplates = Each ItemTemplates
		If it\Tex <> 0
			If it\TexPath <> ""
				For it2.ItemTemplates = Each ItemTemplates
					If it2 <> it And it2\Tex = it\Tex Then it2\Tex = 0
				Next
			EndIf
			DeleteSingleTextureEntryFromCache(it\Tex) : it\Tex = 0
		EndIf
	Next
End Function

Global SoundTransmission%
Global SoundEmitter%

Global TempSounds%[10]
Global TempSoundIndex% = 0

; ~ Music Constants
;[Block]
Const MaxMusic = 33
; ~ [Zone Music]
Const MUS_PLAYGROUHND = 0
Const MUS_RCZ = 1, MUS_HCZ = 2, MUS_LCZ = 3, MUS_BCZ = 4, MUS_EZ = 5
; ~ [Dimension Music]
Const MUS_DIMESION_106 = 6, MUS_DIMESION_106_TRENCHES = 7, MUS_DIMENSION_1499 = 8
; ~ [Containment Music]
Const MUS_CONT_008 = 9, MUS_CONT_035 = 10, MUS_CONT_049 = 11, MUS_SAVE_ME_FROM = 12, MUS_CONT_079 = 13, MUS_CONT_106 = 14, MUS_CONT_205 = 15, MUS_CONT_409 = 16, MUS_CONT_860 = 17, MUS_CONT_914 = 18
; ~ [Chase Music]
Const MUS_CHASE_049 = 19, MUS_CHASE_106 = 20, MUS_CHASE_860 = 21, MUS_CHASE_1499 = 22
; ~ [Menu Music]
Const MUS_MAIN_MENU = 23, MUS_MENU_BREATH = 24, MUS_MENU_ENDING = 27, MUS_MENU_CREDITS = 26
; ~ [Misc Music]
Const MUS_INTRODUCTION = 27
Const MUS_MT = 28, MUS_STORAGE = 29
Const MUS_SURFACE_DAY = 30, MUS_SURFACE_NIGHT = 31
; ~ [Null]
Const MUS_NULL = 32
;[End Block]

Global Music$[MaxMusic]

; ~ Music Itself
;[Block]
Music[MUS_RCZ] = "Zones\RCZ"
Music[MUS_HCZ] = "Zones\HCZ"
Music[MUS_LCZ] = "Zones\LCZ"
Music[MUS_BCZ] = "Zones\BCZ"
Music[MUS_EZ] = "Zones\EZ"

Music[MUS_CONT_035] = "Containments\Cont_035"
Music[MUS_CONT_049] = "Containments\Cont_049"
Music[MUS_CONT_079] = "Containments\Cont_079"
Music[MUS_CONT_106] = "Containments\Cont_106"
Music[MUS_CONT_205] = "Containments\Cont_205"
Music[MUS_CONT_409] = "Containments\Cont_409"
Music[MUS_CONT_860] = "Containments\Cont_860"
Music[MUS_CONT_914] = "Containments\Cont_914"

Music[MUS_DIMESION_106] = "Dimensions\Dimension_106"
Music[MUS_DIMESION_106_TRENCHES] = "Dimensions\Dimension_106_Trenches"
Music[MUS_DIMENSION_1499] = "Dimensions\Dimension_1499"

Music[MUS_CHASE_049] = "Chase\Chase_049"
Music[MUS_CHASE_106] = "Chase\Chase_106"
Music[MUS_CHASE_860] = "Chase\Chase_860"
Music[MUS_CHASE_1499] = "Chase\Chase_1499"

Music[MUS_MAIN_MENU] = "Menu\Main_Menu"
Music[MUS_MENU_BREATH] = "Menu\Bells_Far"
Music[MUS_MENU_ENDING] = "Menu\Ending"
Music[MUS_MENU_CREDITS] = "Menu\Credits"

Music[MUS_INTRODUCTION] = "Misc\Introduction"
Music[MUS_SAVE_ME_FROM] = "Misc\SaveMeFrom"
Music[MUS_SURFACE_DAY] = "Misc\Surface_Day"
Music[MUS_SURFACE_NIGHT] = "Misc\Surface_Night"
Music[MUS_STORAGE] = "Misc\Storage"
Music[MUS_MT] = "Misc\MT"
;[End Block]

Global MusicCHN%
MusicCHN = StreamSound_Strict("SFX\Music\" + Music[MUS_NULL] + ".ogg", opt\MusicVolume, Mode)

Global NowPlaying% = MUS_NULL, ShouldPlay% = MUS_MAIN_MENU
Global CurrMusic% = True

Dim OpenDoorSFX%(12, 3), CloseDoorSFX%(12, 3)
Global BigDoorErrorSFX%[3]
Global DoorClose079%
Global DoorOpen079%

Global KeyCardSFX1%
Global KeyCardSFX2%
Global ScannerSFX1%
Global ScannerSFX2%

Global DoorBudgeSFX1%
Global DoorBudgeSFX2%
Global DoorLockSFX%

Global OpenDoorFastSFX%
Global CautionSFX%

Global NuclearSirenSFX%

Global CameraSFX%

Global StoneDragSFX%

Global GunshotSFX%
Global Gunshot2SFX%
Global Gunshot3SFX%
Global BulletHitSFX%

Global TeslaIdleSFX%
Global TeslaActivateSFX%
Global TeslaPowerUpSFX%
Global TeslaShockSFX%

Global MagnetUpSFX%, MagnetDownSFX%
Global FemurBreakerSFX%
Global EndBreathCHN%
Global EndBreathSFX%

Global CrouchSFX%

Global DecaySFX%[5]

Global BurstSFX%

Global HissSFX%

Global RustleSFX%[6]

Global Use914SFX%
Global Death914SFX%

Global DripSFX%[4]

Global KnobSFX%[2]

Global LeverSFX%, LightSFX%
Global ButtGhostSFX%

Dim RadioSFX%(2, 9)

Global RadioSquelch%
Global RadioStatic%
Global RadioStatic895%
Global RadioBuzz%

Global SCRAMBLESFX%
Global SCRAMBLECHN%

Global NVGSFX%[2]

Global LowBatterySFX%[2]
Global LowBatteryCHN%[2]

Global ElevatorBeepSFX%, ElevatorMoveSFX%

Global PickSFX%[4]

Global AmbientSFXCHN%, CurrAmbientSFX%
Global AmbientSFXAmount%[6]

Dim AmbientSFX%(6, 16)

Global OldManSFX%[9]

Global Scp173SFX%[3]

Global HorrorSFX%[13]

Global MissSFX%

Global IntroSFX%[12]

Global AlarmSFX%[4]

Global CommotionState%[25]

Global HeartBeatSFX%

Global VomitSFX%

Dim BreathSFX%(2, 5)
Global BreathCHN%

Global BreathGasRelaxedSFX%
Global BreathGasRelaxedCHN%

Global NeckSnapSFX%[3]

Global DamageSFX%[14]

Dim CoughSFX%(2, 3) ; ~ Normal / Gas Mask, Amount
Global CoughCHN%, VomitCHN%

Global MachineSFX%
Global ApacheSFX%

Global DecalStep%
; ~ 0 - Normal
; ~ 1 - Metal
; ~ 2 - PD
; ~ 3 - Cloth
; ~ 4 - Forest (Should be used in future. Currently doesn't work for player)
Dim StepSFX%(5, 2, 8) ; ~ (Ground Type, Walk / Run, ID)
Global Step2SFX%[15]

Global VehicleSFX%[2]

Global ExplosionSFX%

Global RadioCHN%[7]
; ~ 6 is used for radio static only

Global IntercomStreamCHN%

Global UserTrackCheck% = 0, UserTrackCheck2% = 0
Global UserTrackMusicAmount% = 0, CurrUserTrack%, UserTrackFlag% = False
Global UserTrackName$[256]

; ~ NPCs Sound Constants
;[Block]
Const SOUND_NPC_MTF_BEEP% = 0
Const SOUND_NPC_MTF_BREATH% = 1
Const SOUND_NPC_008_1_BREATH% = 2
Const SOUND_NPC_035_TENTACLE_IDLE% = 3
Const SOUND_NPC_049_BREATH% = 4
Const SOUND_NPC_049_2_BREATH% = 5
;[End Block]
Const MaxNPCSounds% = 6
Global NPCSound%[MaxNPCSounds]

Function LoadSounds%()
	Local i%
	
	RenderLoading(45, GetLocalString("loading", "sounds"))
	
	For i = 0 To 2
		OpenDoorSFX(DEFAULT_DOOR, i) = LoadSound_Strict("SFX\Door\DoorOpen" + (i + 1) + ".ogg") ; ~ Also one-sided door
		CloseDoorSFX(DEFAULT_DOOR, i) = LoadSound_Strict("SFX\Door\DoorClose" + (i + 1) + ".ogg") ; ~ Also one-sided door
		OpenDoorSFX(ELEVATOR_DOOR, i) = LoadSound_Strict("SFX\Door\ElevatorOpen" + (i + 1) + ".ogg")
		CloseDoorSFX(ELEVATOR_DOOR, i) = LoadSound_Strict("SFX\Door\ElevatorClose" + (i + 1) + ".ogg")
		OpenDoorSFX(HEAVY_DOOR, i) = LoadSound_Strict("SFX\Door\Door2Open" + (i + 1) + ".ogg")
		CloseDoorSFX(HEAVY_DOOR, i) = LoadSound_Strict("SFX\Door\Door2Close" + (i + 1) + ".ogg")
		OpenDoorSFX(BIG_DOOR, i) = LoadSound_Strict("SFX\Door\BigDoorOpen" + (i + 1) + ".ogg")
		CloseDoorSFX(BIG_DOOR, i) = LoadSound_Strict("SFX\Door\BigDoorClose" + (i + 1) + ".ogg")
		OpenDoorSFX(OFFICE_DOOR, i) = LoadSound_Strict("SFX\Door\OfficeDoorOpen" + (i + 1) + ".ogg")
		OpenDoorSFX(WOODEN_DOOR, i) = LoadSound_Strict("SFX\Door\WoodenDoorOpen" + (i + 1) + ".ogg")
		BigDoorErrorSFX[i] = LoadSound_Strict("SFX\Door\BigDoorError" + (i + 1) + ".ogg")
		OpenDoorSFX(HCZ_DOOR, i) = LoadSound_Strict("SFX\Door\DoorOpenHCZ.ogg")
		CloseDoorSFX(HCZ_DOOR, i) = LoadSound_Strict("SFX\Door\DoorCloseHCZ.ogg")
		OpenDoorSFX(EZ_DOOR, i) = LoadSound_Strict("SFX\Door\DoorOpenEZ.ogg")
		CloseDoorSFX(EZ_DOOR, i) = LoadSound_Strict("SFX\Door\DoorCloseEZ.ogg")
		OpenDoorSFX(RCZ_DOOR, i) = LoadSound_Strict("SFX\Door\DoorOpenRCZ.ogg")
		CloseDoorSFX(RCZ_DOOR, i) = LoadSound_Strict("SFX\Door\DoorCloseRCZ.ogg")
	Next
	DoorOpen079 = LoadSound_Strict("SFX\Door\DoorOpen079.ogg")
	DoorClose079 = LoadSound_Strict("SFX\Door\DoorClose079.ogg")
	
	KeyCardSFX1 = LoadSound_Strict("SFX\Interact\KeyCardUse1.ogg")
	KeyCardSFX2 = LoadSound_Strict("SFX\Interact\KeyCardUse2.ogg")
	ScannerSFX1 = LoadSound_Strict("SFX\Interact\ScannerUse1.ogg")
	ScannerSFX2 = LoadSound_Strict("SFX\Interact\ScannerUse2.ogg")
	
	DoorBudgeSFX1 = LoadSound_Strict("SFX\Interact\DoorBudge1.ogg")
	DoorBudgeSFX2 = LoadSound_Strict("SFX\Interact\DoorBudge2.ogg")
	
	DoorLockSFX = LoadSound_Strict("SFX\Interact\DoorLock.ogg")
	
	OpenDoorFastSFX = LoadSound_Strict("SFX\Door\DoorOpenFast.ogg")
	CautionSFX = LoadSound_Strict("SFX\Room\LockroomSiren.ogg")
	
	CameraSFX = LoadSound_Strict("SFX\General\Camera.ogg") 
	
	StoneDragSFX = LoadSound_Strict("SFX\SCP\173\StoneDrag.ogg")
	
	GunshotSFX = LoadSound_Strict("SFX\General\Gunshot.ogg")
	Gunshot2SFX = LoadSound_Strict("SFX\General\Gunshot2.ogg")
	Gunshot3SFX = LoadSound_Strict("SFX\General\BulletMiss.ogg")
	BulletHitSFX = LoadSound_Strict("SFX\General\BulletHit.ogg")
	
	TeslaIdleSFX = LoadSound_Strict("SFX\Room\Tesla\Idle.ogg")
	TeslaActivateSFX = LoadSound_Strict("SFX\Room\Tesla\WindUp.ogg")
	TeslaPowerUpSFX = LoadSound_Strict("SFX\Room\Tesla\PowerUp.ogg")
	TeslaShockSFX = LoadSound_Strict("SFX\Room\Tesla\Shock.ogg")
	
	MagnetUpSFX = LoadSound_Strict("SFX\Room\106Chamber\MagnetUp.ogg") 
	MagnetDownSFX = LoadSound_Strict("SFX\Room\106Chamber\MagnetDown.ogg")
	
	For i = 0 To 3
		DecaySFX[i] = LoadSound_Strict("SFX\SCP\106\Decay" + i + ".ogg")
	Next
	
	BurstSFX = LoadSound_Strict("SFX\Room\TunnelBurst.ogg")
	
	HissSFX = LoadSound_Strict("SFX\General\Hiss.ogg")
	
	For i = 0 To 5
		RustleSFX[i] = LoadSound_Strict("SFX\SCP\372\Rustle" + i + ".ogg")
	Next
	
	Death914SFX = LoadSound_Strict("SFX\SCP\914\PlayerDeath.ogg") 
	Use914SFX = LoadSound_Strict("SFX\SCP\914\PlayerUse.ogg")
	MachineSFX = LoadSound_Strict("SFX\SCP\914\Refining.ogg")
	
	For i = 0 To 3
		DripSFX[i] = LoadSound_Strict("SFX\Character\D9341\BloodDrip" + i + ".ogg")
	Next
	
	LeverSFX = LoadSound_Strict("SFX\Interact\LeverFlip.ogg") 
	
	LightSFX = LoadSound_Strict("SFX\General\LightSwitch.ogg")
	
	ButtGhostSFX = LoadSound_Strict("SFX\SCP\Joke\789J.ogg")
	
	RadioSFX(0, 0) = LoadSound_Strict("SFX\Radio\RadioAlarm.ogg")
	RadioSFX(0, 1) = LoadSound_Strict("SFX\Radio\RadioAlarm2.ogg")
	For i = 0 To 8
		RadioSFX(1, i) = LoadSound_Strict("SFX\Radio\SCPRadio" + i + ".ogg")
	Next
	RadioSquelch = LoadSound_Strict("SFX\Radio\Squelch.ogg")
	RadioStatic = LoadSound_Strict("SFX\Radio\Static.ogg")
	RadioStatic895 = LoadSound_Strict("SFX\Radio\Static895.ogg")
	RadioBuzz = LoadSound_Strict("SFX\Radio\Buzz.ogg")
	
	ElevatorBeepSFX = LoadSound_Strict("SFX\General\Elevator\Beep.ogg") 
	ElevatorMoveSFX = LoadSound_Strict("SFX\General\Elevator\Moving.ogg") 
	
	For i = 0 To 3
		PickSFX[i] = LoadSound_Strict("SFX\Interact\PickItem" + i + ".ogg")
	Next
	
	; ~ 0 = Light Containment Zone
	; ~ 1 = Heavy Containment Zone
	; ~ 2 = Entrance Zone
	; ~ 3 = General
	; ~ 4 = Pre-Breach
	; ~ 5 = SCP-860-1
	AmbientSFXAmount[0] = 8 
	AmbientSFXAmount[1] = 11
	AmbientSFXAmount[2] = 12
	AmbientSFXAmount[3] = 15 
	AmbientSFXAmount[4] = 5
	AmbientSFXAmount[5] = 10
	
	For i = 0 To 2
		OldManSFX[i] = LoadSound_Strict("SFX\SCP\106\Corrosion" + (i + 1) + ".ogg")
	Next
	OldManSFX[3] = LoadSound_Strict("SFX\SCP\106\Laugh.ogg")
	OldManSFX[4] = LoadSound_Strict("SFX\SCP\106\Breathing.ogg")
	OldManSFX[5] = LoadSound_Strict("SFX\Room\PocketDimension\Enter.ogg")
	For i = 0 To 2
		OldManSFX[i + 6] = LoadSound_Strict("SFX\SCP\106\WallDecay" + (i + 1) + ".ogg")
	Next
	
	For i = 0 To 2
		Scp173SFX[i] = LoadSound_Strict("SFX\SCP\173\Rattle" + (i + 1) + ".ogg")
	Next
	
	For i = 0 To 12
		HorrorSFX[i] = LoadSound_Strict("SFX\Horror\Horror" + i + ".ogg")
	Next
	
	For i = 5 To 7
		IntroSFX[i] = LoadSound_Strict("SFX\Room\Intro\Bang" + (i - 4) + ".ogg")
	Next
	For i = 8 To 10
		IntroSFX[i] = LoadSound_Strict("SFX\Room\Intro\Light" + (i - 7) + ".ogg")
	Next
	IntroSFX[11] = LoadSound_Strict("SFX\Room\Intro\173Vent.ogg")
	
	For i = 0 To 3
		AlarmSFX[i] = LoadSound_Strict("SFX\Alarm\Alarm" + (i + 1) + ".ogg")
	Next
	
	HeartBeatSFX = LoadSound_Strict("SFX\Character\D9341\HeartBeat.ogg")
	
	For i = 0 To 4
		BreathSFX(0, i) = LoadSound_Strict("SFX\Character\D9341\Breath" + i + ".ogg")
		BreathSFX(1, i) = LoadSound_Strict("SFX\Character\D9341\Breath" + i + "Gas.ogg")
	Next
	
	For i = 0 To 2
		NeckSnapSFX[i] = LoadSound_Strict("SFX\SCP\173\NeckSnap" + (i + 1) + ".ogg")
	Next
	
	For i = 0 To 13
		DamageSFX[i] = LoadSound_Strict("SFX\Character\D9341\Damage" + (i + 1) + ".ogg")
	Next
	
	For i = 0 To 2
		CoughSFX(0, i) = LoadSound_Strict("SFX\Character\D9341\Cough" + (i + 1) + ".ogg")
		CoughSFX(1, i) = LoadSound_Strict("SFX\Character\D9341\Cough" + (i + 1) + "Gas.ogg")
	Next
	
	ApacheSFX = LoadSound_Strict("SFX\Character\Apache\Propeller.ogg")
	
	For i = 0 To 7
		StepSFX(0, 0, i) = LoadSound_Strict("SFX\Step\Step" + (i + 1) + ".ogg")
		StepSFX(0, 1, i) = LoadSound_Strict("SFX\Step\Run" + (i + 1) + ".ogg")
		StepSFX(1, 0, i) = LoadSound_Strict("SFX\Step\StepMetal" + (i + 1) + ".ogg")
		StepSFX(1, 1, i) = LoadSound_Strict("SFX\Step\RunMetal" + (i + 1) + ".ogg")
		If i < 3
			StepSFX(2, 0, i) = LoadSound_Strict("SFX\Step\StepPD" + (i + 1) + ".ogg")
			StepSFX(3, 0, i) = LoadSound_Strict("SFX\Step\StepCloth" + (i + 1) + ".ogg")
			StepSFX(4, 0, i) = LoadSound_Strict("SFX\Step\StepForest" + (i + 1) + ".ogg")
		EndIf
	Next
	
	For i = 0 To 2
		Step2SFX[i] = LoadSound_Strict("SFX\Character\MTF\Step" + (i + 1) + ".ogg")
	Next
	For i = 3 To 9
		Step2SFX[i] = LoadSound_Strict("SFX\Step\SCP\StepMetal" + (i - 2) + ".ogg")
	Next
	For i = 10 To 11
		Step2SFX[i] = LoadSound_Strict("SFX\Step\StepFluid" + (i - 9) + ".ogg")
	Next
	For i = 12 To 14
		Step2SFX[i] = LoadSound_Strict("SFX\Step\SCP\StepBarefoot" + (i - 11) + ".ogg")
	Next
	
	VehicleSFX[0] = LoadSound_Strict("SFX\Character\Vehicle\Idle.ogg")
	VehicleSFX[1] = LoadSound_Strict("SFX\Character\Vehicle\Move.ogg")
	
	MissSFX = LoadSound_Strict("SFX\General\Miss.ogg")
	
	BreathGasRelaxedSFX = LoadSound_Strict("SFX\Character\D9341\BreathGasRelaxed.ogg")
	
	CrouchSFX = LoadSound_Strict("SFX\Character\D9341\Crouch.ogg")
	
	SCRAMBLESFX = LoadSound_Strict("SFX\General\SCRAMBLE.ogg")
	
	NVGSFX[0] = LoadSound_Strict("SFX\General\NVGOn.ogg")
	NVGSFX[1] = LoadSound_Strict("SFX\General\NVGOff.ogg")
	
	For i = 0 To 1
		LowBatterySFX[i] = LoadSound_Strict("SFX\General\LowBattery" + (i + 1) + ".ogg")
	Next
	
	For i = 0 To 1
		KnobSFX[i] = LoadSound_Strict("SFX\Room\914Chamber\Knob" + (i + 1) + ".ogg")
	Next
End Function

Function RemoveSoundInstances%()
	Local i%
	
	For i = 0 To 15
		If i < 2
			RadioSFX(0, i) = 0
			VehicleSFX[i] = 0
			NVGSFX[i] = 0
			LowBatterySFX[i] = 0
			KnobSFX[i] = 0
		EndIf
		If i < 3
			OpenDoorSFX(DEFAULT_DOOR, i) = 0
			CloseDoorSFX(DEFAULT_DOOR, i) = 0
			OpenDoorSFX(ELEVATOR_DOOR, i) = 0
			CloseDoorSFX(ELEVATOR_DOOR, i) = 0
			OpenDoorSFX(HEAVY_DOOR, i) = 0
			CloseDoorSFX(HEAVY_DOOR, i) = 0
			OpenDoorSFX(BIG_DOOR, i) = 0
			CloseDoorSFX(BIG_DOOR, i) = 0
			OpenDoorSFX(OFFICE_DOOR, i) = 0
			OpenDoorSFX(WOODEN_DOOR, i) = 0
			OpenDoorSFX(HCZ_DOOR, i) = 0
			CloseDoorSFX(HCZ_DOOR, i) = 0
			OpenDoorSFX(EZ_DOOR, i) = 0
			CloseDoorSFX(EZ_DOOR, i) = 0
			OpenDoorSFX(RCZ_DOOR, i) = 0
			CloseDoorSFX(RCZ_DOOR, i) = 0
			BigDoorErrorSFX[i] = 0
			Scp173SFX[i] = 0
			NeckSnapSFX[i] = 0
			CoughSFX(0, i) = 0
			CoughSFX(1, i) = 0
		EndIf
		If i < 4
			DecaySFX[i] = 0
			PickSFX[i] = 0
			AlarmSFX[i] = 0
			DripSFX[i] = 0
		EndIf
		If i < 5
			BreathSFX(0, i) = 0
			BreathSFX(1, i) = 0
		EndIf
		If i < 6
			AmbientSFXAmount[i] = 0
			RustleSFX[i] = 0
			NPCSound[i] = 0
		EndIf
		If i < 8
			StepSFX(0, 0, i) = 0
			StepSFX(0, 1, i) = 0
			StepSFX(1, 0, i) = 0
			StepSFX(1, 1, i) = 0
			If i < 3
				StepSFX(2, 0, i) = 0
				StepSFX(3, 0, i) = 0
				StepSFX(4, 0, i) = 0
			EndIf
		EndIf
		If i < 9
			RadioSFX(1, i) = 0
			OldManSFX[i] = 0
		EndIf
		If i < 11 Then RoomAmbience[i] = 0
		If i < 12 Then IntroSFX[i] = 0
		If i < 13 Then HorrorSFX[i] = 0
		If i < 14 Then DamageSFX[i] = 0
		If i < 15 Then Step2SFX[i] = 0
	Next
	DoorClose079 = 0
	DoorOpen079 = 0
	
	KeyCardSFX1 = 0
	KeyCardSFX2 = 0
	ScannerSFX1 = 0
	ScannerSFX2 = 0
	
	DoorBudgeSFX1 = 0
	DoorBudgeSFX2 = 0
	
	DoorLockSFX = 0
	
	OpenDoorFastSFX = 0
	CautionSFX = 0
	
	CameraSFX = 0
	
	StoneDragSFX = 0
	
	GunshotSFX = 0
	Gunshot2SFX = 0
	Gunshot3SFX = 0
	BulletHitSFX = 0
	
	TeslaIdleSFX = 0
	TeslaActivateSFX = 0
	TeslaPowerUpSFX = 0
	TeslaShockSFX = 0
	
	MagnetUpSFX = 0
	MagnetDownSFX = 0
	
	BurstSFX = 0
	
	HissSFX = 0
	
	Death914SFX = 0
	Use914SFX = 0
	MachineSFX = 0
	
	LeverSFX = 0
	
	LightSFX = 0
	
	ButtGhostSFX = 0
	
	RadioSquelch = 0
	RadioStatic = 0
	RadioStatic895 = 0
	RadioBuzz = 0
	
	ElevatorBeepSFX = 0
	ElevatorMoveSFX = 0
	
	HeartBeatSFX = 0
	
	ApacheSFX = 0
	
	MissSFX = 0
	
	BreathGasRelaxedSFX = 0
	
	CrouchSFX = 0
	
	SCRAMBLESFX = 0
	
	FemurBreakerSFX = 0
	
	ExplosionSFX = 0
	
	VomitSFX = 0
End Function

Function LoadEvents%()
	
	If gm\ID < GM_SANDBOX
		If opt\IntroEnabled Then CreateEvent(e_cont1_173_intro, r_cont1_173_intro, 0)
		CreateEvent(e_cont1_173, r_cont1_173, 0)
		
		CreateEvent(e_dimension_106, r_dimension_106, 0)
		
		; ~ There's a 7% chance that SCP-106 appears in the rooms named r_room2_5_hcz
		CreateEvent(e_room2_5_hcz_106, r_room2_5_hcz, 0, 0.07 + (0.1 * SelectedDifficulty\AggressiveNPCs))
		
		; ~ The chance for SCP-173 appearing in the first r_room2c_gw_lcz is about 66%
		; ~ There's a 30% chance that it appears in the later r_room2c_gw_lcz
		If Rand(3) < 3 Then CreateEvent(e_173_appearing, r_room2c_gw_lcz, 1)
		CreateEvent(e_173_appearing, r_room2c_gw_lcz, 0, 0.3 + (0.5 * SelectedDifficulty\AggressiveNPCs))
		
		CreateEvent(e_trick, r_room2_lcz, 0, 0.15)
		CreateEvent(e_trick, r_room2_3_lcz, 0, 0.15)
		
		CreateEvent(e_1048_a, r_room2_lcz, 1, 1.0)
		CreateEvent(e_1048_a, r_room2_3_lcz, 1, Rnd(0.8))
		CreateEvent(e_1048_a, r_room2_5_lcz, 1, Rnd(0.6))
		
		CreateEvent(e_room2_storage, r_room2_storage, 0)
		
		; ~ SCP-096 spawns in the first (and last)
		CreateEvent(e_room2c_gw_ez_096, r_room2c_gw_ez, 0)
		
		CreateEvent(e_room1_dead_end_106, r_room1_dead_end_lcz, Rand(0, 1))
		CreateEvent(e_room1_dead_end_106, r_room1_dead_end_ez, Rand(0, 1))
		
		CreateEvent(e_room2_scientists_2, r_room2_scientists_2, 0)
		
		CreateEvent(e_room2_2_lcz, r_room2_2_lcz, 0, 1.0)
		
		CreateEvent(e_room2_elevator, r_room2_elevator, Rand(2))
		
		CreateEvent(e_room3_storage, r_room3_storage, 0)
		
		CreateEvent(e_room2_6_hcz_smoke, r_room2_6_hcz, 0, 0.2)
		CreateEvent(e_room2_6_hcz, r_room2_6_hcz, 0, (0.2 * SelectedDifficulty\AggressiveNPCs))
		
		; ~ SCP-173 appears in half of the r_room2_6_lcz-rooms
		CreateEvent(e_173_appearing, r_room2_6_lcz, 0, 0.5 + (0.4 * SelectedDifficulty\AggressiveNPCs))
		
		; ~ The anomalous duck in r_room2_2_ez-rooms
		CreateEvent(e_room2_2_ez, r_room2_2_ez, 0, 0.7)
		
		CreateEvent(e_room2_closets, r_room2_closets, 0)
		
		CreateEvent(e_room2_cafeteria, r_room2_cafeteria, 0)
		
		CreateEvent(e_room3_hcz_duck, r_room3_hcz, 0)
		CreateEvent(e_room3_hcz_1048,r_room3_hcz, 1)
		
		CreateEvent(e_room2_servers_hcz, r_room2_servers_hcz, 0)
		
		CreateEvent(e_173_appearing, r_room3_2_ez, 0, 0.8)
		CreateEvent(e_room3_2_ez_duck, r_room3_2_ez, 1)
		CreateEvent(e_173_appearing, r_room3_3_ez, 0)
		
		; ~ The dead guard
		CreateEvent(e_room3_2_hcz, r_room3_2_hcz, 0, 0.1)
		
		CreateEvent(e_room4_lcz_049, r_room4_lcz, 0)
		
		If Rand(5) < 5
			Select(Rand(3))
				Case 1
					;[Block]
					CreateEvent(e_682_roar, r_room2_5_hcz, Rand(0, 2))
					;[End Block]
				Case 2
					;[Block]
					CreateEvent(e_682_roar, r_room3_hcz, Rand(0, 2))
					;[End Block]
				Case 3
					;[Block]
					CreateEvent(e_682_roar, r_room2_5_ez, 0)
					;[End Block]
			End Select
		EndIf
		
		CreateEvent(e_room2_nuke, r_room2_nuke, 0)
		
		CreateEvent(e_cont1_895, r_cont1_895, 0)
		
		CreateEvent(e_door_closing, r_room3_lcz, 0, 0.1)
		CreateEvent(e_door_closing, r_room3_2_hcz, 0, 0.1)
		
		If Rand(2) = 1
			CreateEvent(e_106_victim, r_room3_lcz, Rand(2))
			CreateEvent(e_106_sinkhole, r_room3_2_lcz, Rand(2, 3))
		Else
			CreateEvent(e_106_victim, r_room3_2_lcz, Rand(2))
			CreateEvent(e_106_sinkhole, r_room3_lcz, Rand(2, 3))
		EndIf
		CreateEvent(e_106_sinkhole, r_room4_lcz, Rand(2))
		
		CreateEvent(e_cont1_079, r_cont1_079, 0)
		
		CreateEvent(e_cont2_049, r_cont2_049, 0)
		
		CreateEvent(e_cont2_012, r_cont2_012, 0)
		
		CreateEvent(e_cont1_035, r_cont1_035, 0)
		
		CreateEvent(e_cont2_008, r_cont2_008, 0)
		
		CreateEvent(e_cont1_106, r_cont1_106, 0)
		
		CreateEvent(e_cont1_372, r_cont1_372, 0)
		
		CreateEvent(e_cont1_914, r_cont1_914, 0)
		
		CreateEvent(e_butt_ghost, r_room2_6_ez, 0)
		CreateEvent(e_toilet_guard, r_room2_6_ez, 1)
		
		CreateEvent(e_room2_2_hcz_106, r_room2_2_hcz, Rand(0, 3))
		
		CreateEvent(e_173_appearing, r_room2_4_hcz, 0, 0.4 + (0.4 * SelectedDifficulty\AggressiveNPCs))
		
		CreateEvent(e_room2_test_hcz, r_room2_test_hcz, 0)
		CreateEvent(e_room2_test_lcz_173, r_room2_test_lcz, 0)
		
		CreateEvent(e_room2_mt, r_room2_mt, 0)
		
		CreateEvent(e_room2c_ec, r_room2c_ec, 0)
		
		CreateEvent(e_gate_a_entrance, r_gate_a_entrance, 0)
		CreateEvent(e_gate_a, r_gate_a, 0)
		CreateEvent(e_gate_b_entrance, r_gate_b_entrance, 0)
		CreateEvent(e_gate_b, r_gate_b, 0)
		
		CreateEvent(e_cont1_205, r_cont1_205, 0)
		
		CreateEvent(e_cont2_860_1, r_cont2_860_1, 0)
		
		CreateEvent(e_cont3_966, r_cont3_966, 0)
		
		CreateEvent(e_cont2_1123, r_cont2_1123, 0)
		
		CreateEvent(e_room2_tesla, r_room2_tesla_lcz, 0, 1.0)
		CreateEvent(e_room2_tesla, r_room2_tesla_hcz, 0, 1.0)
		CreateEvent(e_room2_tesla, r_room2_tesla_ez, 0, 1.0)
		
		CreateEvent(e_room4_2_hcz, r_room4_2_hcz, 0)
		
		CreateEvent(e_room2_gw_2, r_room2_gw_2, Rand(0, 1))
		CreateEvent(e_gateway, r_room2_gw, 0, 1.0)
		CreateEvent(e_gateway, r_room3_gw, 0, 1.0)
		
		CreateEvent(e_cont2c_066_1162_arc, r_cont2c_066_1162_arc, 0)
		
		CreateEvent(e_cont2_500_1499, r_cont2_500_1499, 0)
		
		CreateEvent(e_room2_sl, r_room2_sl, 0)
		
		CreateEvent(e_room2_medibay, r_room2_medibay, 0)
		
		CreateEvent(e_room2_shaft, r_room2_shaft, 0)
		
		CreateEvent(e_096_spawn, r_room2_3_hcz, 0, 0.4 + (0.2 * SelectedDifficulty\AggressiveNPCs))
		CreateEvent(e_096_spawn, r_room2_4_hcz, 0, 0.5 + (0.2 * SelectedDifficulty\AggressiveNPCs))
		CreateEvent(e_096_spawn, r_room2_5_hcz, 0, 0.6 + (0.2 * SelectedDifficulty\AggressiveNPCs))
		CreateEvent(e_096_spawn, r_room2_6_hcz, 0, 0.4 + (0.2 * SelectedDifficulty\AggressiveNPCs))
		CreateEvent(e_096_spawn, r_room3_hcz, 0, 0.6 + (0.2 * SelectedDifficulty\AggressiveNPCs))
		CreateEvent(e_096_spawn, r_room3_2_hcz, 0, 0.6 + (0.2 * SelectedDifficulty\AggressiveNPCs))
		CreateEvent(e_096_spawn, r_room3_3_hcz, 0, 0.7 + (0.2 * SelectedDifficulty\AggressiveNPCs))
		CreateEvent(e_096_spawn, r_room4_hcz, 0, 0.6 + (0.2 * SelectedDifficulty\AggressiveNPCs))
		CreateEvent(e_096_spawn, r_room4_2_hcz, 0, 0.7 + (0.2 * SelectedDifficulty\AggressiveNPCs))
		
		CreateEvent(e_173_appearing, r_room2_4_lcz, 0, 0.4 + (0.4 * SelectedDifficulty\AggressiveNPCs))
		
		CreateEvent(e_room2_ez_035, r_room2_ez, 0)
		
		CreateEvent(e_room2_4_hcz_106, r_room2_4_hcz, 0, 0.07 + (0.1 * SelectedDifficulty\AggressiveNPCs))
		
		CreateEvent(e_room4_ic, r_room4_ic, 0)
		
		CreateEvent(e_cont2_409, r_cont2_409, 0)
		
		CreateEvent(e_cont1_005, r_cont1_005, 0)
		
		CreateEvent(e_room2_ic, r_room2_ic, 0)
	EndIf
	
	CreateEvent(e_room2_checkpoint, r_room2_checkpoint_lcz_hcz, 0, 1.0)
	CreateEvent(e_room2_checkpoint, r_room2_checkpoint_hcz_ez, 0, 1.0)
	
End Function

Function LoadWayPoints%(LoadingStart% = 55, RenderImage% = True)
	Local d.Doors, w.WayPoints, w2.WayPoints, r.Rooms, ClosestRoom.Rooms
	Local x#, y#, z#
	Local Dist#, Dist2#
	
	For d.Doors = Each Doors
		HideEntity(d\OBJ)
		If d\OBJ2 <> 0 Then HideEntity(d\OBJ2)
		HideEntity(d\FrameOBJ)
		
		If d\room = Null
			ClosestRoom.Rooms = Null
			Dist = 30.0
			For r.Rooms = Each Rooms
				x = Abs(EntityX(r\OBJ, True) - EntityX(d\FrameOBJ, True))
				If x < 20.0
					z = Abs(EntityZ(r\OBJ, True) - EntityZ(d\FrameOBJ, True))
					If z < 20.0
						Dist2 = (x * x) + (z * z)
						If Dist2 < Dist
							ClosestRoom = r
							Dist = Dist2
						EndIf
					EndIf
				EndIf
			Next
		Else
			ClosestRoom = d\room
		EndIf
		If (Not d\DisableWaypoint) And d\DoorType <> WOODEN_DOOR And d\DoorType <> OFFICE_DOOR Then CreateWaypoint(d, ClosestRoom, EntityX(d\FrameOBJ, True), EntityY(d\FrameOBJ, True) + 0.18, EntityZ(d\FrameOBJ, True))
	Next
	
	Local Amount% = 0
	
	For w.WayPoints = Each WayPoints
		EntityRadius(w\OBJ, 0.2)
		EntityPickMode(w\OBJ, 1, True)
		Amount = Amount + 1
	Next
	
	Local Number% = 0
	Local Iter% = 0
	Local i%, n%
	
	For w.WayPoints = Each WayPoints
		Number = Number + 1
		Iter = Iter + 1
		If Iter = 20
			If RenderImage Then RenderLoading(LoadingStart + Floor((30.0 / Amount) * Number), GetLocalString("loading", "waypoints"))
			Iter = 0
		EndIf
		
		w2.WayPoints = After(w)
		
		Local CanCreateWayPoint% = False
		
		While w2 <> Null
			If w\room = w2\room Lor w\door <> Null Lor w2\door <> Null
				Dist = EntityDistance(w\OBJ, w2\OBJ)
				
				If w\room\MaxWayPointY = 0.0 Lor w2\room\MaxWayPointY = 0.0
					CanCreateWayPoint = True
				Else
					If Abs(EntityY(w\OBJ) - EntityY(w2\OBJ)) <= w\room\MaxWayPointY Then CanCreateWayPoint = True
				EndIf
				
				If Dist < 7.0
					If CanCreateWayPoint
						If EntityVisible(w\OBJ, w2\OBJ)
							For i = 0 To 4
								If w\connected[i] = Null
									w\connected[i] = w2.WayPoints
									w\Dist[i] = Dist
									Exit
								EndIf
							Next
							
							For n = 0 To 4
								If w2\connected[n] = Null
									w2\connected[n] = w.WayPoints
									w2\Dist[n] = Dist
									Exit
								EndIf
							Next
						EndIf
					EndIf
				EndIf
			EndIf
			w2 = After(w2)
		Wend
	Next
	
	For d.Doors = Each Doors
		ShowEntity(d\OBJ)
		If d\OBJ2 <> 0 Then ShowEntity(d\OBJ2)
		ShowEntity(d\FrameOBJ)
	Next
	
	For w.WayPoints = Each WayPoints
		EntityRadius(w\OBJ, 0.0)
		EntityPickMode(w\OBJ, 0, False)
		
		If opt\DebugMode
			For i = 0 To 4
				If w\connected[i] <> Null
					Local tLine% = CreateLine(EntityX(w\OBJ, True), EntityY(w\OBJ, True), EntityZ(w\OBJ, True), EntityX(w\connected[i]\OBJ, True), EntityY(w\connected[i]\OBJ, True), EntityZ(w\connected[i]\OBJ, True))
					
					EntityColor(tLine, 255.0, 0.0, 0.0)
					EntityParent(tLine, w\OBJ)
				EndIf
			Next
		EndIf
	Next
End Function

; ~ Textures Constants
;[Block]
Const MaxOverlayTextureIDAmount% = 4
Const MaxOverlayIDAmount% = 11
Const MaxIconIDAmount% = 9
Const MaxImageIDAmount% = 5
;[End Block]

; ~ Icon ID Constants
;[Block]
Const Icon_Walk% = 0, Icon_Sprint% = 1, Icon_Crouch% = 2
Const Icon_Blink% = 3, Icon_Blink_Closed% = 4
Const Icon_Shield% = 5, Icon_SCP_268% = 6, Icon_Inventory% = 7, Icon_Quick_Loading% = 8
;[End Block]

; ~ Image ID Constants
;[Block]
Const Img_Pause_Menu% = 0
Const Img_KeyPad_HUD% = 1
Const Img_SCP_294% = 2
Const Img_NVG_Battery% = 3
Const Img_NAV_Background% = 4
;[End Block]

; ~ Overlay ID Constants
;[Block]
Const Orl_Vignette% = 0
Const Orl_Blood% = 1
Const Orl_GasMask% = 2, Orl_GasMask_Fog% = 3
Const Orl_Hazmat% = 4
Const Orl_NVG% = 5
Const Orl_Helmet% = 6
Const Orl_Dark% = 7
Const Orl_Light% = 8
Const Orl_SCP_008% = 9
Const Orl_SCP_409% = 10
;[End Block]

; ~ Overlay Texture ID Constants
;[Block]
Const Orl_Txt_Vignette% = 0
Const Orl_Txt_NVG% = 1
Const Orl_Txt_Dark% = 2
Const Orl_Txt_Tesla% = 3
;[End Block]

Type Textures
	Field IconID%[MaxIconIDAmount]
	Field ImageID%[MaxImageIDAmount]
	Field OverlayTextureID%[MaxOverlayTextureIDAmount]
	Field OverlayID%[MaxOverlayIDAmount]
End Type

Global t.Textures

; ~ Collisions Constants
;[Block]
Const HIT_MAP% = 1
Const HIT_PLAYER% = 2
Const HIT_ITEM% = 3
Const HIT_APACHE% = 4
Const HIT_DEAD% = 5
;[End Block]

Include "Source Code\Player_Core.bb"

Function LoadEntities%()
	CatchErrors("LoadEntities()")
	
	Local i%, Tex%
	Local b%, t1%, SF%
	Local Name$, Test%, File$
	
	DeInitMainMenuAssets()
	
	RenderLoading(0, GetLocalString("loading", "data"))
	
	LoadData()
	
	InitSubtitlesAssets()
	
	RenderLoading(3, GetLocalString("loading", "player"))
	
	SoundEmitter = CreatePivot()
	
	LoadPlayer()
	
	RenderLoading(5, GetLocalString("loading", "icons"))
	
	t\IconID[Icon_Walk] = LoadImage_Strict("GFX\HUD\walk_icon.png")
	t\IconID[Icon_Walk] = ScaleImage2(t\IconID[Icon_Walk], MenuScale, MenuScale)
	t\IconID[Icon_Sprint] = LoadImage_Strict("GFX\HUD\sprint_icon.png")
	t\IconID[Icon_Sprint] = ScaleImage2(t\IconID[Icon_Sprint], MenuScale, MenuScale)
	t\IconID[Icon_Crouch] = LoadImage_Strict("GFX\HUD\crouch_icon.png")
	t\IconID[Icon_Crouch] = ScaleImage2(t\IconID[Icon_Crouch], MenuScale, MenuScale)
	For i = Icon_Blink To Icon_Blink_Closed
		t\IconID[i] = LoadImage_Strict("GFX\HUD\blink_icon(" + (i - 2) + ").png")
		t\IconID[i] = ScaleImage2(t\IconID[i], MenuScale, MenuScale)
	Next
	t\IconID[Icon_Shield] = LoadImage_Strict("GFX\HUD\shield_icon.png")
	t\IconID[Icon_Shield] = ScaleImage2(t\IconID[Icon_Shield], MenuScale, MenuScale)
	t\IconID[Icon_SCP_268] = LoadImage_Strict("GFX\HUD\scp_268_icon.png")
	t\IconID[Icon_SCP_268] = ScaleImage2(t\IconID[Icon_SCP_268], MenuScale, MenuScale)
	t\IconID[Icon_Inventory] = LoadImage_Strict("GFX\HUD\HUD_Inv_Ryan.png")
	t\IconID[Icon_Inventory] = ScaleImage2(t\IconID[Icon_Inventory], MenuScale, MenuScale) : MidHandle(t\IconID[Icon_Inventory])
	t\IconID[Icon_Quick_Loading] = LoadImage_Strict("GFX\Menu\Images\Quick_Loading.png")
	t\IconID[Icon_Quick_Loading] = ScaleImage2(t\IconID[Icon_Quick_Loading], MenuScale, MenuScale)
	
	For i = 0 To MaxAchievements - 1
		Local Loc$ = "a" + Str(i)
		
		achv\AchievementStrings[i] = GetFileLocalString(AchievementsFile, Loc, "AchvName")
		achv\AchievementDescs[i] = GetFileLocalString(AchievementsFile, Loc, "AchvDesc")
	Next
	
	achv\AchvLocked = LoadImage_Strict("GFX\Achievements\AchvLocked.png")
	achv\AchvLocked = ScaleImage2(achv\AchvLocked, opt\GraphicHeight / 768.0, opt\GraphicHeight / 768.0)
	
	t\ImageID[Img_Pause_Menu] = LoadImage_Strict("GFX\Menu\pause_menu.png")
	t\ImageID[Img_Pause_Menu] = ScaleImage2(t\ImageID[Img_Pause_Menu], MenuScale, MenuScale)
	
	t\ImageID[Img_KeyPad_HUD] = CreateImage(317 * MenuScale, 462 * MenuScale)
	
	t\ImageID[Img_SCP_294] = LoadImage_Strict("GFX\Overlays\scp_294_overlay.png")
	t\ImageID[Img_SCP_294] = ScaleImage2(t\ImageID[Img_SCP_294], MenuScale, MenuScale)
	
	t\ImageID[Img_NVG_Battery] = LoadImage_Strict("GFX\HUD\night_vision_goggles_battery.png")
	t\ImageID[Img_NVG_Battery] = ScaleImage2(t\ImageID[Img_NVG_Battery], MenuScale, MenuScale)
	MaskImage(t\ImageID[Img_NVG_Battery], 255, 0, 255)
	
	t\ImageID[Img_NAV_Background] = CreateImage(opt\GraphicWidth, opt\GraphicHeight)
	
	RenderLoading(10, GetLocalString("loading", "textures"))
	
	LoadMissingTexture()
	
	AmbientLightRoomTex = CreateTextureUsingCacheSystem(2, 2, 0)
	TextureBlend(AmbientLightRoomTex, 2 + (Not opt\Atmosphere))
	SetBuffer(TextureBuffer(AmbientLightRoomTex))
	ClsColor(0, 0, 0)
	Cls()
	SetBuffer(BackBuffer())
	
	CreateBlurImage()
	
	; ~ Overlays
	Local OverlayScale# = GraphicHeightFloat / GraphicWidthFloat
	
	t\OverlayTextureID[Orl_Txt_Vignette] = LoadTexture_Strict("GFX\Overlays\vignette_overlay.png", 1, DeleteAllTextures) ; ~ VIGNETTE
	t\OverlayID[Orl_Vignette] = CreateSprite(ArkBlurCam)
	ScaleSprite(t\OverlayID[Orl_Vignette], 1.0, OverlayScale)
	EntityTexture(t\OverlayID[Orl_Vignette], t\OverlayTextureID[Orl_Txt_Vignette])
	EntityBlend(t\OverlayID[Orl_Vignette], 2)
	EntityOrder(t\OverlayID[Orl_Vignette], -1000)
	MoveEntity(t\OverlayID[Orl_Vignette], 0.0, 0.0, 1.0)
	
	Tex = LoadTexture_Strict("GFX\Overlays\gas_mask_overlay.png", 1, DeleteAllTextures) ; ~ GAS MASK
	t\OverlayID[Orl_GasMask] = CreateSprite(ArkBlurCam)
	ScaleSprite(t\OverlayID[Orl_GasMask], 1.0, OverlayScale)
	EntityTexture(t\OverlayID[Orl_GasMask], Tex)
	EntityBlend(t\OverlayID[Orl_GasMask], 2)
	EntityFX(t\OverlayID[Orl_GasMask], 1)
	EntityOrder(t\OverlayID[Orl_GasMask], -1003)
	MoveEntity(t\OverlayID[Orl_GasMask], 0.0, 0.0, 1.0)
	DeleteSingleTextureEntryFromCache(Tex)
	
	Tex = LoadTexture_Strict("GFX\Overlays\hazmat_suit_overlay.png", 1, DeleteAllTextures) ; ~ HAZMAT SUIT
	t\OverlayID[Orl_Hazmat] = CreateSprite(ArkBlurCam)
	ScaleSprite(t\OverlayID[Orl_Hazmat], 1.0, OverlayScale)
	EntityTexture(t\OverlayID[Orl_Hazmat], Tex)
	EntityBlend(t\OverlayID[Orl_Hazmat], 2)
	EntityFX(t\OverlayID[Orl_Hazmat], 1)
	EntityOrder(t\OverlayID[Orl_Hazmat], -1003)
	MoveEntity(t\OverlayID[Orl_Hazmat], 0, 0, 1.0)
	DeleteSingleTextureEntryFromCache(Tex)
	
	Tex = LoadTexture_Strict("GFX\Overlays\scp_008_overlay.png", 1, DeleteAllTextures) ; ~ SCP-008
	t\OverlayID[Orl_SCP_008] = CreateSprite(ArkBlurCam)
	ScaleSprite(t\OverlayID[Orl_SCP_008], 1.0, OverlayScale)
	EntityTexture(t\OverlayID[Orl_SCP_008], Tex)
	EntityBlend(t\OverlayID[Orl_SCP_008], 3)
	EntityFX(t\OverlayID[Orl_SCP_008], 1)
	EntityOrder(t\OverlayID[Orl_SCP_008], -1003)
	MoveEntity(t\OverlayID[Orl_SCP_008], 0.0, 0.0, 1.0)
	DeleteSingleTextureEntryFromCache(Tex)
	
	t\OverlayTextureID[Orl_Txt_NVG] = LoadTexture_Strict("GFX\Overlays\night_vision_goggles_overlay.png", 1, DeleteAllTextures) ; ~ NIGHT VISION GOGGLES
	t\OverlayID[Orl_NVG] = CreateSprite(ArkBlurCam)
	ScaleSprite(t\OverlayID[Orl_NVG], 1.0, OverlayScale)
	EntityTexture(t\OverlayID[Orl_NVG], t\OverlayTextureID[Orl_Txt_NVG])
	EntityBlend(t\OverlayID[Orl_NVG], 2)
	EntityFX(t\OverlayID[Orl_NVG], 1)
	EntityOrder(t\OverlayID[Orl_NVG], -1003)
	MoveEntity(t\OverlayID[Orl_NVG], 0.0, 0.0, 1.0)
	
	t\OverlayTextureID[Orl_Txt_Dark] = CreateTextureUsingCacheSystem(SMALLEST_POWER_TWO_HALF, SMALLEST_POWER_TWO_HALF, 1 + 2) ; ~ DARK
	SetBuffer(TextureBuffer(t\OverlayTextureID[Orl_Txt_Dark]))
	Cls()
	SetBuffer(BackBuffer())
	t\OverlayID[Orl_Dark] = CreateSprite(ArkBlurCam)
	ScaleSprite(t\OverlayID[Orl_Dark], 1.0, OverlayScale)
	EntityTexture(t\OverlayID[Orl_Dark], t\OverlayTextureID[Orl_Txt_Dark])
	EntityBlend(t\OverlayID[Orl_Dark], 1)
	EntityOrder(t\OverlayID[Orl_Dark], -1002)
	MoveEntity(t\OverlayID[Orl_Dark], 0.0, 0.0, 1.0)
	EntityAlpha(t\OverlayID[Orl_Dark], 0.0)
	
	Tex = CreateTextureUsingCacheSystem(SMALLEST_POWER_TWO_HALF, SMALLEST_POWER_TWO_HALF, 1 + 2) ; ~ LIGHT
	SetBuffer(TextureBuffer(Tex))
	ClsColor(255, 255, 255)
	Cls()
	ClsColor(0, 0, 0)
	SetBuffer(BackBuffer())
	t\OverlayID[Orl_Light] = CreateSprite(ArkBlurCam)
	ScaleSprite(t\OverlayID[Orl_Light], 1.0, OverlayScale)
	EntityTexture(t\OverlayID[Orl_Light], Tex)
	EntityBlend(t\OverlayID[Orl_Light], 1)
	EntityOrder(t\OverlayID[Orl_Light], -1002)
	MoveEntity(t\OverlayID[Orl_Light], 0.0, 0.0, 1.0)
	DeleteSingleTextureEntryFromCache(Tex)
	
	Tex = LoadTexture_Strict("GFX\Overlays\scp_409_overlay.png", 1, DeleteAllTextures) ; ~ SCP-409
	t\OverlayID[Orl_SCP_409] = CreateSprite(ArkBlurCam)
	ScaleSprite(t\OverlayID[Orl_SCP_409], 1.0, OverlayScale)
	EntityTexture(t\OverlayID[Orl_SCP_409], Tex)
	EntityBlend(t\OverlayID[Orl_SCP_409], 3)
	EntityFX(t\OverlayID[Orl_SCP_409], 1)
	EntityOrder(t\OverlayID[Orl_SCP_409], -1003)
	MoveEntity(t\OverlayID[Orl_SCP_409], 0.0, 0.0, 1.0)
	DeleteSingleTextureEntryFromCache(Tex)
	
	Tex = LoadTexture_Strict("GFX\Overlays\helmet_overlay.png", 1, DeleteAllTextures) ; ~ HELMET
	t\OverlayID[Orl_Helmet] = CreateSprite(ArkBlurCam)
	ScaleSprite(t\OverlayID[Orl_Helmet], 1.0, OverlayScale)
	EntityTexture(t\OverlayID[Orl_Helmet], Tex)
	EntityBlend(t\OverlayID[Orl_Helmet], 2)
	EntityFX(t\OverlayID[Orl_Helmet], 1)
	EntityOrder(t\OverlayID[Orl_Helmet], -1003)
	MoveEntity(t\OverlayID[Orl_Helmet], 0.0, 0.0, 1.0)
	DeleteSingleTextureEntryFromCache(Tex)
	
	Tex = LoadTexture_Strict("GFX\Overlays\blood_overlay.png", 1, DeleteAllTextures) ; ~ BLOOD
	t\OverlayID[Orl_Blood] = CreateSprite(ArkBlurCam)
	ScaleSprite(t\OverlayID[Orl_Blood], 1.0, OverlayScale)
	EntityTexture(t\OverlayID[Orl_Blood], Tex)
	EntityBlend(t\OverlayID[Orl_Blood], 2)
	EntityFX(t\OverlayID[Orl_Blood], 1)
	EntityOrder(t\OverlayID[Orl_Blood], -1003)
	MoveEntity(t\OverlayID[Orl_Blood], 0.0, 0.0, 1.0)
	DeleteSingleTextureEntryFromCache(Tex)
	
	Tex = LoadTexture_Strict("GFX\Overlays\fog_gas_mask_overlay.png", 1, DeleteAllTextures) ; ~ FOG IN GAS MASK
	t\OverlayID[Orl_GasMask_Fog] = CreateSprite(ArkBlurCam)
	ScaleSprite(t\OverlayID[Orl_GasMask_Fog], 1.0, OverlayScale)
	EntityTexture(t\OverlayID[Orl_GasMask_Fog], Tex)
	EntityBlend(t\OverlayID[Orl_GasMask_Fog], 3)
	EntityFX(t\OverlayID[Orl_GasMask_Fog], 1)
	EntityOrder(t\OverlayID[Orl_GasMask_Fog], -1002)
	MoveEntity(t\OverlayID[Orl_GasMask_Fog], 0.0, 0.0, 1.0)
	DeleteSingleTextureEntryFromCache(Tex)
	
	For i = Orl_Blood To Orl_SCP_409
		HideEntity(t\OverlayID[i])
	Next
	t\OverlayTextureID[Orl_Txt_Tesla] = LoadTexture_Strict("GFX\Overlays\tesla_overlay.png", 1 + 2, DeleteAllTextures)
	
	MapCubeMap = CreateCubeMap("MapCubeMap")
	
	LoadDecals()
	
	LoadParticles()
	
	LoadMaterials(MaterialsFile)
	
	RenderLoading(13, GetLocalString("loading", "models"))
	
	LoadDoors()
	
	LoadNPCs()
	
	LoadHitBoxes()
	
	LoadPlayerModel()
	
	LoadLevers()
	
	LoadMonitors()
	
	LoadSecurityCams()
	
	LoadElevatorModels()
	
	LoadMisc()
	
	LoadItems()
	
	RenderLoading(15, GetLocalString("loading", "weapons"))
	
	LoadWeapons()
	
	RenderLoading(20, GetLocalString("loading", "tracks"))
	
	UserTrackMusicAmount = 0
	If opt\UserTrackMode > 0
		Local DirPath$ = "SFX\Radio\UserTracks\"
		
		If FileType(DirPath) <> 2 Then CreateDir(DirPath)
		
		Local Dir% = ReadDir(DirPath)
		
		Repeat
			File = NextFile(Dir)
			If File = "" Then Exit
			If FileType(DirPath + File) = 1
				Test = LoadSound(DirPath + File)
				If Test <> 0
					UserTrackName[UserTrackMusicAmount] = File
					UserTrackMusicAmount = UserTrackMusicAmount + 1
				EndIf
				FreeSound(Test) : Test = 0
			EndIf
		Forever
		CloseDir(Dir)
	EndIf
	
	RenderLoading(25, GetLocalString("loading", "graphic"))
	
	AntiAlias(opt\AntiAliasing)
	TextureLodBias(opt\TextureDetailsLevel)
	TextureAnisotropic(opt\AnisotropicLevel)
	
	RenderLoading(30, GetLocalString("loading", "console"))
	
	ClearConsole()
	
	CatchErrors("Uncaught: LoadEntities()")
End Function

Function RemoveTextureInstances%()
	Local i%
	
	For i = 0 To MaxAchievements - 1
		If achv\AchvIMG[i] <> 0 Then FreeImage(achv\AchvIMG[i]) : achv\AchvIMG[i] = 0
	Next
	FreeImage(achv\AchvLocked) : achv\AchvLocked = 0
	
	For i = 0 To MaxIconIDAmount - 1
		FreeImage(t\IconID[i]) : t\IconID[i] = 0
	Next
	For i = 0 To MaxImageIDAmount - 1
		FreeImage(t\ImageID[i]) : t\ImageID[i] = 0
	Next
	For i = 0 To MaxOverlayTextureIDAmount - 1
		t\OverlayTextureID[i] = 0
	Next
	For i = 0 To MaxOverlayIDAmount - 1
		FreeEntity(t\OverlayID[i]) : t\OverlayID[i] = 0
	Next
	Delete Each Achievements
	Delete Each Textures
End Function

Function InitOtherStuff%()
	Local sv.Save, cm.CustomMaps
	
	me\Playable = True : me\SelectedEnding = -1
	
	opt\MasterVolume = opt\PrevMasterVolume
	
	chs\NoClipSpeed = 2.0
	If opt\DebugMode Then InitCheats()
	
	as\Timer = 70.0 * 120.0
	If SelectedDifficulty\SaveType <> SAVE_ANYWHERE Then opt\AutoSaveEnabled = False
End Function

Function InitNewGame%()
	CatchErrors("InitNewGame()")
	
	Local de.Decals, d.Doors, it.Items, r.Rooms, sc.SecurityCams, e.Events, rt.RoomTemplates
	Local i%, Skip%
	
	LoadEntities()
	LoadSounds()
	
	opt\CameraFogFar = 6.0
	
	IsBlackOut = False : PrevIsBlackOut = False
	RemoteDoorOn = True
	
	InitOtherStuff()
	
	Dim Inventory.Items(MaxInventorySpace)
	
	RenderLoading(50, GetLocalString("loading", "stuff"))
	
	me\BlinkTimer = -10.0 : me\BlinkEffect = 1.0 : me\Stamina = 100.0 : me\StaminaEffect = 1.0 : me\HeartBeatRate = 70.0 : me\Funds = Rand(0, 6)
	
	I_005\ChanceToSpawn = Rand(3)
	
	Remove714Timer = 500.0
	RemoveHazmatTimer = 500.0
	
	CODE_DR_MAYNARD = 0
	For i = 0 To 3
		CODE_DR_MAYNARD = CODE_DR_MAYNARD + (Rand(9) * (10 ^ i))
	Next
	If CODE_DR_MAYNARD = CODE_DR_HARP Lor CODE_DR_MAYNARD = CODE_O5_COUNCIL Then CODE_DR_MAYNARD = CODE_DR_MAYNARD + 1
	
	CODE_O5_COUNCIL = ((Int(CODE_DR_MAYNARD) * 2) Mod 10000)
	If CODE_O5_COUNCIL < 1000 Then CODE_O5_COUNCIL = CODE_O5_COUNCIL + 1000
	
	CODE_MAINTENANCE_TUNNELS = ((Int(CODE_DR_MAYNARD) * 3) Mod 10000)
	If CODE_MAINTENANCE_TUNNELS < 1000 Then CODE_MAINTENANCE_TUNNELS = CODE_MAINTENANCE_TUNNELS + 1000
	
	RenderLoading(55, GetLocalString("loading", "rooms"))
	
	If SelectedCustomMap = Null
		CreateMap()
	Else
		LoadMap(CustomMapsPath + SelectedCustomMap\Name)
	EndIf
	
	LoadWayPoints()
	
	n_I\Curr173 = CreateNPC(NPCType173, 0.0, -500.0, 0.0)
	n_I\Curr106 = CreateNPC(NPCType106, 0.0, -500.0, 0.0)
	n_I\Curr106\State = 70.0 * 60.0 * Rnd(12.0, 17.0)
	
	For d.Doors = Each Doors
		EntityParent(d\OBJ, 0)
		If d\DoorType = DEFAULT_DOOR Lor d\DoorType = ONE_SIDED_DOOR Lor d\DoorType = SCP_914_DOOR
			MoveEntity(d\OBJ, 0.0, 0.0, 8.0 * RoomScale)
		ElseIf d\DoorType = OFFICE_DOOR Lor d\DoorType = WOODEN_DOOR
			MoveEntity(d\OBJ, (((d\DoorType = OFFICE_DOOR) * 92.0) + ((d\DoorType = WOODEN_DOOR) * 70.0)) * RoomScale, 0.0, 0.0)
		EndIf
		If d\OBJ2 <> 0
			EntityParent(d\OBJ2, 0)
			If d\DoorType = DEFAULT_DOOR Lor d\DoorType = ONE_SIDED_DOOR Lor d\DoorType = SCP_914_DOOR Then MoveEntity(d\OBJ2, 0.0, 0.0, 8.0 * RoomScale)
		EndIf
		If d\FrameOBJ <> 0 Then EntityParent(d\FrameOBJ, 0)
		For i = 0 To 1
			If d\Buttons[i] <> 0 Then EntityParent(d\Buttons[i], 0)
			If d\ElevatorPanel[i] <> 0 Then EntityParent(d\ElevatorPanel[i], 0)
		Next
	Next
	
	For it.Items = Each Items
		EntityType(it\Collider, HIT_ITEM)
		EntityParent(it\Collider, 0)
	Next
	
	For sc.SecurityCams = Each SecurityCams
		EntityParent(sc\BaseOBJ, 0)
		If sc\MonitorOBJ <> 0 Then EntityParent(sc\MonitorOBJ, 0)
	Next
	
	For r.Rooms = Each Rooms
		If (Not r\RoomTemplate\DisableDecals)
			If Rand(4) = 1
				de.Decals = CreateDecal(Rand(DECAL_BLOOD_1, DECAL_BLOOD_2), r\x + Rnd(-2.0, 2.0), r\y + 0.005, r\z + Rnd(-2.0, 2.0), 90.0, Rnd(360.0), 0.0, Rnd(0.1, 0.4), Rnd(0.85, 0.95))
				EntityParent(de\OBJ, r\OBJ)
			EndIf
			If Rand(4) = 1
				de.Decals = CreateDecal(DECAL_CORROSIVE_1, r\x + Rnd(-2.0, 2.0), r\y + 0.005, r\z + Rnd(-2.0, 2.0), 90.0, Rnd(360.0), 0.0, Rnd(0.5, 0.7), Rnd(0.7, 0.85))
				EntityParent(de\OBJ, r\OBJ)
			EndIf
		EndIf
		
		; ~ Determining player's position depending on zone
		;[Block]
		Select(szl\CurrentZone)
			Case LCZ
				;[Block]
				If r\RoomTemplate\RoomID = r_cont1_173 And (Not opt\IntroEnabled)
					PositionEntity(me\Collider, r\x + 3584.0 * RoomScale, r\y + 704.0 * RoomScale, r\z + 1024.0 * RoomScale)
					PlayerRoom = r
				ElseIf r\RoomTemplate\RoomID = r_cont1_173_intro And opt\IntroEnabled
					PositionEntity(me\Collider, EntityX(r\Objects[5], True), EntityY(r\Objects[5], True), EntityZ(r\Objects[5], True))
					PlayerRoom = r
				EndIf
				;[End Block]
			Case HCZ
				;[Block]
				If r\RoomTemplate\RoomID = r_room2_checkpoint_lcz_hcz
					PositionEntity(me\Collider, r\x, r\y + 80.0 * RoomScale, r\z - 500.0 * RoomScale)
					PlayerRoom = r
				EndIf
				;[End Block]
			Case EZ
				;[Block]
				If r\RoomTemplate\RoomID = r_room2_checkpoint_hcz_ez
					PositionEntity(me\Collider, r\x, r\y + 80.0 * RoomScale, r\z - 500.0 * RoomScale)
					PlayerRoom = r
				EndIf
				;[End Block]
			Case GATE_A_TOPSIDE
				;[Block]
				If r\RoomTemplate\RoomID = r_gate_a
					PositionEntity(me\Collider, r\x, r\y + 80.0 * RoomScale, r\z)
					PlayerRoom = r
				EndIf
				;[End Block]
			Case GATE_B_TOPSIDE
				;[Block]
				If r\RoomTemplate\RoomID = r_gate_b
					PositionEntity(me\Collider, r\x, r\y + 80.0 * RoomScale, r\z)
					PlayerRoom = r
				EndIf
				;[End Block]
		End Select
		;[End Block]
	Next
	
	For rt.RoomTemplates = Each RoomTemplates
		FreeEntity(rt\OBJ) : rt\OBJ = 0
	Next
	
	Local ts.TempScreens, twp.TempWayPoints, tl.TempLights, tp.TempProps, tse.TempSoundEmitters
	
	For ts.TempScreens = Each TempScreens
		Delete(ts)
	Next
	
	For twp.TempWayPoints = Each TempWayPoints
		Delete(twp)
	Next
	
	For tl.TempLights = Each TempLights
		Delete(tl)
	Next
	
	For tp.TempProps = Each TempProps
		Delete(tp)
	Next
	
	For tse.TempSoundEmitters = Each TempSoundEmitters
		Delete(tse)
	Next
	
	RenderLoading(85, GetLocalString("loading", "events"))
	
	If SelectedCustomMap = Null Then LoadEvents()
	
	For e.Events = Each Events
		Select(e\EventID)
			Case e_room2_nuke
				;[Block]
				e\EventState = 1.0
				;[End Block]
			Case e_cont1_106
				;[Block]
				e\EventState2 = 1.0
				;[End Block]
			Case e_room2_sl
				;[Block]
				e\EventState3 = 1.0
				;[End Block]
		End Select
	Next
	
	RenderLoading(90, GetLocalString("loading", "pos"))
	
	TurnEntity(me\Collider, 0.0, Rnd(160.0, 200.0), 0.0)
	
	ResetEntity(me\Collider)
	
	MoveMouse(mo\Viewport_Center_X, mo\Viewport_Center_Y)
	
	SetFontEx(fo\FontID[Font_Default])
	
	HidePointer()
	
	me\DropSpeed = 0.0
	
	UpdateWorld()
	UpdateNPCs()
	UpdateRender()
	
	DeleteTextureEntriesFromCache(DeleteMapTextures)
	
	RenderLoading(100)
	
	fps\Factor[0] = 0.0
	fps\PrevTime = MilliSecs()
	
	ResetInput()
	
	CatchErrors("Uncaught: InitNewGame()")
End Function

Function InitLoadGame%()
	CatchErrors("InitLoadGame()")
	
	Local d.Doors, sc.SecurityCams, rt.RoomTemplates, e.Events
	Local i%, x#, y#, z#
	
	InitOtherStuff()
	LoadWayPoints()
	
	For d.Doors = Each Doors
		EntityParent(d\OBJ, 0)
		If d\OBJ2 <> 0 Then EntityParent(d\OBJ2, 0)
		If d\FrameOBJ <> 0 Then EntityParent(d\FrameOBJ, 0)
		For i = 0 To 1
			If d\Buttons[i] <> 0 Then EntityParent(d\Buttons[i], 0)
			If d\ElevatorPanel[i] <> 0 Then EntityParent(d\ElevatorPanel[i], 0)
		Next
	Next
	
	For sc.SecurityCams = Each SecurityCams
		EntityParent(sc\BaseOBJ, 0)
		If sc\MonitorOBJ <> 0 Then EntityParent(sc\MonitorOBJ, 0)
	Next
	
	For rt.RoomTemplates = Each RoomTemplates
		FreeEntity(rt\OBJ) : rt\OBJ = 0
	Next
	
	Local ts.TempScreens, twp.TempWayPoints, tl.TempLights, tp.TempProps, tse.TempSoundEmitters
	
	For ts.TempScreens = Each TempScreens
		Delete(ts)
	Next
	
	For twp.TempWayPoints = Each TempWayPoints
		Delete(twp)
	Next
	
	For tl.TempLights = Each TempLights
		Delete(tl)
	Next
	
	For tp.TempProps = Each TempProps
		Delete(tp)
	Next
	
	For tse.TempSoundEmitters = Each TempSoundEmitters
		Delete(tse)
	Next
	
	RenderLoading(85, GetLocalString("loading", "events"))
	
	RenderLoading(90, GetLocalString("loading", "pos"))
	
	ResetEntity(me\Collider)
	
	MoveMouse(mo\Viewport_Center_X, mo\Viewport_Center_Y)
	
	SetFontEx(fo\FontID[Font_Default])
	
	HidePointer()
	
	me\DropSpeed = 0.0
	
	UpdateWorld()
	UpdateNPCs()
	UpdateRender()
	
	DeleteTextureEntriesFromCache(DeleteMapTextures)
	
	RenderLoading(100)
	
	fps\Factor[0] = 0.0
	fps\PrevTime = MilliSecs()
	
	ResetInput()
	
	CatchErrors("Uncaught: InitLoadGame()")
End Function

Function NullGame%(PlayButtonSFX% = True)
	CatchErrors("NullGame()")
	
	Local ach.AchievementMsg, c.ConsoleMsg, e.Events, itt.ItemTemplates, it.Items, de.Decals, p.Particles, em.Emitters, d.Doors, lvr.Levers, sc.SecurityCams
	Local n.NPCs, s.Screens, w.WayPoints, pr.Props, l.Lights, rt.RoomTemplates, r.Rooms, m.Materials, snd.Sound, fr.Forest, mt.MTGrid
	Local sv.Save, cm.CustomMaps, se.SoundEmitters, proj.Projectors
	
	Local i%, x%, y%, Lvl%
	
	DeleteTextureEntriesFromCache(DeleteAllTextures)
	
	StopMouseMovement()
	KillSounds(False)
	If PlayButtonSFX Then PlaySound_Strict(ButtonSFX[0])
	
	RandomSeed = ""
	
	UsedConsole = False
	Delete Each Cheats
	WireFrameState = 0
	WireFrame(0)
	ConsoleOpen = False
	ConsoleInput = ""
	ConsoleScroll = 0.0 : ConsoleScrollDragging = 0
	ConsoleMouseMem = 0
	ConsoleR = 0 : ConsoleG = 0 : ConsoleB = 0
	For c.ConsoleMsg = Each ConsoleMsg
		Delete(c)
	Next
	
	Occupation = ""
	InFacility = NullFloor
	PlayerFallingPickDistance = 0.0
	PlayerInsideMTElevator = False
	ToMTElevatorFloor = 0
	
	ShouldEntitiesFall = False
	HideDistance = 0.0
	CoffinDistance = 0.0
	
	SecondaryLightOn = 0.0
	IsBlackOut = False : PrevIsBlackOut = False
	UpdateLightsTimer = 0.0
	
	RemoteDoorOn = False
	
	GameSaved = False
	CanSave = 0
	
	MTFTimer = 0.0
	MTFCameraCheckTimer = 0.0
	MTFCameraCheckDetected = False
	
	CODE_DR_MAYNARD = 0
	CODE_MAINTENANCE_TUNNELS = 0
	CODE_O5_COUNCIL = 0
	
	ShouldPlay = MUS_NULL
	FreeEntity(SoundEmitter) : SoundEmitter = 0
	SoundTransmission = False
	
	TempLightVolume = 0.0
	LightVolume = 0.0
	CurrFogColorR = 0.0 : CurrFogColorG = 0.0 : CurrFogColorB = 0.0
	CurrFogColor = ""
	CurrAmbientColorR = 0.0 : CurrAmbientColorG = 0.0 : CurrAmbientColorB = 0.0
	CurrAmbientColor = ""
	
	GrabbedEntity = 0
	CameraPitch = 0.0
	
	DrawHandIcon = False
	For i = 0 To 3
		DrawArrowIcon[i] = False
	Next
	
	BatMsgTimer = 0.0
	
	EscapeSecondsTimer = 0.0
	EscapeTimer = 0.0
	
	If Camera <> 0 Then FreeEntity(Camera) : Camera = 0
	If Sky <> 0 Then FreeEntity(Sky) : Sky = 0
	
	CurrTrisAmount = 0
	
	CurrAchvMSGID = 0
	For ach.AchievementMsg = Each AchievementMsg
		Delete(ach)
	Next
	
	SubFile = 0
	SubColors = 0
	SubtitlesInit = False
	ClearSubtitles()
	DeInitSubtitlesAssets()
	Delete Each Messages
	Delete Each AutoSave
	
	opt\CameraFogFar = 0.0
	FreeEntity(me\Collider) : me\Collider = 0
	FreeEntity(me\Head) : me\Head = 0
	Delete Each Player
	Delete Each WearableItems
	
	Delete Each SCP005
	Delete Each SCP008
	Delete Each SCP035
	Delete Each SCP268
	Delete Each SCP294
	Delete Each SCP409
	For i = 0 To 1
		I_427\Sound[i] = 0
	Next
	Delete Each SCP427
	Delete Each SCP500
	Delete Each SCP714
	Delete Each SCP1025
	
	Delete Each Weapons
	Delete Each CubeMap
	MapCubeMap = Null
	Delete Each Water
	Delete Each HitBox
	
	QuickLoadPercent = 0
	QuickLoadPercent_DisplayTimer = 0.0
	For e.Events = Each Events
		RemoveEvent(e)
	Next
	
	IsUsingRadio = False
	InvOpen = False
	For i = 0 To 9
		If i < 9
			RadioState[i] = 0.0
			RadioState2[i] = 0
		EndIf
		RadioState3[i] = 0
	Next
	
	ItemAmount = 0 : MaxItemAmount = 0
	LastItemID = 0
	For it.Items = Each Items
		RemoveItem(it)
	Next
	Dim Inventory.Items(0)
	For itt.ItemTemplates = Each ItemTemplates
		RemoveItemTemplate(itt)
	Next
	
	For de.Decals = Each Decals
		RemoveDecal(de)
	Next
	RemoveDecalInstances()
	For p.Particles = Each Particles
		RemoveParticle(p)
	Next
	RemoveParticleInstances()
	For em.Emitters = Each Emitters
		RemoveEmitter(em)
	Next
	Delete Each BrokenDoor
	For d.Doors = Each Doors
		RemoveDoor(d)
	Next
	d_I\AnimButton = 0
	RemoveDoorInstances()
	For lvr.Levers = Each Levers
		RemoveLever(lvr)
	Next
	RemoveLeverInstances()
	For sc.SecurityCams = Each SecurityCams
		RemoveSecurityCam(sc)
	Next
	RemoveSecurityCamInstances()
	RemoveMonitorInstances()
	For s.Screens = Each Screens
		RemoveScreen(s)
	Next
	For w.WayPoints = Each WayPoints
		RemoveWaypoint(w)
	Next
	For pr.Props = Each Props
		RemoveProp(pr)
	Next
	For l.Lights = Each Lights
		RemoveLight(l)
	Next
	Delete Each AlarmRotors
	Delete Each FluLights
	For proj.Projectors = Each Projectors
		RemoveProjector(proj)
	Next
	Delete Each Elevator
	For se.SoundEmitters = Each SoundEmitters
		RemoveSoundEmitter(se)
	Next
	For fr.Forest = Each Forest
		If fr <> Null Then DestroyForest(fr)
		Delete(fr)
	Next
	For mt.MTGrid = Each MTGrid
		If mt <> Null Then DestroyMT(mt, False)
		Delete(mt)
	Next
	Dim MapRoom$(0, 0)
	Delete Each MapGrid
	Delete Each MapZones
	RoomTempID = 0
	For r.Rooms = Each Rooms
		RemoveRoom(r)
	Next
	For rt.RoomTemplates = Each RoomTemplates
		RemoveRoomTemplate(rt)
	Next
	
	DeleteVectors2D()
	DeleteVectors3D()
	
	RemoveHazmatTimer = 0.0
	Remove714Timer = 0.0
	ForestNPC = 0
	ForestNPCTex = 0
	For i = 0 To 2
		ForestNPCData[i] = 0.0
	Next
	For n.NPCs = Each NPCs
		RemoveNPC(n)
	Next
	RemoveNPCInstances()
	
	RemoveMiscInstances()
	
	For m.Materials = Each Materials
		Delete(m)
	Next
	RemoveTextureInstances()
	Delete Each TextureInCache
	AmbientLightRoomTex = 0
	FreeTexture(MissingTexture) : MissingTexture = 0
	
	Mesh_MinX = 0.0 : Mesh_MinY = 0.0 : Mesh_MinZ = 0.0
	Mesh_MaxX = 0.0 : Mesh_MaxY = 0.0 : Mesh_MaxZ = 0.0
	Mesh_MagX = 0.0 : Mesh_MagY = 0.0 : Mesh_MagZ = 0.0
	
	For i = 0 To 24
		CommotionState[i] = False
	Next
	CurrAmbientSFX = 0
	TempSoundIndex = 0
	For snd.Sound = Each Sound
		If snd\InternalHandle <> 0 Then FreeSound(snd\InternalHandle) : snd\InternalHandle = 0
		Delete(snd)
	Next
	RemoveSoundInstances()
	
	For sv.Save = Each Save
		Delete(sv)
	Next
	For cm.CustomMaps = Each CustomMaps
		Delete(cm)
	Next
	
	FreeBlur()
	If FresizeTexture <> 0 Then FreeTexture(FresizeTexture) : FresizeTexture = 0
	If FresizeTexture2 <> 0 Then FreeTexture(FresizeTexture2) : FresizeTexture2 = 0
	If FresizeImage <> 0 Then FreeEntity(FresizeImage) : FresizeImage = 0
	If FresizeCam <> 0 Then FreeEntity(FresizeCam) : FresizeCam = 0
	
	RenderTween = 0.0
	
	ClearTextureFilters()
	ClearCollisions()
	ClearWorld()
	ResetTimingAccumulator()
	InitFastResize()
	
	; ~ Load main menu assets and open main menu
	ShouldDeleteGadgets = True
	DeleteMenuGadgets()
	InitMainMenuAssets()
	MenuOpen = False
	Delete Each InGameMenu
	MainMenuOpen = True
	mm\MainMenuTab = MainMenuTab_Default
	
	CatchErrors("Uncaught: NullGame()")
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D TSS