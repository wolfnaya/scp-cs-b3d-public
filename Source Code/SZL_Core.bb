
;! ~ [ZONE ID CONSTANTS]
;[Block]
Const MaxMapZones% = 22
;[End Block]

; ~ Light Containment Zone
;[Block]
Const LCZ_SL_3% = 1, LCZ_SL_2% = 2, LCZ_SL_1% = 3
;[End Block]
; ~ Heavy Containment Zone
;[Block]
Const HCZ_SL_3% = 4, HCZ_SL_2% = 5, HCZ_SL_1% = 6
;[End Block]
; ~ Entrance Zone
;[Block]
Const EZ_SL_3% = 7, EZ_SL_2% = 8, EZ_SL_1% = 9
;[End Block]
; ~ Reinforced Containment Zone
;[Block]
Const RCZ_SL_3% = 10, RCZ_SL_2% = 11, RCZ_SL_1% = 12
;[End Block]
; ~ Biological Containment Zone
;[Block]
Const BCZ_SL_3% = 13, BCZ_SL_2% = 14, BCZ_SL_1% = 15
;[End Block]
; ~ Surface
;[Block]
Const GATE_A_TOPSIDE% = 16, GATE_B_TOPSIDE% = 17, GATE_C_TOPSIDE% = 18, GATE_D_TOPSIDE% = 19
;[End Block]
; ~ Other
;[Block]
Const REACTOR_CORE% = 20, CLASS_D_CELLS% = 21
;[End Block]

Const LCZ% = 1, HCZ% = 2, EZ% = 3, RCZ% = 4, BCZ% = 5 ; ~ TODO: Get Rid Of These!!! - Wolfnaya

; ~ Main SZL System Type
;[Block]
Type SeparateZoneLoading
	Field CurrentZone%
	Field GlobalZone%
	Field NewZone%
	Field Loading%
	Field CheckpointLoading%
End Type
;[End Block]

Global szl.SeparateZoneLoading = New SeparateZoneLoading

; ~ The main function of the SZL System
;[Block]
Function UpdateZoneLoading%()
	Local e.Events, ZoneName$, i%
	
	If szl\Loading = 1
		DebugLog "----------------------------------------------"
		DebugLog "### --- Started SZL Stage (1) --- ###"
		me\Playable = True
		me\Zombie = False
		me\DropSpeed = 0.0
		For e.Events = Each Events
			If e\room = PlayerRoom Then e\EventState = 0.0
		Next
		DebugLog "----------------------------------------------"
		DebugLog "Applied Important Variables"
		SaveZone(CurrSave\Name)
		If FileType(SavePath + CurrSave\Name + "\zone_" + szl\CurrentZone + ".cb") = 1
			DebugLog "----------------------------------------------"
			DebugLog "Successfully Saved Zone As: (" + CurrSave\Name + "\zone_" + szl\CurrentZone + ".cb" + ")"
		Else
			RuntimeError "Zone Couldn't Be Saved"
		EndIf
		If szl\CheckpointLoading
			QuickLoadPercent = 0
		Else
			RenderLoading(0)
		EndIf
		DeleteZone()
		DebugLog "----------------------------------------------"
		DebugLog "Deleted Old Zone: (" + szl\CurrentZone + ")"
		szl\CurrentZone = szl\NewZone
		DebugLog "----------------------------------------------"
		DebugLog "Set Current Zone To New Zone: (" + szl\CurrentZone + ")"
		szl\Loading = 2
		DebugLog "----------------------------------------------"
		DebugLog "### --- Started SZL Stage (2) --- ###"
	Else
		DebugLog "----------------------------------------------"
		If FileType(SavePath + CurrSave\Name + "\zone_" + szl\CurrentZone + ".cb") <> 1
			DebugLog "Creating New Zone..."
			CreateZone()
			DebugLog "----------------------------------------------"
			DebugLog "Successfully Created New Zone"
		Else
			LoadZone(szl\CurrentZone)
			DebugLog "Loading Into Existing Zone..."
			DebugLog "----------------------------------------------"
			DebugLog "Successfuly Loaded Into Zone"
		EndIf
		If szl\Loading = 4
			DebugLog "----------------------------------------------"
			DebugLog "### --- Started SZL Stage (4) --- ###"
			If szl\CheckpointLoading
				If PlayerRoom\RoomTemplate\RoomID = r_room2_checkpoint_lcz_hcz
					If szl\CurrentZone = HCZ
						PlayerRoom\RoomDoors[0]\Open = True
						PlayerRoom\RoomDoors[1]\Open = False
					Else
						PlayerRoom\RoomDoors[0]\Open = False
						PlayerRoom\RoomDoors[1]\Open = True
					EndIf
				Else
					If szl\CurrentZone = HCZ
						PlayerRoom\RoomDoors[0]\Open = False
						PlayerRoom\RoomDoors[1]\Open = True
					Else
						PlayerRoom\RoomDoors[0]\Open = True
						PlayerRoom\RoomDoors[1]\Open = False
					EndIf
				EndIf
				For e.Events = Each Events
					If e\room = PlayerRoom
						If szl\CurrentZone = LCZ
							ZoneName = "LCZ"
						ElseIf szl\CurrentZone = HCZ
							ZoneName = "HCZ"
						ElseIf szl\CurrentZone = EZ
							ZoneName = "EZ"
						EndIf
						If e\Sound <> 0 Then FreeEntity(e\Sound) : e\Sound = 0
						If e\Sound2 <> 0 Then FreeEntity(e\Sound2) : e\Sound2 = 0
						e\Sound = LoadEventSound(e, "SFX\Door\DoorCheckpoint.ogg")
						e\Sound2 = LoadEventSound(e, "SFX\Room\Checkpoint\Enter_" + ZoneName + ".ogg")
						e\SoundCHN = PlaySound2(e\Sound, Camera, e\room\RoomDoors[1]\OBJ)
						e\SoundCHN2 = PlaySound2(e\Sound2, Camera, e\room\RoomDoors[1]\OBJ)
					EndIf
				Next
				QuickLoadPercent = 100
			Else
				RenderLoading(100)
			EndIf
			szl\Loading = 0
			SeedRnd MilliSecs()
			TeleportEntity(me\Collider, EntityX(me\Collider), EntityY(me\Collider) + (0.1 * fps\Factor[0]), EntityZ(me\Collider))
			If SelectedDifficulty\SaveType <> NO_SAVES And SelectedDifficulty\SaveType <> SAVE_ON_QUIT Then SaveGame(CurrSave\Name)
			DebugLog "----------------------------------------------"
			DebugLog "Successfully Changed Zone! New Zone ID Is: (" + szl\CurrentZone + ")"
			DebugLog "=============================================="
		EndIf
	EndIf
	
