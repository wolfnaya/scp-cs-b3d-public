
; ~ Button ID Constants
;[Block]
Const BUTTON_DEFAULT% = 0
Const BUTTON_KEYCARD% = 1
Const BUTTON_KEYPAD% = 2
Const BUTTON_SCANNER% = 3
Const BUTTON_ELEVATOR% = 4
Const BUTTON_MT_ELEVATOR% = 5
;[End Block]

Function CreateButton%(ButtonID% = BUTTON_DEFAULT, x#, y#, z#, Pitch# = 0.0, Yaw# = 0.0, Roll# = 0.0, Parent% = 0, Locked% = False)
	Local OBJ%
	
	OBJ = CopyEntity(d_I\ButtonModelID[ButtonID])
	ScaleEntity(OBJ, 0.03, 0.03, 0.03)
	PositionEntity(OBJ, x, y, z)
	RotateEntity(OBJ, Pitch, Yaw, Roll)
	EntityPickMode(OBJ, 2)
	If Locked Then EntityTexture(OBJ, d_I\ButtonTextureID[BUTTON_RED_TEXTURE])
	If Parent <> 0 Then EntityParent(OBJ, Parent)
	
	Return(OBJ)
End Function

Function UpdateButton%(OBJ%)
	Local Dist# = EntityDistanceSquared(me\Collider, OBJ)
	Local Result% = False
	
	If Dist < 0.64
		Local Temp% = CreatePivot()
		
		PositionEntity(Temp, EntityX(Camera), EntityY(Camera), EntityZ(Camera))
		PointEntity(Temp, OBJ)
		
		If EntityPick(Temp, 0.6) = OBJ
			If d_I\ClosestButton = 0 Lor Dist < EntityDistanceSquared(me\Collider, d_I\ClosestButton)
				d_I\ClosestButton = OBJ
				FreeEntity(Temp) : Temp = 0
				Return(True)
			EndIf
		EndIf
		FreeEntity(Temp) : Temp = 0
	EndIf
	Return(False)
End Function

;[Block]
Type BrokenDoor
	Field IsBroken%
	Field x#, z#
End Type
;[End Block]

Global bk.BrokenDoor

; ~ Door ID Constants
;[Block]
Const DEFAULT_DOOR% = 0
Const ELEVATOR_DOOR% = 1
Const HEAVY_DOOR% = 2
Const BIG_DOOR% = 3
Const OFFICE_DOOR% = 4, WOODEN_DOOR% = 5
Const ONE_SIDED_DOOR% = 6, SCP_914_DOOR% = 7
Const MT_ELEVATOR_DOOR% = 8
Const LCZ_DOOR% = 9, HCZ_DOOR% = 10, EZ_DOOR% = 11, RCZ_DOOR% = 12
;[End Block]

;[Block]
Type Doors
	Field OBJ%, OBJ2%, FrameOBJ%, Buttons%[2]
	Field Locked%, LockedUpdated%, Open%, Angle%, OpenState#, FastOpen%
	Field DoorType%, Dist#
	Field Timer%, TimerState#
	Field KeyCard%
	Field room.Rooms
	Field DisableWaypoint%
	Field SoundCHN%, SoundCHN2%
	Field ButtonCHN%
	Field Code$
	Field AutoClose%
	Field LinkedDoor.Doors
	Field IsElevatorDoor% = False
	Field MTFClose% = True
	Field ElevatorPanel%[2]
	Field PlayCautionSFX%
End Type
;[End Block]

Function CreateDoor.Doors(room.Rooms, x#, y#, z#, Angle#, Open% = False, DoorType% = DEFAULT_DOOR, Keycard% = KEY_MISC, Code$ = "", CustomParent% = 0)
	Local d.Doors
	Local Parent%, i%
	Local FrameScaleX#, FrameScaleY#, FrameScaleZ#
	Local DoorScaleX#, DoorScaleY#, DoorScaleZ#
	Local FrameModelID%, DoorModelID_1%, DoorModelID_2%, ButtonID%
	
	If room <> Null
		Parent = room\OBJ
	Else
		Parent = CustomParent
	EndIf
	
	d.Doors = New Doors
	
	; ~ (Keycard > 0) - KEY CARD
	; ~ (Keycard = 0) - DEFAULT
	; ~ (Keycard > -4 And Keycard < 0) - HAND
	; ~ (Keycard <= -4) - KEY
	
	d\KeyCard = Keycard 
	
	d\Code = Code
	
	d\Angle = Angle
	d\Open = Open
	
	; ~ Set "d\Locked = 1" for elevator doors to fix buttons color. Anyway the door will be unlocked by "UpdateMTElevators" function. -- Jabka
	If DoorType = MT_ELEVATOR_DOOR Then d\Locked = 1
	d\DoorType = DoorType
	If DoorType = SCP_914_DOOR Then DoorType = ONE_SIDED_DOOR
	
	d\MTFClose = True
	d\AutoClose = (Open And ((DoorType = DEFAULT_DOOR) Lor (DoorType = HEAVY_DOOR)) And (Keycard = 0) And (Code = "") And Rand(10) = 1)
	
	d\room = room
	
	Select(DoorType)
		Case DEFAULT_DOOR
			;[Block]
			DoorModelID_1 = DOOR_DEFAULT_MODEL
			DoorModelID_2 = DoorModelID_1
			DoorScaleX = 203.0 * RoomScale / MeshWidth(d_I\DoorModelID[DoorModelID_1]) : DoorScaleY = 313.0 * RoomScale / MeshHeight(d_I\DoorModelID[DoorModelID_1]) : DoorScaleZ = 15.0 * RoomScale / MeshDepth(d_I\DoorModelID[DoorModelID_1])
			
			FrameModelID = DOOR_DEFAULT_FRAME_MODEL
			FrameScaleX = RoomScale : FrameScaleY = RoomScale : FrameScaleZ = RoomScale
			;[End Block]
		Case ONE_SIDED_DOOR
			;[Block]
			DoorModelID_1 = DOOR_ONE_SIDED_MODEL
			DoorModelID_2 = DoorModelID_1
			DoorScaleX = 203.0 * RoomScale / MeshWidth(d_I\DoorModelID[DoorModelID_1]) : DoorScaleY = 313.0 * RoomScale / MeshHeight(d_I\DoorModelID[DoorModelID_1]) : DoorScaleZ = 15.0 * RoomScale / MeshDepth(d_I\DoorModelID[DoorModelID_1])
			
			FrameModelID = DOOR_DEFAULT_FRAME_MODEL
			FrameScaleX = RoomScale : FrameScaleY = RoomScale : FrameScaleZ = RoomScale
			;[End Block]
		Case ELEVATOR_DOOR, MT_ELEVATOR_DOOR
			;[Block]
			DoorModelID_1 = DOOR_ELEVATOR_MODEL
			DoorModelID_2 = DoorModelID_1
			DoorScaleX = RoomScale : DoorScaleY = RoomScale : DoorScaleZ = RoomScale
			
			FrameModelID = DOOR_DEFAULT_FRAME_MODEL
			FrameScaleX = RoomScale : FrameScaleY = RoomScale : FrameScaleZ = RoomScale
			;[End Block]
		Case HEAVY_DOOR
			;[Block]
			DoorModelID_1 = DOOR_HEAVY_MODEL_1
			DoorModelID_2 = DOOR_HEAVY_MODEL_2
			DoorScaleX = RoomScale : DoorScaleY = RoomScale : DoorScaleZ = RoomScale
			
			FrameModelID = DOOR_DEFAULT_FRAME_MODEL
			FrameScaleX = RoomScale : FrameScaleY = RoomScale : FrameScaleZ = RoomScale
			;[End Block]
		Case BIG_DOOR
			;[Block]
			DoorModelID_1 = DOOR_BIG_MODEL_1
			DoorModelID_2 = DOOR_BIG_MODEL_2
			DoorScaleX = 55.0 * RoomScale : DoorScaleY = 55.0 * RoomScale : DoorScaleZ = 55.0 * RoomScale
			
			FrameModelID = DOOR_BIG_FRAME_MODEL
			FrameScaleX = 55.0 * RoomScale : FrameScaleY = 55.0 * RoomScale : FrameScaleZ = 55.0 * RoomScale
			;[End Block]
		Case OFFICE_DOOR
			;[Block]
			DoorModelID_1 = DOOR_OFFICE_MODEL
			DoorScaleX = RoomScale : DoorScaleY = RoomScale : DoorScaleZ = RoomScale
			
			FrameModelID = DOOR_OFFICE_FRAME_MODEL
			FrameScaleX = RoomScale : FrameScaleY = RoomScale : FrameScaleZ = RoomScale
			;[End Block]
		Case WOODEN_DOOR
			;[Block]
			DoorModelID_1 = DOOR_WOODEN_MODEL
			DoorScaleX = 46.0 * RoomScale : DoorScaleY = 44.0 * RoomScale : DoorScaleZ = 46.0 * RoomScale
			
			FrameModelID = DOOR_WOODEN_FRAME_MODEL
			FrameScaleX = 45.0 * RoomScale : FrameScaleY = 44.0 * RoomScale : FrameScaleZ = 80.0 * RoomScale
			;[End Block]
		Case LCZ_DOOR
			;[Block]
			DoorModelID_1 = DOOR_LCZ_MODEL
			DoorScaleX = 203.0 * RoomScale / MeshWidth(d_I\DoorModelID[DoorModelID_1]) : DoorScaleY = 313.0 * RoomScale / MeshHeight(d_I\DoorModelID[DoorModelID_1]) : DoorScaleZ = 15.0 * RoomScale / MeshDepth(d_I\DoorModelID[DoorModelID_1])
			
			FrameModelID = DOOR_LCZ_FRAME_MODEL
			FrameScaleX = RoomScale : FrameScaleY = RoomScale : FrameScaleZ = RoomScale
			;[End Block]
		Case HCZ_DOOR
			;[Block]
			DoorModelID_1 = DOOR_HCZ_MODEL
			DoorScaleX = RoomScale : DoorScaleY = RoomScale : DoorScaleZ = RoomScale
			
			FrameModelID = DOOR_HCZ_FRAME_MODEL
			FrameScaleX = RoomScale : FrameScaleY = RoomScale : FrameScaleZ = RoomScale
			;[End Block]
		Case EZ_DOOR
			;[Block]
			DoorModelID_1 = DOOR_EZ_MODEL
			DoorScaleX = 203.0 * RoomScale / MeshWidth(d_I\DoorModelID[DoorModelID_1]) : DoorScaleY = 313.0 * RoomScale / MeshHeight(d_I\DoorModelID[DoorModelID_1]) : DoorScaleZ = 15.0 * RoomScale / MeshDepth(d_I\DoorModelID[DoorModelID_1])
			
			FrameModelID = DOOR_EZ_FRAME_MODEL
			FrameScaleX = RoomScale : FrameScaleY = RoomScale : FrameScaleZ = RoomScale
			;[End Block]
		Case RCZ_DOOR
			;[Block]
			DoorModelID_1 = DOOR_RCZ_MODEL_1
			DoorModelID_2 = DOOR_RCZ_MODEL_2
			DoorScaleX = RoomScale : DoorScaleY = RoomScale : DoorScaleZ = RoomScale
			
			FrameModelID = DOOR_RCZ_FRAME_MODEL
			FrameScaleX = RoomScale : FrameScaleY = RoomScale : FrameScaleZ = RoomScale
			;[End Block]
	End Select
	
	d\FrameOBJ = CopyEntity(d_I\DoorFrameModelID[FrameModelID])
	ScaleEntity(d\FrameOBJ, FrameScaleX, FrameScaleY, FrameScaleZ)
	PositionEntity(d\FrameOBJ, x, y, z)
	If DoorType = BIG_DOOR Then EntityType(d\FrameOBJ, HIT_MAP)
	EntityPickMode(d\FrameOBJ, 2)
	
	d\OBJ = CopyEntity(d_I\DoorModelID[DoorModelID_1])
	ScaleEntity(d\OBJ, DoorScaleX, DoorScaleY, DoorScaleZ)
	PositionEntity(d\OBJ, x, y, z)
	RotateEntity(d\OBJ, 0.0, Angle, 0.0)
	EntityType(d\OBJ, HIT_MAP)
	EntityPickMode(d\OBJ, 2)
	EntityParent(d\OBJ, Parent)
	
	If DoorType <> OFFICE_DOOR And DoorType <> WOODEN_DOOR
		d\OBJ2 = CopyEntity(d_I\DoorModelID[DoorModelID_2])
		ScaleEntity(d\OBJ2, DoorScaleX, DoorScaleY, DoorScaleZ)
		PositionEntity(d\OBJ2, x, y, z)
		RotateEntity(d\OBJ2, 0.0, Angle + ((DoorType <> BIG_DOOR) * 180.0), 0.0)
		EntityType(d\OBJ2, HIT_MAP)
		EntityPickMode(d\OBJ2, 2)
		EntityParent(d\OBJ2, Parent)
	EndIf
	
	For i = 0 To 1
		If (DoorType = OFFICE_DOOR) Lor (DoorType = WOODEN_DOOR)
			If (Not d\Open)
				d\Buttons[i] = CreatePivot()
				PositionEntity(d\Buttons[i], x - 0.22, y + 0.6, z + 0.1 + (i * (-0.2)))
				EntityRadius(d\Buttons[i], 0.1)
				EntityPickMode(d\Buttons[i], 1)
				EntityParent(d\Buttons[i], d\FrameOBJ)
			EndIf
		Else
			If DoorType = ELEVATOR_DOOR
				If i = 0 Then ButtonID = BUTTON_ELEVATOR
				If i = 1 Then ButtonID = BUTTON_MT_ELEVATOR
				
				d\ElevatorPanel[i] = CopyEntity(d_I\ElevatorPanelModel)
				ScaleEntity(d\ElevatorPanel[i], RoomScale, RoomScale, RoomScale)
				RotateEntity(d\ElevatorPanel[i], 0.0, i * 180.0, 0.0)
				PositionEntity(d\ElevatorPanel[i], x, y + 1.27, z + 0.13 + (i * (-0.26)))
				EntityParent(d\ElevatorPanel[i], d\FrameOBJ)
			ElseIf DoorType = MT_ELEVATOR_DOOR
				ButtonID = i * BUTTON_MT_ELEVATOR
				
				d\ElevatorPanel[i] = CopyEntity(d_I\ElevatorPanelModel)
				ScaleEntity(d\ElevatorPanel[i], RoomScale, RoomScale, RoomScale)
				RotateEntity(d\ElevatorPanel[i], 0.0, i * 180.0, 0.0)
				PositionEntity(d\ElevatorPanel[i], x, y + 1.27, z + 0.13 + (i * (-0.26)))
				EntityParent(d\ElevatorPanel[i], d\FrameOBJ)
			Else
				If Code <> ""
					ButtonID = BUTTON_KEYPAD
				ElseIf Keycard > KEY_MISC
					ButtonID = BUTTON_KEYCARD
				ElseIf Keycard > KEY_860 And Keycard < KEY_MISC
					ButtonID = BUTTON_SCANNER
				Else
					ButtonID = BUTTON_DEFAULT
				EndIf
			EndIf
			d\Buttons[i] = CreateButton(ButtonID, x + ((DoorType <> BIG_DOOR) * (0.6 + (i * (-1.2)))) + ((DoorType = BIG_DOOR) * ((-432.0 + (i * 864.0)) * RoomScale)), y + 0.7, z + ((DoorType <> BIG_DOOR) * ((-0.1) + (i * 0.2))) + ((DoorType = BIG_DOOR) * ((192.0 + (i * (-384.0)))) * RoomScale), 0.0, ((DoorType <> BIG_DOOR) * (i * 180.0)) + ((DoorType = BIG_DOOR) * (90.0 + (i * 180.0))), 0.0, d\FrameOBJ, d\Locked)
		EndIf
	Next
	RotateEntity(d\FrameOBJ, 0.0, Angle, 0.0)
	EntityParent(d\FrameOBJ, Parent)
	
	Return(d)
End Function

Function UpdateDoors%()
	Local d.Doors, p.Particles
	Local x#, z#, Dist#, i%, FindButton%
	Local SinValue#
	Local FPSFactorEx#
	
	d_I\ClosestButton = 0
	d_I\ClosestDoor = Null
	For d.Doors = Each Doors
		If (EntityDistanceSquared(d\FrameOBJ, me\Collider) <= PowTwo(HideDistance * 1.75)) Lor (d\IsElevatorDoor > 0) ; ~ Make elevator doors update everytime because if not, this can cause a bug where the elevators suddenly won't work, most noticeable in room2_mt -- ENDSHN
			; ~ Automatically disable d\AutoClose parameter in order to prevent player get stuck -- Jabka
			If d\AutoClose And d\Locked > 0 Then d\AutoClose = False
			FindButton = (1 - (d\Open And ((d\DoorType = OFFICE_DOOR) Lor (d\DoorType = WOODEN_DOOR))))
			
			If ((d\OpenState >= 180.0 Lor d\OpenState <= 0.0) And FindButton) And GrabbedEntity = 0
				For i = 0 To 1
					If d\Buttons[i] <> 0
						If Abs(EntityX(me\Collider) - EntityX(d\Buttons[i], True)) < 1.0 And Abs(EntityZ(me\Collider) - EntityZ(d\Buttons[i], True)) < 1.0
							If UpdateButton(d\Buttons[i])
								d_I\ClosestDoor = d : me\SndVolume = 4.0 : d_I\AnimButton = d_I\ClosestButton
								Exit
							EndIf
						EndIf
					EndIf
				Next
			EndIf
			
			Local FPSFactorDoubled# = fps\Factor[0] * 2.0
			
			If d\Open
				If d\OpenState < 180.0
					Select(d\DoorType)
						Case DEFAULT_DOOR
							;[Block]
							d\OpenState = Min(180.0, d\OpenState + (FPSFactorDoubled * (d\FastOpen + 1)))
							FPSFactorEx = Sin(d\OpenState) * (d\FastOpen + 1) * fps\Factor[0] / 80.0
							MoveEntity(d\OBJ, FPSFactorEx, 0.0, 0.0)
							If d\OBJ2 <> 0 Then MoveEntity(d\OBJ2, FPSFactorEx, 0.0, 0.0)
							;[End Block]
						Case ELEVATOR_DOOR, MT_ELEVATOR_DOOR
							;[Block]
							d\OpenState = Min(180.0, d\OpenState + (FPSFactorDoubled * (d\FastOpen + 1)))
							FPSFactorEx = Sin(d\OpenState) * (d\FastOpen + 1) * fps\Factor[0] / 162.0
							MoveEntity(d\OBJ, FPSFactorEx, 0.0, 0.0)
							If d\OBJ2 <> 0 Then MoveEntity(d\OBJ2, FPSFactorEx, 0.0, 0.0)
							;[End Block]
						Case HEAVY_DOOR
							;[Block]
							d\OpenState = Min(180.0, d\OpenState + (FPSFactorDoubled * (d\FastOpen + 1)))
							SinValue = Sin(d\OpenState)
							MoveEntity(d\OBJ, SinValue * (d\FastOpen + 1) * fps\Factor[0] / 85.0, 0.0, 0.0)
							If d\OBJ2 <> 0 Then MoveEntity(d\OBJ2, SinValue * (d\FastOpen + 1) * fps\Factor[0] / 120.0, 0.0, 0.0)
							;[End Block]
						Case BIG_DOOR
							;[Block]
							d\OpenState = Min(180.0, d\OpenState + (fps\Factor[0] * 0.8))
							SinValue = Sin(d\OpenState)
							FPSFactorEx = fps\Factor[0] / 180.0
							MoveEntity(d\OBJ, SinValue * FPSFactorEx, 0.0, 0.0)
							If d\OBJ2 <> 0 Then MoveEntity(d\OBJ2, (-SinValue) * FPSFactorEx, 0.0, 0.0)
							;[End Block]
						Case OFFICE_DOOR, WOODEN_DOOR
							;[Block]
							If d\room <> Null
								d\OpenState = CurveValue(180.0, d\OpenState, 40.0) + (fps\Factor[0] * 0.01)
								RotateEntity(d\OBJ, 0.0, d\room\Angle + d\Angle + (d\OpenState / 2.5), 0.0)
							EndIf
							;[End Block]
						Case ONE_SIDED_DOOR, LCZ_DOOR, HCZ_DOOR, EZ_DOOR
							;[Block]
							d\OpenState = Min(180.0, d\OpenState + (FPSFactorDoubled * (d\FastOpen + 1)))
							SinValue = Sin(d\OpenState)
							FPSFactorEx = fps\Factor[0] / 80.0
							MoveEntity(d\OBJ, SinValue * (d\FastOpen + 1) * FPSFactorEx, 0.0, 0.0)
							If d\OBJ2 <> 0 Then MoveEntity(d\OBJ2, SinValue * (d\FastOpen + 1) * (-FPSFactorEx), 0.0, 0.0)
							If d\DoorType = HCZ_DOOR Then Animate2(d\OBJ, AnimTime(d\OBJ), 1.0, 20.0, 0.7, False)
							;[End Block]
						Case RCZ_DOOR
							;[Block]
							d\OpenState = Min(180.0, d\OpenState + (FPSFactorDoubled * (d\FastOpen + 1)))
							SinValue = Sin(d\OpenState)
							MoveEntity(d\OBJ, 0.0, SinValue * (d\FastOpen + 1) * fps\Factor[0] / 82.0, 0.0)
							If d\OBJ2 <> 0 Then MoveEntity(d\OBJ2, 0.0, -(SinValue * (d\FastOpen + 1) * fps\Factor[0] / 82.0), 0.0)
							Animate2(d\FrameOBJ,AnimTime(d\FrameOBJ), 1.0, 20.0, 0.1, False)
							;[End Block]
						Case SCP_914_DOOR ; ~ Used for SCP-914 only
							;[Block]
							d\OpenState = Min(180.0, d\OpenState + (fps\Factor[0] * 1.4))
							SinValue = Sin(d\OpenState)
							FPSFactorEx = fps\Factor[0] / 114.0
							MoveEntity(d\OBJ, SinValue * FPSFactorEx, 0.0, 0.0)
							If d\OBJ2 <> 0 Then MoveEntity(d\OBJ2, SinValue * (-FPSFactorEx), 0.0, 0.0)
							;[End Block]
					End Select
				Else
					If d\DoorType = RCZ_DOOR
						SetAnimTime(d\FrameOBJ, 21.0)
					ElseIf d\DoorType = HCZ_DOOR
						SetAnimTime(d\OBJ, 21.0)
					EndIf
					d\FastOpen = False
					ResetEntity(d\OBJ)
					If d\OBJ2 <> 0 Then ResetEntity(d\OBJ2)
					If d\TimerState > 0.0
						d\TimerState = Max(0.0, d\TimerState - fps\Factor[0])
						If d\PlayCautionSFX And (d\TimerState + fps\Factor[0] > 110.0 And d\TimerState <= 110.0) Then d\SoundCHN = PlaySound2(CautionSFX, Camera, d\OBJ)
						If d\TimerState = 0.0 Then OpenCloseDoor(d)
					EndIf
					If d\AutoClose And RemoteDoorOn
						If EntityDistanceSquared(Camera, d\OBJ) < 4.41
							If I_714\Using = 0 And wi\GasMask <> 4 And wi\HazmatSuit <> 4 Then PlaySound_Strict(HorrorSFX[7])
							OpenCloseDoor(d) : d\AutoClose = False
						EndIf
					EndIf
				EndIf
			Else
				Local FrameX# = EntityX(d\FrameOBJ, True)
				Local FrameY# = EntityY(d\FrameOBJ, True)
				Local FrameZ# = EntityZ(d\FrameOBJ, True)
				
				If d\OpenState > 0.0
					Select(d\DoorType)
						Case DEFAULT_DOOR
							;[Block]
							d\OpenState = Max(0.0, d\OpenState - (FPSFactorDoubled * (d\FastOpen + 1)))
							FPSFactorEx = Sin(d\OpenState) * (d\FastOpen + 1) * (-fps\Factor[0]) / 80.0
							MoveEntity(d\OBJ, FPSFactorEx, 0.0, 0.0)
							If d\OBJ2 <> 0 Then MoveEntity(d\OBJ2, FPSFactorEx, 0.0, 0.0)
							;[End Block]
						Case ELEVATOR_DOOR, MT_ELEVATOR_DOOR
							;[Block]
							d\OpenState = Max(0.0, d\OpenState - (FPSFactorDoubled * (d\FastOpen + 1)))
							FPSFactorEx = Sin(d\OpenState) * (d\FastOpen + 1) * (-fps\Factor[0]) / 162.0
							MoveEntity(d\OBJ, FPSFactorEx, 0.0, 0.0)
							If d\OBJ2 <> 0 Then MoveEntity(d\OBJ2, FPSFactorEx, 0.0, 0.0)
							;[End Block]
						Case HEAVY_DOOR
							;[Block]
							d\OpenState = Max(0.0, d\OpenState - (FPSFactorDoubled * (d\FastOpen + 1)))
							SinValue = Sin(d\OpenState)
							MoveEntity(d\OBJ, SinValue * (d\FastOpen + 1) * (-fps\Factor[0]) / 85.0, 0.0, 0.0)
							If d\OBJ2 <> 0 Then MoveEntity(d\OBJ2, SinValue * (d\FastOpen + 1) * (-fps\Factor[0]) / 120.0, 0.0, 0.0)
							;[End Block]
						Case BIG_DOOR
							;[Block]
							d\OpenState = Max(0.0, d\OpenState - (fps\Factor[0] * 0.8))
							SinValue = Sin(d\OpenState)
							FPSFactorEx = fps\Factor[0] / 180.0
							MoveEntity(d\OBJ, SinValue * (-FPSFactorEx), 0.0, 0.0)
							If d\OBJ2 <> 0 Then MoveEntity(d\OBJ2, SinValue * FPSFactorEx, 0.0, 0.0)
							If d\OpenState < 15.0 And d\OpenState + fps\Factor[0] >= 15.0
								If opt\ParticleAmount = 2
									For i = 0 To Rand(75, 99)
										Local Pvt% = CreatePivot()
										
										PositionEntity(Pvt, FrameX + Rnd(-0.2, 0.2), FrameY + Rnd(0.0, 1.2), FrameZ + Rnd(-0.2, 0.2))
										RotateEntity(Pvt, 0.0, Rnd(360.0), 0.0)
										
										p.Particles = CreateParticle(PARTICLE_DUST, EntityX(Pvt), EntityY(Pvt), EntityZ(Pvt), 0.002, 0.0, 300.0)
										p\Speed = 0.005 : p\SizeChange = -0.00001 : p\Size = 0.01 : p\AlphaChange = -0.01
										RotateEntity(p\Pvt, Rnd(-20.0, 20.0), Rnd(360.0), 0.0)
										ScaleSprite(p\OBJ, p\Size, p\Size)
										EntityOrder(p\OBJ, -1)
										FreeEntity(Pvt) : Pvt = 0
									Next
								EndIf
							EndIf
							;[End Block]
						Case OFFICE_DOOR, WOODEN_DOOR
							;[Block]
							d\OpenState = 0.0
							RotateEntity(d\OBJ, 0.0, EntityYaw(d\FrameOBJ), 0.0)
							;[End Block]
						Case ONE_SIDED_DOOR, LCZ_DOOR, HCZ_DOOR, EZ_DOOR
							;[Block]
							d\OpenState = Max(0.0, d\OpenState - (FPSFactorDoubled * (d\FastOpen + 1)))
							SinValue = Sin(d\OpenState)
							FPSFactorEx = fps\Factor[0] / 80.0
							MoveEntity(d\OBJ, SinValue * (d\FastOpen + 1) * (-FPSFactorEx), 0.0, 0.0)
							If d\OBJ2 <> 0 Then MoveEntity(d\OBJ2, SinValue * (d\FastOpen + 1) * FPSFactorEx, 0.0, 0.0)
							If d\DoorType = HCZ_DOOR Then Animate2(d\OBJ, AnimTime(d\OBJ), 21.0, 50.0, 0.7, False)
							;[End Block]
						Case RCZ_DOOR
							;[Block]
							d\OpenState = Max(0.0, d\OpenState - (FPSFactorDoubled * (d\FastOpen + 1)))
							SinValue = Sin(d\OpenState)
							MoveEntity(d\OBJ, 0.0, SinValue * (-fps\Factor[0]) * (d\FastOpen + 1) / 82.0, 0.0)
							If d\OBJ2 <> 0 Then MoveEntity(d\OBJ2, 0.0, -(SinValue * (-fps\Factor[0]) * (d\FastOpen + 1) / 82.0), 0.0)
							Animate2(d\FrameOBJ,AnimTime(d\FrameOBJ), 21.0, 40.0, 0.45, False)
							;[End Block]
						Case SCP_914_DOOR ; ~ Used for SCP-914 only
							;[Block]
							d\OpenState = Min(180.0, d\OpenState - (fps\Factor[0] * 1.4))
							SinValue = Sin(d\OpenState)
							FPSFactorEx = fps\Factor[0] / 114.0
							MoveEntity(d\OBJ, SinValue * (-FPSFactorEx), 0.0, 0.0)
							If d\OBJ2 <> 0 Then MoveEntity(d\OBJ2, SinValue * FPSFactorEx, 0.0, 0.0)
							;[End Block]
					End Select
				Else
					If d\DoorType = RCZ_DOOR
						SetAnimTime(d\FrameOBJ, 1.0)
					ElseIf d\DoorType = HCZ_DOOR
						SetAnimTime(d\OBJ, 1.0)
					EndIf
					d\FastOpen = False
					PositionEntity(d\OBJ, FrameX, FrameY, FrameZ)
					If d\DoorType = DEFAULT_DOOR Lor d\DoorType = ONE_SIDED_DOOR Lor d\DoorType = SCP_914_DOOR
						MoveEntity(d\OBJ, 0.0, 0.0, 8.0 * RoomScale)
					ElseIf d\DoorType = OFFICE_DOOR Lor d\DoorType = WOODEN_DOOR
						MoveEntity(d\OBJ, (((d\DoorType = OFFICE_DOOR) * 92.0) + ((d\DoorType = WOODEN_DOOR) * 68.0)) * RoomScale, 0.0, 0.0)
					EndIf
					If d\OBJ2 <> 0
						PositionEntity(d\OBJ2, FrameX, FrameY, FrameZ)
						If d\DoorType = DEFAULT_DOOR Lor d\DoorType = ONE_SIDED_DOOR Lor d\DoorType = SCP_914_DOOR Then MoveEntity(d\OBJ2, 0.0, 0.0, 8.0 * RoomScale)
					EndIf
				EndIf
			EndIf
			If (Not (d\DoorType = WOODEN_DOOR And PlayerRoom\RoomTemplate\RoomID = r_cont2_860_1)) Then UpdateSoundOrigin(d\SoundCHN, Camera, d\FrameOBJ)
			
			If d\DoorType = BIG_DOOR
				If d\Locked = 2
					If d\OpenState > 48.0
						d\Open = False
						d\OpenState = Min(d\OpenState, 48.0)
					Else
						If EntityDistanceSquared(me\Collider, d\FrameOBJ) < 0.16
							If d\OpenState > 6.0 And d\Open = False And (Not chs\GodMode)
								If me\Health > 0 Then PlaySound_Strict(Death914SFX)
								msg\DeathMsg = Format(GetLocalString("death", "door"), Occupation)
								Kill(True)
							EndIf
						EndIf
					EndIf
				EndIf
			EndIf
			
			If d\DoorType <> OFFICE_DOOR And d\DoorType <> WOODEN_DOOR
				Local TextureID%
				If d\KeyCard = KEY_005
					TextureID = BUTTON_106_TEXTURE
				ElseIf d\OpenState > 0.0 And d\OpenState < 180.0
					TextureID = BUTTON_YELLOW_TEXTURE
				ElseIf d\Locked = 1
					TextureID = BUTTON_RED_TEXTURE
				Else
					TextureID = BUTTON_GREEN_TEXTURE
				EndIf
				
				; ~ TODO: FIND SOLUTION TO PREVENT FUNCTION LOOP
				
				If d\DoorType = ELEVATOR_DOOR Then
					i = 1
					If d\Buttons[i] <> 0 Then EntityTexture(d\Buttons[i], d_I\ButtonTextureID[TextureID])
				Else
					For i = 0 To 1
						If d\Buttons[i] <> 0 Then EntityTexture(d\Buttons[i], d_I\ButtonTextureID[TextureID])
					Next
				EndIf
				
				Local elev.Elevator
				
				If d\KeyCard = KEY_MISC
					If ChannelPlaying(d\ButtonCHN)
						If d_I\AnimButton <> 0
							If PlayerInsideMTElevator
								If InFacility = LowerFloor Lor (InFacility <> UpperFloor And ToMTElevatorFloor = UpperFloor)
									Animate2(d_I\AnimButton, AnimTime(d_I\AnimButton), 1.0, 20.0, 2.0, False)
								Else
									Animate2(d_I\AnimButton, AnimTime(d_I\AnimButton), 21.0, 40.0, 2.0, False)
								EndIf
							ElseIf PlayerInsideElevator
								For elev.Elevator = Each Elevator
									If elev\tofloor = 3 Then
										Animate2(d_I\AnimButton, AnimTime(d_I\AnimButton), 1.0, 20.0, 1.0, False)
									ElseIf elev\tofloor = 2 Then
										Animate2(d_I\AnimButton, AnimTime(d_I\AnimButton), 21.0, 40.0, 1.0, False)
									Else
										Animate2(d_I\AnimButton, AnimTime(d_I\AnimButton), 41.0, 60.0, 1.0, False)
									EndIf
								Next
							Else
								Animate2(d_I\AnimButton, AnimTime(d_I\AnimButton), 1.0, 20.0, 2.0, False)
							EndIf
						EndIf
					EndIf
				EndIf
			ElseIf d\DoorType = OFFICE_DOOR
				If ChannelPlaying(d\ButtonCHN) Then Animate2(d\OBJ, AnimTime(d\OBJ), 1.0, 41.0, 1.2, False)
			EndIf
		EndIf
	Next
End Function

Global CODE_DR_MAYNARD%, CODE_O5_COUNCIL%, CODE_MAINTENANCE_TUNNELS%
; ~ Doors Code Constants
;[Block]
Const CODE_DR_HARP% = 7816
Const CODE_DR_L% = 2411
Const CODE_CONT1_035% = 5731
Const CODE_LOCKED$ = "GEAR"
;[End Block]

Function UseDoor%(PlaySFX% = True)
	Local Temp%, i%
	
	If SelectedItem <> Null Then Temp = GetUsingItem(SelectedItem)
	If d_I\ClosestDoor\KeyCard > KEY_MISC
		If SelectedItem = Null
			CreateMsg(GetLocalString("msg", "key.require"))
			d_I\ClosestDoor\ButtonCHN = PlaySound2(ButtonSFX[0], Camera, d_I\ClosestButton)
			Return
		Else
			If Temp <= KEY_MISC
				CreateMsg(GetLocalString("msg", "key.require"))
			Else
				If Temp = KEY_CARD_6
					CreateMsg(GetLocalString("msg", "key.slot.6"))
				Else
					If d_I\ClosestDoor\Locked = 1
						If Temp = KEY_005
							CreateMsg(GetLocalString("msg", "key.nothappend.005"))
						Else
							CreateMsg(GetLocalString("msg", "key.nothappend"))
						EndIf
					Else
						If Temp = KEY_005
							CreateMsg(GetLocalString("msg", "key.005"))
						Else
							If Temp < d_I\ClosestDoor\KeyCard
								If d_I\ClosestDoor\KeyCard = KEY_005
									CreateMsg(GetLocalString("msg", "key.required.106"))
								Else
									CreateMsg(Format(GetLocalString("msg", "key.higher"), d_I\ClosestDoor\KeyCard - 2))
								EndIf
							Else
								CreateMsg(GetLocalString("msg", "key.slot"))
							EndIf
						EndIf
					EndIf
				EndIf
;				SelectedItem = Null
			EndIf
			If (d_I\ClosestDoor\Locked <> 1) And (((Temp > KEY_MISC) And (Temp <> KEY_CARD_6) And (Temp >= d_I\ClosestDoor\KeyCard)) Lor (Temp = KEY_005))
				d_I\ClosestDoor\ButtonCHN = PlaySound2(KeyCardSFX1, Camera, d_I\ClosestButton)
			Else
				If Temp <= KEY_MISC
					d_I\ClosestDoor\ButtonCHN = PlaySound2(ButtonSFX[0], Camera, d_I\ClosestButton)
				Else
					d_I\ClosestDoor\ButtonCHN = PlaySound2(KeyCardSFX2, Camera, d_I\ClosestButton)
				EndIf
				Return
			EndIf
		EndIf
	ElseIf d_I\ClosestDoor\KeyCard > KEY_860 And d_I\ClosestDoor\KeyCard < KEY_MISC
		If SelectedItem = Null
			CreateMsg(GetLocalString("msg", "dna.denied_1"))
			d_I\ClosestDoor\ButtonCHN = PlaySound2(ScannerSFX2, Camera, d_I\ClosestButton)
			Return
		Else
			If ((Temp >= KEY_MISC) Lor (Temp < KEY_HAND_YELLOW)) And (Temp <> KEY_005)
				CreateMsg(GetLocalString("msg", "dna.denied_1"))
			Else
				If (d_I\ClosestDoor\KeyCard <> Temp) And (Temp <> KEY_005)
					CreateMsg(GetLocalString("msg", "dna.denied_2"))
				Else
					If d_I\ClosestDoor\Locked = 1
						If Temp = KEY_005
							CreateMsg(GetLocalString("msg", "key.nothappend.005"))
						Else
							CreateMsg(GetLocalString("msg", "key.nothappend"))
						EndIf
					Else
						If Temp = KEY_005
							CreateMsg(GetLocalString("msg", "dna.granted.005"))
						Else
							CreateMsg(GetLocalString("msg", "dna.granted"))
						EndIf
					EndIf
				EndIf
;				SelectedItem = Null
			EndIf
			If (d_I\ClosestDoor\Locked = 0) And ((Temp = d_I\ClosestDoor\KeyCard) Lor (Temp = KEY_005))
				d_I\ClosestDoor\ButtonCHN = PlaySound2(ScannerSFX1, Camera, d_I\ClosestButton)
			Else
				d_I\ClosestDoor\ButtonCHN = PlaySound2(ScannerSFX2, Camera, d_I\ClosestButton)
				Return
			EndIf
		EndIf
	ElseIf d_I\ClosestDoor\Code <> ""
		If SelectedItem = Null
			If (d_I\ClosestDoor\Locked = 0) And (d_I\ClosestDoor\Code <> CODE_LOCKED) And (d_I\ClosestDoor\Code = msg\KeyPadInput)
				d_I\ClosestDoor\ButtonCHN = PlaySound2(ScannerSFX1, Camera, d_I\ClosestButton)
			Else
				d_I\ClosestDoor\ButtonCHN = PlaySound2(ScannerSFX2, Camera, d_I\ClosestButton)
				Return
			EndIf
		Else
			If Temp = KEY_005
				If d_I\ClosestDoor\Locked = 1
					CreateMsg(GetLocalString("msg", "keypad.nothappend.005"))
				Else
					CreateMsg(GetLocalString("msg", "keypad.nothappend"))
				EndIf
			EndIf
;			SelectedItem = Null
			
			If (d_I\ClosestDoor\Locked = 0) And (d_I\ClosestDoor\Code <> CODE_LOCKED) And (Temp = KEY_005)
				d_I\ClosestDoor\ButtonCHN = PlaySound2(ScannerSFX1, Camera, d_I\ClosestButton)
			Else
				d_I\ClosestDoor\ButtonCHN = PlaySound2(ScannerSFX2, Camera, d_I\ClosestButton)
				Return
			EndIf
		EndIf
		
		If d_I\ClosestDoor\Code = Str(CODE_DR_MAYNARD)
			GiveAchievement(AchvMaynard)
		ElseIf d_I\ClosestDoor\Code = CODE_DR_HARP
			GiveAchievement(AchvHarp)
		ElseIf d_I\ClosestDoor\Code = CODE_O5_COUNCIL
			GiveAchievement(AchvO5)
		EndIf
	Else
		If d_I\ClosestDoor\DoorType = WOODEN_DOOR Lor d_I\ClosestDoor\DoorType = OFFICE_DOOR
			If d_I\ClosestDoor\Locked > 0
				If SelectedItem = Null
					CreateMsg(GetLocalString("msg", "wood.wontbudge"))
					If d_I\ClosestDoor\DoorType = OFFICE_DOOR
						d_I\ClosestDoor\ButtonCHN = PlaySound2(DoorBudgeSFX1, Camera, d_I\ClosestButton)
						SetAnimTime(d_I\ClosestDoor\OBJ, 1.0)
					Else
						d_I\ClosestDoor\ButtonCHN = PlaySound2(DoorBudgeSFX2, Camera, d_I\ClosestButton)
					EndIf
				Else
					If (Temp > KEY_860) And (Temp <> KEY_005)
						CreateMsg(GetLocalString("msg", "wood.wontbudge"))
					Else
						If d_I\ClosestDoor\Locked = 2 Lor ((Temp <> d_I\ClosestDoor\KeyCard) And (Temp <> KEY_005))
							CreateMsg(GetLocalString("msg", "wood.nothappend.005"))
						Else
							CreateMsg(GetLocalString("msg", "wood.unlock"))
							d_I\ClosestDoor\Locked = 0
						EndIf
;						SelectedItem = Null
					EndIf
					If (Temp > KEY_860) And (Temp <> KEY_005)
						If d_I\ClosestDoor\DoorType = OFFICE_DOOR
							d_I\ClosestDoor\ButtonCHN = PlaySound2(DoorBudgeSFX1, Camera, d_I\ClosestButton)
							SetAnimTime(d_I\ClosestDoor\OBJ, 1.0)
						Else
							d_I\ClosestDoor\ButtonCHN = PlaySound2(DoorBudgeSFX2, Camera, d_I\ClosestButton)
						EndIf
					Else
						d_I\ClosestDoor\ButtonCHN = PlaySound2(DoorLockSFX, Camera, d_I\ClosestButton)
					EndIf
				EndIf
				Return
			Else
				If d_I\ClosestDoor\DoorType = OFFICE_DOOR
					d_I\ClosestDoor\ButtonCHN = PlaySound2(DoorBudgeSFX1, Camera, d_I\ClosestButton)
					SetAnimTime(d_I\ClosestDoor\OBJ, 1.0)
				EndIf
			EndIf
		Else
			If d_I\ClosestDoor\Locked = 1
				If d_I\ClosestDoor\DoorType = MT_ELEVATOR_DOOR
					If (Not d_I\ClosestDoor\IsElevatorDoor > 0)
						CreateMsg(GetLocalString("msg", "elev.broken"))
						d_I\ClosestDoor\ButtonCHN = PlaySound2(ButtonSFX[1], Camera, d_I\ClosestButton)
						If PlayerInsideMTElevator
							If InFacility = LowerFloor Lor (InFacility <> UpperFloor And ToMTElevatorFloor = UpperFloor)
								SetAnimTime(d_I\ClosestButton, 1.0)
							Else
								SetAnimTime(d_I\ClosestButton, 21.0)
							EndIf
						Else
							SetAnimTime(d_I\ClosestButton, 1.0)
						EndIf
						Return
					Else
						If d_I\ClosestDoor\IsElevatorDoor = 1
							CreateMsg(GetLocalString("msg", "elev.called"))
						ElseIf d_I\ClosestDoor\IsElevatorDoor = 3
							CreateMsg(GetLocalString("msg", "elev.floor"))
						ElseIf msg\Txt <> GetLocalString("msg", "elev.called")
							Select(Rand(10))
								Case 1
									;[Block]
									CreateMsg(GetLocalString("msg", "elev.stop"))
									;[End Block]
								Case 2
									;[Block]
									CreateMsg(GetLocalString("msg", "elev.faster"))
									;[End Block]
								Case 3
									;[Block]
									CreateMsg(GetLocalString("msg", "elev.mav"))
									;[End Block]
								Default
									;[Block]
									CreateMsg(GetLocalString("msg", "elev.already"))
									;[End Block]
							End Select
						Else
							CreateMsg(GetLocalString("msg", "elev.already"))
						EndIf
						d_I\ClosestDoor\ButtonCHN = PlaySound2(ButtonSFX[0], Camera, d_I\ClosestButton)
						SetAnimTime(d_I\ClosestButton, 1.0)
						Return
					EndIf
				Else
					If d_I\ClosestDoor\Open
						CreateMsg(GetLocalString("msg", "button.nothappend"))
					Else
						CreateMsg(GetLocalString("msg", "button.locked"))
					EndIf
					d_I\ClosestDoor\ButtonCHN = PlaySound2(ButtonSFX[1], Camera, d_I\ClosestButton)
					SetAnimTime(d_I\ClosestButton, 1.0)
					Return
				EndIf
			Else
				d_I\ClosestDoor\ButtonCHN = PlaySound2(ButtonSFX[0], Camera, d_I\ClosestButton)
				If PlayerInsideMTElevator
					If InFacility = LowerFloor Lor (InFacility <> UpperFloor And ToMTElevatorFloor = UpperFloor)
						SetAnimTime(d_I\ClosestButton, 1.0)
					Else
						SetAnimTime(d_I\ClosestButton, 21.0)
					EndIf
				Else
					SetAnimTime(d_I\ClosestButton, 1.0)
				EndIf
			EndIf
		EndIf
	EndIf
	
	OpenCloseDoor(d_I\ClosestDoor, PlaySFX)
End Function

Function OpenCloseDoor%(d.Doors, PlaySFX% = True, PlayCautionSFX% = False)
	d\PlayCautionSFX = PlayCautionSFX
	
	d\Open = (Not d\Open)
	If d\LinkedDoor <> Null
		d\LinkedDoor\Open = (Not d\LinkedDoor\Open)
		d\PlayCautionSFX = True
		d\LinkedDoor\PlayCautionSFX = True
	EndIf
	
	If d\Open
		If d\LinkedDoor <> Null Then d\LinkedDoor\TimerState = d\LinkedDoor\Timer
		d\TimerState = d\Timer
	EndIf
	
	Local DoorType% = d\DoorType
	
	If d\DoorType = ONE_SIDED_DOOR Lor d\DoorType = LCZ_DOOR Then DoorType = DEFAULT_DOOR
	
	If d\DoorType = MT_ELEVATOR_DOOR Then DoorType = ELEVATOR_DOOR
	
	If PlaySFX
		Local SoundRand% = Rand(0, 2)
		
		If DoorType = WOODEN_DOOR
			If PlayerRoom\RoomTemplate\RoomID = r_cont2_860_1
				SoundRand = 2
			Else
				SoundRand = Rand(0, 1)
			EndIf
		EndIf
		
		Local SoundOpen% = OpenDoorSFX(DoorType, SoundRand)
		Local SoundClose% = CloseDoorSFX(DoorType, SoundRand)
		
		If DoorType = BIG_DOOR And d\Locked = 2 Then SoundOpen = BigDoorErrorSFX[Rand(0, 2)]
		
		If d\Open
			d\SoundCHN = PlaySound2(SoundOpen, Camera, d\OBJ)
		Else
			d\SoundCHN = PlaySound2(SoundClose, Camera, d\OBJ)
		EndIf
	EndIf
End Function

Function RemoveDoor%(d.Doors)
	Local i%
	
	FreeEntity(d\OBJ) : d\OBJ = 0
	If d\OBJ2 <> 0 Then FreeEntity(d\OBJ2) : d\OBJ2 = 0
	For i = 0 To 1
		If d\Buttons[i] <> 0 Then FreeEntity(d\Buttons[i]) : d\Buttons[i] = 0
		If d\ElevatorPanel[i] <> 0 Then FreeEntity(d\ElevatorPanel[i]) : d\ElevatorPanel[i] = 0
	Next
	FreeEntity(d\FrameOBJ) : d\FrameOBJ = 0
	Delete(d)
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D TSS