End Function
;[End Block]

; ~ Changing the Zone
;[Block]
Function ChangeZone%(ZoneID%, IsCheckpoint% = False)
	
	If szl\Loading = 0
		szl\NewZone = ZoneID
		DebugLog "=============================================="
		DebugLog "Started Zone Change Sequence..."
		DebugLog "----------------------------------------------"
		DebugLog "Determined New Zone As: (" + szl\NewZone + ")"
		DebugLog "----------------------------------------------"
		If IsCheckpoint
			szl\CheckpointLoading = True
			DebugLog "Changing Zone Via Checkpoint"
		Else
			szl\CheckpointLoading = False
			DebugLog "Changing Zone Without Checkpoint"
		EndIf
		szl\Loading = 1
		DebugLog "----------------------------------------------"
		DebugLog "Started UpdateZoneLoading() Function"
	EndIf
	
End Function
;[End Block]

; ~ Creating the Zone
;[Block]
Function CreateZone%()	Local Temp%, Zone%, RoomID%, r.Rooms, r2.Rooms, d.Doors, it.Items, sc.SecurityCams, de.Decals, e.Events, rt.RoomTemplates
	Local i%
	
	If szl\Loading = 2
		
		If szl\CheckpointLoading
			QuickLoadPercent = 10
		Else
			RenderLoading(10)
		EndIf
		
		I_005\ChanceToSpawn = Rand(10)
		
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
		
		If szl\CheckpointLoading
			QuickLoadPercent = 20
		Else
			RenderLoading(20)
		EndIf
		
		szl\Loading = 3
		
		DebugLog "----------------------------------------------"
		DebugLog "### --- Started SZL Stage (3) --- ###"
	Else
		
		If szl\CheckpointLoading
			QuickLoadPercent = 30
		Else
			RenderLoading(30)
		EndIf
		
		CreateMap()
		
		If szl\CheckpointLoading
			QuickLoadPercent = 40
		Else
			RenderLoading(40)
		EndIf
		
		LoadWayPoints(45, (Not szl\CheckpointLoading))
		
		If szl\CheckpointLoading
			QuickLoadPercent = 50
		Else
			RenderLoading(50)
		EndIf
		
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
		
		If szl\CheckpointLoading
			QuickLoadPercent = 60
		Else
			RenderLoading(60)
		EndIf
		
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
		Next
		
		If szl\CheckpointLoading
			QuickLoadPercent = 70
		Else
			RenderLoading(70)
		EndIf
		
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
		
		If szl\CheckpointLoading
			QuickLoadPercent = 80
		Else
			RenderLoading(80)
		EndIf
		
		If SelectedCustomMap = Null Then LoadEvents()
		
		For e.Events = Each Events
			If e\EventID = e_room2_nuke Then e\EventState = 1.0
			If e\EventID = e_cont1_106 Then e\EventState2 = 1.0
			If e\EventID = e_room2_sl Then e\EventState3 = 1.0
		Next
		
		ResetEntity(me\Collider)
		
		MoveMouse(mo\Viewport_Center_X, mo\Viewport_Center_Y)
		
		SetFontEx(fo\FontID[Font_Default])
		
		HidePointer()
		
		me\DropSpeed = 0.0
		
		UpdateWorld()
		UpdateNPCs()
		UpdateRender()
		
		DeleteTextureEntriesFromCache(DeleteMapTextures)
		
		fps\Factor[0] = 0.0
		fps\PrevTime = MilliSecs()
		
		ResetInput()
		
		UpdateRooms()
		UpdateDoors()
		
		If szl\CheckpointLoading
			QuickLoadPercent = 99
		Else
			RenderLoading(99)
		EndIf
		szl\Loading = 4
	EndIf
	
End Function
;[End Block]

; ~ Loading the Zone
;[Block]
Function LoadZone%(ZoneID%)
	Local d.Doors, sc.SecurityCams, r.Rooms, rt.RoomTemplates, e.Events
	Local x%, y%, z%, i%
	
	If szl\Loading = 2
		If szl\CheckpointLoading
			QuickLoadPercent = 10
		Else
			RenderLoading(10)
		EndIf
		LoadSavedZone(CurrSave\Name)
		szl\Loading = 3
		
		DebugLog "----------------------------------------------"
		DebugLog "### --- Started SZL Stage (3) --- ###"
	Else
		If szl\CheckpointLoading
			QuickLoadPercent = 20
		Else
			RenderLoading(20)
		EndIf
		
		LoadWayPoints(25, (Not szl\CheckpointLoading))
		
		If szl\CheckpointLoading
			QuickLoadPercent = 30
		Else
			RenderLoading(30)
		EndIf
		
		For d.Doors = Each Doors
			EntityParent(d\OBJ, 0)
			If d\OBJ2 <> 0 Then EntityParent(d\OBJ2, 0)
			If d\FrameOBJ <> 0 Then EntityParent(d\FrameOBJ, 0)
			For i = 0 To 1
				If d\Buttons[i] <> 0 Then EntityParent(d\Buttons[i], 0)
				If d\ElevatorPanel[i] <> 0 Then EntityParent(d\ElevatorPanel[i], 0)
			Next
		Next
		
		If szl\CheckpointLoading
			QuickLoadPercent = 40
		Else
			RenderLoading(40)
		EndIf
		
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
		
		If szl\CheckpointLoading
			QuickLoadPercent = 50
		Else
			RenderLoading(50)
		EndIf
		
		LoadEvents()
		
		If szl\CheckpointLoading
			QuickLoadPercent = 60
		Else
			RenderLoading(60)
		EndIf
		
		ResetEntity(me\Collider)
		
		MoveMouse(mo\Viewport_Center_X, mo\Viewport_Center_Y)
		
		SetFontEx(fo\FontID[Font_Default])
		
		HidePointer()
		
		me\DropSpeed = 0.0
		
		If szl\CheckpointLoading
			QuickLoadPercent = 70
		Else
			RenderLoading(70)
		EndIf
		
		UpdateWorld()
		UpdateNPCs()
		UpdateRender()
		
		If szl\CheckpointLoading
			QuickLoadPercent = 80
		Else
			RenderLoading(80)
		EndIf
		
		DeleteTextureEntriesFromCache(DeleteMapTextures)
		
		If szl\CheckpointLoading
			QuickLoadPercent = 99
		Else
			RenderLoading(99)
		EndIf
		
		fps\Factor[0] = 0.0
		fps\PrevTime = MilliSecs()
		
		ResetInput()
		
		UpdateDoors()
		
		szl\Loading = 4
	EndIf
	
End Function
;[End Block]

; ~ Deleting the Zone
;[Block]
Function DeleteZone%()
	Local r.Rooms, w.WayPoints, d.Doors, elev.Elevator, em.Emitters, it.Items, n.NPCs, e.Events, sc.SecurityCams, s.Screens, de.Decals, twp.TempWayPoints, pr.Props, rt.RoomTemplates, p.Particles, lvr.Levers, l.Lights, se.SoundEmitters, fr.Forest, mt.MTGrid, ts.TempScreens, tl.TempLights, tp.TempProps, tse.TempSoundEmitters
	Local i%, j%
	
	For d.Doors = Each Doors
		If d\room = PlayerRoom
			For i = 0 To MaxRoomAdjacents - 1
				If PlayerRoom\AdjDoor[i] <> Null
					If PlayerRoom\AdjDoor[i] = d
						RemoveDoor(d)
					EndIf
				EndIf
			Next
		Else
			RemoveDoor(d)
		EndIf
	Next
	
;	For d.Doors = Each Doors
;		If d\room <> PlayerRoom
;			Local DeleteDoor% = True
;			For i = 0 To MaxRoomAdjacents - 1
;				If PlayerRoom\AdjDoor[i] <> Null
;					If PlayerRoom\AdjDoor[i] = d
;						DeleteDoor = False
;						Exit
;					EndIf
;				EndIf
;			Next
;			If DeleteDoor Then RemoveDoor(d)
;		EndIf
;	Next
	For r.Rooms = Each Rooms
		If r <> PlayerRoom Then RemoveRoom(r)
	Next
	For rt.RoomTemplates = Each RoomTemplates
		rt\OBJ = 0
	Next
	DeleteElevator()
	For e.Events = Each Events
		RemoveEvent(e)
	Next
	For it.Items = Each Items
		If (Not IsItemInInventory(it)) Then RemoveItem(it)
	Next
	For de.Decals = Each Decals
		Delete(de)
	Next
	For p.Particles = Each Particles
		Delete(p)
	Next
	For em.Emitters = Each Emitters
		Delete(em)
	Next
	For lvr.Levers = Each Levers
		Delete(lvr)
	Next
	For sc.SecurityCams = Each SecurityCams
		Delete(sc)
	Next
	For s.Screens = Each Screens
		Delete(s)
	Next
	For w.WayPoints = Each WayPoints
		Delete(w)
	Next
	For pr.Props = Each Props
		Delete(pr)
	Next
	For l.Lights = Each Lights
		Delete(l)
	Next
	For se.SoundEmitters = Each SoundEmitters
		Delete(se)
	Next
	For fr.Forest = Each Forest
		Delete(fr)
	Next
	For mt.MTGrid = Each MTGrid
		Delete(mt)
	Next
	For n.NPCs = Each NPCs
		RemoveNPC(n)
	Next
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
	
End Function
;[End Block]

; ~ Saving the Zone
;[Block]
Function SaveZone%(File$)
	Local n.NPCs, r.Rooms, do.Doors
	Local x%, y%, i%, Temp%, it.Items
	
	File = SavePath + File
	
	If FileType(File) <> 2 Then CreateDir(File)
	
	Local f% = WriteFile(File + "\zone_" + szl\CurrentZone + ".cb")
	
	For x = 0 To MapGridSize
		For y = 0 To MapGridSize
			WriteByte(f, CurrMapGrid\Grid[x + (y * MapGridSize)])
			WriteByte(f, CurrMapGrid\Found[x + (y * MapGridSize)])
		Next
	Next
	
	WriteInt(f, 113)
	
	Temp = 0
	For n.NPCs = Each NPCs
		Temp = Temp + 1
	Next
	
	WriteInt(f, Temp)
	For n.NPCs = Each NPCs
		WriteByte(f, n\NPCType)
		WriteFloat(f, EntityX(n\Collider, True))
		WriteFloat(f, EntityY(n\Collider, True))
		WriteFloat(f, EntityZ(n\Collider, True))
		
		WriteFloat(f, EntityPitch(n\Collider))
		WriteFloat(f, EntityYaw(n\Collider))
		WriteFloat(f, EntityRoll(n\Collider))
		
		WriteFloat(f, n\State)
		WriteFloat(f, n\State2)
		WriteFloat(f, n\State3)
		WriteInt(f, n\PrevState)
		
		WriteByte(f, n\Idle)
		WriteFloat(f, n\LastDist)
		WriteInt(f, n\LastSeen)
		
		WriteFloat(f, n\CurrSpeed)
		
		WriteFloat(f, n\Angle)
		
		WriteFloat(f, n\Reload)
		
		WriteInt(f, n\ID)
		If n\Target <> Null
			WriteInt(f, n\Target\ID)
		Else
			WriteInt(f, 0)
		EndIf
		
		WriteFloat(f, n\EnemyX)
		WriteFloat(f, n\EnemyY)
		WriteFloat(f, n\EnemyZ)
		
		WriteString(f, n\Texture)
		
		WriteFloat(f, AnimTime(n\OBJ))
		
		WriteByte(f, n\Contained)
		WriteFloat(f, n\PathX)
		WriteFloat(f, n\PathZ)
		WriteInt(f, n\HP)
		WriteString(f, n\Model)
		WriteFloat(f, n\ModelScaleX)
		WriteFloat(f, n\ModelScaleY)
		WriteFloat(f, n\ModelScaleZ)
		WriteByte(f, n\HasAsset)
		WriteByte(f, n\TextureID)
		WriteByte(f, n\HideFromNVG)
	Next
	
	WriteInt(f, 632)
	
	WriteByte(f, bk\IsBroken)
	WriteFloat(f, bk\x)
	WriteFloat(f, bk\z)
	
	WriteByte(f, I_Zone\Transition[0])
	WriteByte(f, I_Zone\Transition[1])
	WriteByte(f, I_Zone\HasCustomForest)
	WriteByte(f, I_Zone\HasCustomMT)
	
	Temp = 0
	For r.Rooms = Each Rooms
		Temp = Temp + 1
	Next
	WriteInt(f, Temp)
	For r.Rooms = Each Rooms
		WriteInt(f, r\RoomTemplate\ID)
		WriteInt(f, r\Angle)
		WriteFloat(f, r\x)
		WriteFloat(f, r\y)
		WriteFloat(f, r\z)
		
		WriteByte(f, r\Found)
		WriteInt(f, r\Zone)
		
		If PlayerRoom = r
			WriteByte(f, 1)
		Else
			WriteByte(f, 0)
		EndIf
		
		For i = 0 To MaxRoomNPCs - 1
			If r\NPC[i] = Null
				WriteInt(f, 0)
			Else
				WriteInt(f, r\NPC[i]\ID)
			EndIf
		Next
		
		For i = 0 To MaxRoomLevers - 1
			If r\RoomLevers[i] = Null
				WriteByte(f, 2)
			Else
				If EntityPitch(r\RoomLevers[i]\OBJ, True) > 0.0
					WriteByte(f, 0)
				Else
					WriteByte(f, 1)
				EndIf
			EndIf
		Next
		
		If r\mt = Null ; ~ This room doesn't have a grid
			WriteByte(f, 0)
		Else ; ~ This room has a grid
			WriteByte(f, 1)
			For y = 0 To MTGridSize - 1
				For x = 0 To MTGridSize - 1
					WriteByte(f, r\mt\Grid[x + (y * MTGridSize)])
					WriteByte(f, r\mt\Angles[x + (y * MTGridSize)])
				Next
			Next
		EndIf
		
		If r\fr = Null ; ~ This room doesn't have a forest
			WriteByte(f, 0)
		Else ; ~ This room has a forest
			If (Not I_Zone\HasCustomForest)
				WriteByte(f, 1)
			Else
				WriteByte(f, 2)
			EndIf
			For y = 0 To ForestGridSize - 1
				For x = 0 To ForestGridSize - 1
					WriteByte(f, r\fr\Grid[x + (y * ForestGridSize)])
				Next
			Next
			WriteFloat(f, EntityX(r\fr\Forest_Pivot, True))
			WriteFloat(f, EntityY(r\fr\Forest_Pivot, True))
			WriteFloat(f, EntityZ(r\fr\Forest_Pivot, True))
		EndIf
	Next
	
	WriteInt(f, 954)
	
	Temp = 0
	For do.Doors = Each Doors
		Temp = Temp + 1
	Next
	WriteInt(f, Temp)
	For do.Doors = Each Doors
		WriteFloat(f, EntityX(do\FrameOBJ, True))
		WriteFloat(f, EntityY(do\FrameOBJ, True))
		WriteFloat(f, EntityZ(do\FrameOBJ, True))
		WriteByte(f, do\Open)
		WriteFloat(f, do\OpenState)
		WriteByte(f, do\Locked)
		WriteByte(f, do\AutoClose)
		
		WriteFloat(f, EntityX(do\OBJ, True))
		WriteFloat(f, EntityZ(do\OBJ, True))
		WriteFloat(f, EntityYaw(do\OBJ, True))
		
		If do\OBJ2 <> 0
			WriteFloat(f, EntityX(do\OBJ2, True))
			WriteFloat(f, EntityZ(do\OBJ2, True))
		Else
			WriteFloat(f, 0.0)
			WriteFloat(f, 0.0)
		EndIf
		
		WriteFloat(f, do\Timer)
		WriteFloat(f, do\TimerState)
		
		WriteByte(f, do\IsElevatorDoor)
		WriteByte(f, do\MTFClose)
	Next
	
	WriteInt(f, 1845)
	
	Local de.Decals
	
	Temp = 0
	For de.Decals = Each Decals
		Temp = Temp + 1
	Next
	WriteInt(f, Temp)
	For de.Decals = Each Decals
		WriteInt(f, de\ID)
		
		WriteFloat(f, EntityX(de\OBJ, True))
		WriteFloat(f, EntityY(de\OBJ, True))
		WriteFloat(f, EntityZ(de\OBJ, True))
		
		WriteFloat(f, EntityPitch(de\OBJ, True))
		WriteFloat(f, EntityYaw(de\OBJ, True))
		WriteFloat(f, EntityRoll(de\OBJ, True))
		
		WriteFloat(f, de\Size)
		WriteFloat(f, de\Alpha)
		WriteByte(f, de\FX)
		WriteByte(f, de\BlendMode)
		WriteByte(f, de\R) : WriteByte(f, de\G) : WriteByte(f, de\B)
		
		WriteFloat(f, de\Timer)
		WriteFloat(f, de\LifeTime)
		WriteFloat(f, de\SizeChange)
		WriteFloat(f, de\AlphaChange)
	Next
	
	Local e.Events
	
	Temp = 0
	For e.Events = Each Events
		Temp = Temp + 1
	Next
	WriteInt(f, Temp)
	For e.Events = Each Events
		WriteByte(f, e\EventID)
		WriteFloat(f, e\EventState)
		WriteFloat(f, e\EventState2)
		WriteFloat(f, e\EventState3)
		WriteFloat(f, e\EventState4)
		WriteFloat(f, EntityX(e\room\OBJ))
		WriteFloat(f, EntityZ(e\room\OBJ))
		WriteString(f, e\EventStr)
	Next
	
	For it = Each Items
		If (Not IsItemInInventory(it))
			WriteByte f, 1
			WriteString f, it\ItemTemplate\Name
			WriteString f, it\ItemTemplate\TempName
			WriteString f, it\Name
			WriteFloat f, EntityX(it\Collider, True)
			WriteFloat f, EntityY(it\Collider, True)
			WriteFloat f, EntityZ(it\Collider, True)
			WriteByte f, it\R
			WriteByte f, it\G
			WriteByte f, it\B
			WriteFloat f, it\Alpha
			WriteFloat f, EntityPitch(it\Collider)
			WriteFloat f, EntityYaw(it\Collider)
			WriteFloat f, it\State
			WriteFloat f, it\State2
			WriteFloat f, it\State3
			If it\ItemTemplate\IsAnim <> 0 Then WriteFloat f, AnimTime(it\Model)
			WriteByte f,it\InvSlots
			WriteInt f,it\ID
			If it\ItemTemplate\InvImg = it\InvImg
				WriteByte f, 0
			Else
				WriteByte f, 1
			EndIf
		EndIf
	Next
	WriteByte f, 0
	
	For it = Each Items
		If (Not IsItemInInventory(it)) And it\InvSlots > 0
			For i = 0 To it\InvSlots - 1
				If it\SecondInv[i] <> Null
					WriteInt f, it\SecondInv[i]\ID
				Else
					WriteInt f, -1
				EndIf
			Next
		EndIf
	Next
	
	CloseFile(f)
	
End Function
;[End Block]

; ~ Loading Saved Zone
;[Block]
Function LoadSavedZone%(File$)
	Local r.Rooms, n.NPCs, do.Doors, rt.RoomTemplates
	Local X#, Y#, Z#, i%, j%, Temp%, StrTemp$, Tex%, ID%
	Local it.Items, it2.Items, itt.ItemTemplates
	
	File = SavePath + File
	
	Local f% = ReadFile_Strict(File + "\zone_" + szl\CurrentZone + ".cb")
	
	CurrMapGrid.MapGrid = New MapGrid
	For X = 0 To MapGridSize
		For Y = 0 To MapGridSize
			CurrMapGrid\Grid[X + (Y * MapGridSize)] = ReadByte(f)
			CurrMapGrid\Found[X + (Y * MapGridSize)] = ReadByte(f)
		Next
	Next
	
	If ReadInt(f) <> 113 Then RuntimeError(GetLocalString("save", "corrupted_1"))
	
	For n.NPCs = Each NPCs
		RemoveNPC(n)
	Next
	
	Temp = ReadInt(f)
	For i = 1 To Temp
		Local NPCType% = ReadByte(f)
		
		X = ReadFloat(f)
		Y = ReadFloat(f)
		Z = ReadFloat(f)
		
		n.NPCs = CreateNPC(NPCType, X, Y, Z)
		Select(NPCType)
			Case NPCType173
				;[Block]
				n_I\Curr173 = n
				;[End Block]
			Case NPCType106
				;[Block]
				n_I\Curr106 = n
				;[End Block]
			Case NPCType096
				;[Block]
				n_I\Curr096 = n
				;[End Block]
			Case NPCType513_1
				;[Block]
				n_I\Curr513_1 = n
				;[End Block]
			Case NPCType049
				;[Block]
				n_I\Curr049 = n
				;[End Block]
		End Select
		
		X = ReadFloat(f)
		Y = ReadFloat(f)
		Z = ReadFloat(f)
		RotateEntity(n\Collider, X, Y, Z)
		
		n\State = ReadFloat(f)
		n\State2 = ReadFloat(f)
		n\State3 = ReadFloat(f)
		n\PrevState = ReadInt(f)
		
		n\Idle = ReadByte(f)
		n\LastDist = ReadFloat(f)
		n\LastSeen = ReadInt(f)
		
		n\CurrSpeed = ReadFloat(f)
		n\Angle = ReadFloat(f)
		n\Reload = ReadFloat(f)
		
		ForceSetNPCID(n, ReadInt(f))
		n\TargetID = ReadInt(f)
		
		n\EnemyX = ReadFloat(f)
		n\EnemyY = ReadFloat(f)
		n\EnemyZ = ReadFloat(f)
		
		n\Texture = ReadString(f)
		If n\Texture <> ""
			Tex = LoadTexture_Strict(n\Texture)
			If opt\Atmosphere Then TextureBlend(Tex, 5)
			EntityTexture(n\OBJ, Tex)
			DeleteSingleTextureEntryFromCache(Tex)
		EndIf
		
		Local Frame# = ReadFloat(f)
		
		Select(NPCType)
			Case NPCType106, NPCTypeD, NPCType096, NPCTypeMTF, NPCTypeGuard, NPCType049, NPCType049_2, NPCTypeClerk, NPCType008_1
				;[Block]
				SetAnimTime(n\OBJ, Frame)
				;[End Block]
		End Select
		
		n\Frame = Frame
		n\Contained = ReadByte(f)
		n\PathX = ReadFloat(f)
		n\PathZ = ReadFloat(f)
		n\HP = ReadInt(f)
		n\Model = ReadString(f)
		n\ModelScaleX = ReadFloat(f)
		n\ModelScaleY = ReadFloat(f)
		n\ModelScaleZ = ReadFloat(f)
		If n\Model <> ""
			FreeEntity(n\OBJ) : n\OBJ = 0
			n\OBJ = LoadAnimMesh_Strict(n\Model)
			ScaleEntity(n\OBJ, n\ModelScaleX, n\ModelScaleY, n\ModelScaleZ)
			SetAnimTime(n\OBJ, Frame)
		EndIf
		n\HasAsset = ReadByte(f)
		If n\HasAsset Then CreateNPCAsset(n)
		n\TextureID = ReadByte(f)
		If n\TextureID > 0
			ChangeNPCTextureID(n, n\TextureID - 1)
			SetAnimTime(n\OBJ, Frame)
		EndIf
		n\HideFromNVG = ReadByte(f)
		
		If n\TargetID <> 0
			Local n2.NPCs
			
			For n2.NPCs = Each NPCs
				If n2 <> n
					If n2\ID = n\TargetID Then n\Target = n2
				EndIf
			Next
		EndIf
	Next
	
	For n.NPCs = Each NPCs
		If n\NPCType = NPCTypeMTF
			If n_I\MTFLeader = Null Then n_I\MTFLeader = n
		EndIf
	Next
	
	If ReadInt(f) <> 632 Then RuntimeError(GetLocalString("save", "corrupted_2"))
	
	bk\IsBroken = ReadByte(f)
	bk\x = ReadFloat(f)
	bk\z = ReadFloat(f)
	
	I_Zone\Transition[0] = ReadByte(f)
	I_Zone\Transition[1] = ReadByte(f)
	I_Zone\HasCustomForest = ReadByte(f)
	I_Zone\HasCustomMT = ReadByte(f)
	
	Temp = ReadInt(f)
	For i = 1 To Temp
		Local RoomTemplateID% = ReadInt(f)
		Local Angle% = ReadInt(f)
		
		X = ReadFloat(f)
		Y = ReadFloat(f)
		Z = ReadFloat(f)
		
		Local Found% = ReadByte(f)
		Local Level% = ReadInt(f)
		
		Local Temp2% = ReadByte(f)
		
		Angle = WrapAngle(Angle)
		
		For rt.RoomTemplates = Each RoomTemplates
			If rt\ID = RoomTemplateID
				r.Rooms = CreateRoom(Level, rt\Shape, X, Y, Z, rt\RoomID, Angle)
				;SetupTriggerBoxes(r)
				r\Found = Found
				Exit
			EndIf
		Next
		
		If Temp2 = 1 Then PlayerRoom = r
		
		For X = 0 To MaxRoomNPCs - 1
			ID = ReadInt(f)
			If ID > 0
				For n.NPCs = Each NPCs
					If n\ID = ID
						r\NPC[X] = n
						Exit
					EndIf
				Next
			EndIf
		Next
		
		For X = 0 To MaxRoomLevers - 1
			ID = ReadByte(f)
			If ID = 0
				RotateEntity(r\RoomLevers[X]\OBJ, 80.0, EntityYaw(r\RoomLevers[X]\OBJ), 0.0)
			ElseIf ID = 1
				RotateEntity(r\RoomLevers[X]\OBJ, -80.0, EntityYaw(r\RoomLevers[X]\OBJ), 0.0)
			EndIf
		Next
		
		If ReadByte(f) = 1 ; ~ This room has a grid
			If r\mt <> Null ; ~ Remove the old grid content
				DestroyMT(r\mt)
				Delete(r\mt)
			EndIf
			r\mt.MTGrid = New MTGrid
			
			For Y = 0 To MTGridSize - 1
				For X = 0 To MTGridSize - 1
					r\mt\Grid[X + (Y * MTGridSize)] = ReadByte(f)
					r\mt\Angles[X + (Y * MTGridSize)] = ReadByte(f)
					; ~ Get only the necessary data, make the event handle the meshes and waypoints separately
				Next
			Next
		EndIf
		
		Local HasForest% = ReadByte(f)
		
		If HasForest > 0 ; ~ This room has a forest
			If r\fr <> Null ; ~ Remove the old forest
				DestroyForest(r\fr)
			Else
				r\fr.Forest = New Forest
			EndIf
			For Y = 0 To ForestGridSize - 1
				For X = 0 To ForestGridSize - 1
					r\fr\Grid[X + (Y * ForestGridSize)] = ReadByte(f)
				Next
			Next
			
			Local lX# = ReadFloat(f)
			Local lY# = ReadFloat(f)
			Local lZ# = ReadFloat(f)
			
			If HasForest = 1
				PlaceForest(r\fr, lX, lY, lZ, r)
			Else
				PlaceMapCreatorForest(r\fr, lX, lY, lZ, r)
			EndIf
		ElseIf r\fr <> Null ; ~ Remove the old forest
			DestroyForest(r\fr)
			Delete(r\fr)
		EndIf
	Next
	
	If ReadInt(f) <> 954 Then RuntimeError(GetLocalString("save", "corrupted_3"))
	
	Local ShouldSpawnDoor%, DoorID%;, Zone%
	
	For Y = MapGridSize To 0 Step -1
;		If Y < I_Zone\Transition[1] - (SelectedCustomMap = Null)
;			Zone = EZ
;		ElseIf Y >= I_Zone\Transition[1] - (SelectedCustomMap = Null) And Y < I_Zone\Transition[0] - (SelectedCustomMap = Null)
;			Zone = HCZ
;		Else
;			Zone = LCZ
;		EndIf
		For X = MapGridSize To 0 Step -1
			If CurrMapGrid\Grid[X + (Y * MapGridSize)] > MapGrid_NoTile
				For r.Rooms = Each Rooms
					r\Angle = WrapAngle(r\Angle)
					If Int(r\x / RoomSpacing) = X And Int(r\z / RoomSpacing) = Y
						Select(r\RoomTemplate\Shape)
							Case ROOM1
								;[Block]
								ShouldSpawnDoor = (r\Angle = 90.0)
								;[End Block]
							Case ROOM2
								;[Block]
								ShouldSpawnDoor = (r\Angle = 90.0 Lor r\Angle = 270.0)
								;[End Block]
							Case ROOM2C
								;[Block]
								ShouldSpawnDoor = (r\Angle = 0.0 Lor r\Angle = 90.0)
								;[End Block]
							Case ROOM3
								;[Block]
								ShouldSpawnDoor = (r\Angle = 0.0 Lor r\Angle = 180.0 Lor r\Angle = 90.0)
								;[End Block]
							Default
								;[Block]
								ShouldSpawnDoor = True
								;[End Block]
						End Select
						
						;[Block]
						If szl\CurrentZone = LCZ Then DoorID = LCZ_DOOR ElseIf szl\CurrentZone = HCZ Then DoorID = HCZ_DOOR ElseIf szl\CurrentZone = EZ Then DoorID = EZ_DOOR ElseIf szl\CurrentZone = RCZ Then DoorID = RCZ_DOOR ElseIf szl\CurrentZone = BCZ Then DoorID = ONE_SIDED_DOOR
						;[End Block]
						
						If ShouldSpawnDoor
							If X + 1 < MapGridSize + 1
								If CurrMapGrid\Grid[(X + 1) + (Y * MapGridSize)] > MapGrid_NoTile
									do.Doors = CreateDoor(r, Float(X) * RoomSpacing + (RoomSpacing / 2.0), 0.0, Float(Y) * RoomSpacing, 90.0, Max(Rand(-3, 1), 0.0), DoorID)
									r\AdjDoor[0] = do
								EndIf
							EndIf
						EndIf
						
						Select(r\RoomTemplate\Shape)
							Case ROOM1
								;[Block]
								ShouldSpawnDoor = (r\Angle = 180.0)
								;[End Block]
							Case ROOM2
								;[Block]
								ShouldSpawnDoor = (r\Angle = 0.0 Lor r\Angle = 180.0)
								;[End Block]
							Case ROOM2C
								;[Block]
								ShouldSpawnDoor = (r\Angle = 180.0 Lor r\Angle = 90.0)
								;[End Block]
							Case ROOM3
								;[Block]
								ShouldSpawnDoor = (r\Angle = 180.0 Lor r\Angle = 90.0 Lor r\Angle = 270.0)
								;[End Block]
							Default
								;[Block]
								ShouldSpawnDoor = True
								;[End Block]
						End Select
						If ShouldSpawnDoor
							If Y + 1 < MapGridSize + 1
								If CurrMapGrid\Grid[X + ((Y + 1) * MapGridSize)] > MapGrid_NoTile
									do.Doors = CreateDoor(r, Float(X) * RoomSpacing, 0.0, Float(Y) * RoomSpacing + (RoomSpacing / 2.0), 0.0, Max(Rand(-3, 1), 0.0), DoorID)
									r\AdjDoor[3] = do
								EndIf
							EndIf
						EndIf
						Exit
					EndIf
				Next
			EndIf
		Next
	Next
	
	Temp = ReadInt(f)
	
	For i = 1 To Temp
		X = ReadFloat(f)
		Y = ReadFloat(f)
		Z = ReadFloat(f)
		
		Local Open% = ReadByte(f)
		Local OpenState# = ReadFloat(f)
		Local Locked% = ReadByte(f)
		Local AutoClose% = ReadByte(f)
		
		Local OBJX# = ReadFloat(f)
		Local OBJZ# = ReadFloat(f)
		Local OBJYaw# = ReadFloat(f)
		
		Local OBJ2X# = ReadFloat(f)
		Local OBJ2Z# = ReadFloat(f)
		
		Local Timer# = ReadFloat(f)
		Local TimerState# = ReadFloat(f)
		
		Local IsElevDoor% = ReadByte(f)
		Local MTFClose% = ReadByte(f)
		
		For do.Doors = Each Doors
			If EntityX(do\FrameOBJ, True) = X And EntityY(do\FrameOBJ, True) = Y And EntityZ(do\FrameOBJ, True) = Z
				do\Open = Open
				do\OpenState = OpenState
				do\Locked = Locked
				do\AutoClose = AutoClose
				do\Timer = Timer
				do\TimerState = TimerState
				do\IsElevatorDoor = IsElevDoor
				do\MTFClose = MTFClose
				
				PositionEntity(do\OBJ, OBJX, Y, OBJZ, True)
				RotateEntity(do\OBJ, 0.0, OBJYaw, 0.0, True)
				If do\OBJ2 <> 0 Then PositionEntity(do\OBJ2, OBJ2X, Y, OBJ2Z, True)
				Exit
			EndIf
		Next
	Next
	
	If ReadInt(f) <> 1845 Then RuntimeError(GetLocalString("save", "corrupted_4"))
	
	Local de.Decals
	
	For de.Decals = Each Decals
		RemoveDecal(de)
	Next
	
	Temp = ReadInt(f)
	For i = 1 To Temp
		ID = ReadInt(f)
		X = ReadFloat(f)
		Y = ReadFloat(f)
		Z = ReadFloat(f)
		
		Local Pitch# = ReadFloat(f)
		Local Yaw# = ReadFloat(f)
		Local Roll# = ReadFloat(f)
		
		de.Decals = CreateDecal(ID, X, Y, Z, Pitch, Yaw, Roll)
		
		Local Size# = ReadFloat(f)
		Local Alpha# = ReadFloat(f)
		Local FX% = ReadByte(f)
		Local BlendMode% = ReadByte(f)
		Local Red% = ReadByte(f), Green% = ReadByte(f), Blue% = ReadByte(f)
		
		Local DecalTimer# = ReadFloat(f)
		Local LifeTime# = ReadFloat(f)
		Local SizeChange# = ReadFloat(f)
		Local AlphaChange# = ReadFloat(f)
		
		For de.Decals = Each Decals
			If EntityX(de\OBJ, True) = X And EntityY(de\OBJ, True) = Y And EntityZ(de\OBJ, True) = Z
				de\Size = Size
				de\Alpha = Alpha
				de\FX = FX
				de\BlendMode = BlendMode
				de\R = Red : de\G = Green : de\B = Blue
				de\Timer = DecalTimer
				de\LifeTime = LifeTime
				de\SizeChange = SizeChange
				de\AlphaChange = AlphaChange
				
				ScaleEntity(de\OBJ, Size, Size, 1.0, True)
				EntityAlpha(de\OBJ, Alpha)
				EntityFX(de\OBJ, FX)
				EntityBlend(de\OBJ, BlendMode)
				If Red <> 0 Lor Green <> 0 Lor Blue <> 0 Then EntityColor(de\OBJ, Red, Green, Blue)
				Exit
			EndIf
		Next
	Next
	
	Local e.Events
	
	For e.Events = Each Events
		RemoveEvent(e)
	Next
	
	Temp = ReadInt(f)
	For i = 1 To Temp
		e.Events = New Events
		e\EventID = ReadByte(f)
		e\EventState = ReadFloat(f)
		e\EventState2 = ReadFloat(f)
		e\EventState3 = ReadFloat(f)
		e\EventState4 = ReadFloat(f)
		X = ReadFloat(f)
		Z = ReadFloat(f)
		For r.Rooms = Each Rooms
			If EntityX(r\OBJ) = X And EntityZ(r\OBJ) = Z
				e\room = r
				Exit
			EndIf
		Next
		e\EventStr = ReadString(f)
		FindForestEvent(e)
		
		Select(e\EventID)
			Case e_cont2_860_1, e_cont1_205
				;[Block]
				e\EventStr = ""
				;[End Block]
			Case e_cont1_106
				;[Block]
				If e\EventState2 = 0.0 Then PositionEntity(e\room\Objects[1], EntityX(e\room\Objects[1], True), (-1280.0) * RoomScale, EntityZ(e\room\Objects[1], True), True)
				;[End Block]
			Case e_cont2_008
				;[Block]
				If e\EventState < 2.0 Then RotateEntity(e\room\Objects[1], 85.0, EntityYaw(e\room\Objects[1], True), 0.0, True)
				;[End Block]
		End Select
	Next
	
	For it.Items = Each Items
		If (Not IsItemInInventory(it)) Then RemoveItem(it)
	Next
	
	Temp = ReadByte(f)
	While Temp
		Local ittName$ = ReadString(f)
		Local tempName$ = ReadString(f)
		Local Name$ = ReadString(f)
		X = ReadFloat(f)
		Y = ReadFloat(f)
		Z = ReadFloat(f)
		Red% = ReadByte(f)
		Green% = ReadByte(f)
		Blue% = ReadByte(f)
		Alpha# = ReadFloat(f)
		it.Items = CreateItem(ittName, tempName, X, Y, Z, Red,Green,Blue,Alpha)
		it\Name = Name
		EntityType it\Collider, HIT_ITEM
		X = ReadFloat(f)
		Y = ReadFloat(f)
		RotateEntity(it\Collider, X, Y, 0)
		it\State = ReadFloat(f)
		it\State2 = ReadFloat(f)
		it\State3 = ReadFloat(f)
		For itt.ItemTemplates = Each ItemTemplates
			If (itt\TempName = tempName) And (itt\Name = ittName)
				If itt\IsAnim <> 0 Then SetAnimTime it\Model, ReadFloat(f) : Exit
			EndIf
		Next
		it\InvSlots = ReadByte(f)
		it\ID = ReadInt(f)
		If it\ID > LastItemID Then LastItemID = it\ID
		If ReadByte(f) = 0
			it\InvImg = it\ItemTemplate\InvImg
		Else
			it\InvImg = it\ItemTemplate\InvImg2
		EndIf	
		Temp = ReadByte(f)
	Wend
	
	For it = Each Items
		If (Not IsItemInInventory(it)) And it\InvSlots > 0
			For i = 0 To it\InvSlots - 1
				Temp2 = ReadInt(f)
				If Temp2 > -1
					For it2 = Each Items
						If it2\ID = Temp2
							it\SecondInv[i] = it2 : Exit
						EndIf
					Next
				EndIf
			Next
		EndIf
	Next
	
	CloseFile(f)
	
	; ~ TODO: Check If Needed - Wolfnaya
	
	For i = 0 To 1
		mon_I\UpdateCheckpoint[i] = True
	Next
	
	For r.Rooms = Each Rooms
		For i = 0 To MaxRoomAdjacents - 1
			r\Adjacent[i] = Null
		Next
		
		Local r2.Rooms
		
		For r2.Rooms = Each Rooms
			If r <> r2
				If r2\z = r\z
					If r2\x = r\x + 8.0
						r\Adjacent[0] = r2
						If r\AdjDoor[0] = Null Then r\AdjDoor[0] = r2\AdjDoor[2]
					ElseIf r2\x = r\x - 8.0
						r\Adjacent[2] = r2
						If r\AdjDoor[2] = Null Then r\AdjDoor[2] = r2\AdjDoor[0]
					EndIf
				ElseIf r2\x = r\x
					If r2\z = r\z - 8.0
						r\Adjacent[1] = r2
						If r\AdjDoor[1] = Null Then r\AdjDoor[1] = r2\AdjDoor[3]
					ElseIf r2\z = r\z + 8.0
						r\Adjacent[3] = r2
						If r\AdjDoor[3] = Null Then r\AdjDoor[3] = r2\AdjDoor[1]
					EndIf
				EndIf
			EndIf
			If r\Adjacent[0] <> Null And r\Adjacent[1] <> Null And r\Adjacent[2] <> Null And r\Adjacent[3] <> Null Then Exit
		Next
		
		For do.Doors = Each Doors
			If do\KeyCard = 0 And do\Code = ""
				If EntityZ(do\FrameOBJ, True) = r\z
					If EntityX(do\FrameOBJ, True) = r\x + 4.0
						r\AdjDoor[0] = do
					ElseIf EntityX(do\FrameOBJ, True) = r\x - 4.0
						r\AdjDoor[2] = do
					EndIf
				ElseIf EntityX(do\FrameOBJ, True) = r\x
					If EntityZ(do\FrameOBJ, True) = r\z + 4.0
						r\AdjDoor[3] = do
					ElseIf EntityZ(do\FrameOBJ, True) = r\z - 4.0
						r\AdjDoor[1] = do
					EndIf
				EndIf
			EndIf
		Next
	Next
	
End Function
;[End Block]
;~IDEal Editor Parameters:
;~C#Blitz3D TSS