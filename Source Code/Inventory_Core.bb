
; ~ Inventory Constants
;[Block]
Const MaxInventorySpace% = 38
Const MaxWeightDefault# = 60.0
Const Inv_Slot_Head% = 9, Inv_Slot_Torso% = 10, Inv_Slot_Gatget% = 11, Inv_Slot_Backpack% = 12
Const Inv_Slot_Primary% = 13, Inv_Slot_Secondary% = 14, Inv_Slot_Holster% = 15, Inv_Slot_Scabbard% = 16
;[End Block]

Global ClickedDescription%

;[Block]
Function RenderInventory%()
	Local PrevOtherOpen.Items
	Local OtherSize%, OtherAmount%
	Local IsEmpty%
	Local IsMouseOn%
	Local ClosedInv%
	Local INVENTORY_GFX_WIDTH% = 70 * MenuScale
	Local INVENTORY_GFX_HEIGHT% = 70 * MenuScale
	Local INVENTORY_GFX_SPACING% = 35 * MenuScale
	Local InvImgSize% = (64 * MenuScale) / 2
	Local i%, n%, z%, x%, y%, MouseSlot% = 66
	Local InvX%, InvY%, InvYHeight%, InvLocked% = False, InvColorR#, InvColorG#, InvColorB#
	Local InvImgWidth%, InvImgHeight%
	Local FrameX%, FrameY%
	
	If OtherOpen <> Null
		PrevOtherOpen = OtherOpen
		OtherSize = OtherOpen\InvSlots
		
		For i = 0 To OtherSize - 1
			If OtherOpen\SecondInv[i] <> Null Then OtherAmount = OtherAmount + 1
		Next
		
		Local TempX% = 0
		
		x = mo\Viewport_Center_X - ((INVENTORY_GFX_WIDTH * 10 / 2) + (INVENTORY_GFX_SPACING * ((10 / 2) - 1))) / 2
		y = mo\Viewport_Center_Y - (INVENTORY_GFX_HEIGHT * ((OtherSize / 10 * 2) - 1)) - INVENTORY_GFX_SPACING
		
		IsMouseOn = -1
		For n = 0 To OtherSize - 1
			If MouseOn(x, y, INVENTORY_GFX_WIDTH, INVENTORY_GFX_HEIGHT) Then IsMouseOn = n
			
			If IsMouseOn = n
				MouseSlot = n
				Color(255, 0, 0)
				Rect(x - 1, y - 1, INVENTORY_GFX_WIDTH + (2 * MenuScale), INVENTORY_GFX_HEIGHT + (2 * MenuScale))
			EndIf
			
			RenderFrame(x, y, INVENTORY_GFX_WIDTH, INVENTORY_GFX_HEIGHT)
			
			If OtherOpen = Null Then Exit
			
			If OtherOpen\SecondInv[n] <> Null
				If (IsMouseOn = n Lor SelectedItem <> OtherOpen\SecondInv[n]) Then DrawBlock(OtherOpen\SecondInv[n]\InvImg, x + (INVENTORY_GFX_WIDTH / 2) - (32 * MenuScale), y + (INVENTORY_GFX_HEIGHT / 2) - (32 * MenuScale))
			EndIf
			If OtherOpen\SecondInv[n] <> Null And SelectedItem <> OtherOpen\SecondInv[n]
				If IsMouseOn = n
					Color(255, 255, 255)
					TextEx(x + (INVENTORY_GFX_WIDTH / 2), y + INVENTORY_GFX_HEIGHT + INVENTORY_GFX_SPACING - (15 * MenuScale), OtherOpen\SecondInv[n]\ItemTemplate\DisplayName, True)
				EndIf
			EndIf
			
			x = x + INVENTORY_GFX_WIDTH + INVENTORY_GFX_SPACING
			TempX = TempX + 1
			If TempX = 5
				TempX = 0
				y = y + (INVENTORY_GFX_HEIGHT * 2)
				x = mo\Viewport_Center_X - ((INVENTORY_GFX_WIDTH * 10 / 2) + (INVENTORY_GFX_SPACING * ((10 / 2) - 1))) / 2
			EndIf
		Next
		
		If SelectedItem <> Null
			If mo\MouseDown1
				;If SelectedItem\ItemTemplate\SoundID <> 66 Then PlaySound_Strict(PickSFX[SelectedItem\ItemTemplate\SoundID])
				If MouseSlot = 66
					DrawBlock(SelectedItem\InvImg, MousePosX - InvImgSize, MousePosY - InvImgSize)
				ElseIf SelectedItem <> PrevOtherOpen\SecondInv[MouseSlot]
					DrawBlock(SelectedItem\InvImg, MousePosX - InvImgSize, MousePosY - InvImgSize)
				EndIf
			EndIf
		EndIf
		
		RenderCursor()
	ElseIf InvOpen
		
		INVENTORY_GFX_SPACING% = 1 * MenuScale
		
		x = mo\Viewport_Center_X - ((INVENTORY_GFX_WIDTH * 9 / 3) + (INVENTORY_GFX_SPACING * (9 / 3 - 1))) / 2
		y = mo\Viewport_Center_Y - INVENTORY_GFX_HEIGHT - INVENTORY_GFX_SPACING
		
		If wi\Backpack
			InvYHeight = y * 0.3
		Else
			InvYHeight = y
		EndIf
		
		DrawImage(t\IconID[Icon_Inventory], mo\Viewport_Center_X, mo\Viewport_Center_Y)
		
		SetFont(fo\FontID[Font_Default_Medium])
		TextEx(x + (INVENTORY_GFX_WIDTH * 6.0) + (INVENTORY_GFX_WIDTH / 3), InvYHeight - (INVENTORY_GFX_HEIGHT / 2), GetLocalString("inventory", "name"))
;		If wi\BallisticVest Then TextEx(x + (INVENTORY_GFX_WIDTH * 6.0) + (INVENTORY_GFX_WIDTH / 3), y - (INVENTORY_GFX_HEIGHT / 6), GetLocalString("inventory", "slot.vest"))
		If wi\Backpack Then TextEx(x + (INVENTORY_GFX_WIDTH * 6.0) + (INVENTORY_GFX_WIDTH / 3), InvYHeight + (INVENTORY_GFX_HEIGHT * 3.5), GetLocalString("inventory", "slot.backpack"))
		TextEx(x - (INVENTORY_GFX_WIDTH * 7.5) + (INVENTORY_GFX_WIDTH / 3), y - (INVENTORY_GFX_HEIGHT * 2.5), GetLocalString("inventory", "slot.primary"))
		TextEx(x - (INVENTORY_GFX_WIDTH * 7.5) + (INVENTORY_GFX_WIDTH / 3), y + (INVENTORY_GFX_HEIGHT / 3), GetLocalString("inventory", "slot.secondary"))
		TextEx(x - (INVENTORY_GFX_WIDTH * 7.5) + (INVENTORY_GFX_WIDTH / 3), y + (INVENTORY_GFX_HEIGHT * 3.6), GetLocalString("inventory", "slot.holster"))
		TextEx(x - (INVENTORY_GFX_WIDTH * 2.75) + (INVENTORY_GFX_WIDTH / 3), y + (INVENTORY_GFX_HEIGHT * 3.6), GetLocalString("inventory", "slot.scabbard"))
		TextEx(x - (INVENTORY_GFX_WIDTH / 2) + (INVENTORY_GFX_WIDTH / 2), y - (INVENTORY_GFX_HEIGHT * 2), GetLocalString("inventory", "slot.head"))
		TextEx(x - (INVENTORY_GFX_WIDTH / 2) + (INVENTORY_GFX_WIDTH / 2), y + (INVENTORY_GFX_HEIGHT / 2), GetLocalString("inventory", "slot.torso"))
		TextEx(x + (INVENTORY_GFX_WIDTH * 2) + (INVENTORY_GFX_WIDTH / 2), y - (INVENTORY_GFX_HEIGHT * 2), GetLocalString("inventory", "slot.gatget"))
		TextEx(x + (INVENTORY_GFX_WIDTH * 2) + (INVENTORY_GFX_WIDTH / 2), y + (INVENTORY_GFX_HEIGHT / 2), GetLocalString("inventory", "slot.backpack"))
		SetFont(fo\FontID[Font_Default])
		
		IsMouseOn = -1
		For n = 0 To MaxItemAmount - 1
			
			If n < Inv_Slot_Head
				InvX = x + INVENTORY_GFX_WIDTH * 6.0
				InvY = InvYHeight
			ElseIf n > Inv_Slot_Scabbard
				InvX = x + INVENTORY_GFX_WIDTH * 6.0
				InvY = InvYHeight
			ElseIf n = Inv_Slot_Head
				InvX = x - INVENTORY_GFX_WIDTH * 3.0
				InvY = y - INVENTORY_GFX_HEIGHT * 3.5
			ElseIf n = Inv_Slot_Torso
				InvY = y - INVENTORY_GFX_HEIGHT
			ElseIf n = Inv_Slot_Gatget
				InvX = x - INVENTORY_GFX_WIDTH * 2.0
				InvY = y - INVENTORY_GFX_HEIGHT * 3.5
			ElseIf n = Inv_Slot_Backpack
				InvY = y - INVENTORY_GFX_HEIGHT
			ElseIf n = Inv_Slot_Primary
				INVENTORY_GFX_WIDTH = 400 * MenuScale
				INVENTORY_GFX_HEIGHT = 140 * MenuScale
				InvX = x - INVENTORY_GFX_WIDTH * 2.5
				InvY = y - INVENTORY_GFX_HEIGHT * 2.0
			ElseIf n = Inv_Slot_Secondary
				InvY = y - INVENTORY_GFX_HEIGHT / 2.0
			ElseIf n = Inv_Slot_Holster
				INVENTORY_GFX_WIDTH = 70 * MenuScale
				INVENTORY_GFX_HEIGHT = 70 * MenuScale
				InvY = y + INVENTORY_GFX_HEIGHT * 2.0
			ElseIf n = Inv_Slot_Scabbard
				InvX = InvX + INVENTORY_GFX_HEIGHT * 4.7
			EndIf
			
			If MouseOn(InvX, InvY, INVENTORY_GFX_WIDTH, INVENTORY_GFX_HEIGHT) Then IsMouseOn = n
			
			If Inventory(n) <> Null
				Local ShouldDrawRect%
				
				Color(0.0, 200.0, 0.0)
				Select(Inventory(n)\ItemTemplate\TempName)
					Case "gasmask"
						;[Block]
						ShouldDrawRect = (wi\GasMask = 1)
						;[End Block]
					Case "gasmask2"
						;[Block]
						ShouldDrawRect = (wi\GasMask = 2)
						;[End Block]
					Case "gasmask3"
						;[Block]
						ShouldDrawRect = (wi\GasMask = 3)
						;[End Block]
					Case "gasmask4"
						;[Block]
						ShouldDrawRect = (wi\GasMask = 4)
						;[End Block]
					Case "nvg"
						;[Block]
						ShouldDrawRect = (wi\NightVision = 1)
						;[End Block]
					Case "nvg3"
						;[Block]
						ShouldDrawRect = (wi\NightVision = 2)
						;[End Block]
					Case "nvg2"
						;[Block]
						ShouldDrawRect = (wi\NightVision = 3)
						;[End Block]
					Case "scramble"
						;[Block]
						ShouldDrawRect = (wi\SCRAMBLE = 1)
						;[End Block]
					Case "scramble2"
						;[Block]
						ShouldDrawRect = (wi\SCRAMBLE = 2)
						;[End Block]
					Case "helmet"
						;[Block]
						ShouldDrawRect = wi\BallisticHelmet
						;[End Block]
					Case "scp268fake"
						;[Block]
						ShouldDrawRect = (I_268\Using = 1)
						;[End Block]
					Case "scp268"
						;[Block]
						ShouldDrawRect = (I_268\Using = 2)
						;[End Block]
					Case "scp268fine"
						;[Block]
						ShouldDrawRect = (I_268\Using = 3)
						;[End Block]
					Case "vest"
						;[Block]
						ShouldDrawRect = (wi\BallisticVest = 1)
						;[End Block]
					Case "vest2"
						;[Block]
						ShouldDrawRect = (wi\BallisticVest = 2)
						;[End Block]
					Case "hazmat"
						;[Block]
						ShouldDrawRect = (wi\HazmatSuit = 1)
						;[End Block]
					Case "hazmat2"
						;[Block]
						ShouldDrawRect = (wi\HazmatSuit = 2)
						;[End Block]
					Case "hazmat3"
						;[Block]
						ShouldDrawRect = (wi\HazmatSuit = 3)
						;[End Block]
					Case "hazmat4"
						;[Block]"
						ShouldDrawRect = (wi\HazmatSuit = 4)
						;[End Block]
					Case "scp427"
						;[Block]
						ShouldDrawRect = (I_427\Using)
						;[End Block]
					Case "scp714"
						;[Block]
						ShouldDrawRect = (I_714\Using = 2)
						;[End Block]
					Case "coarse714"
						;[Block]
						ShouldDrawRect = (I_714\Using = 1)
						;[End Block]
					Default
						;[Block]
						ShouldDrawRect = False
						;[End Block]
				End Select
				If n > Inv_Slot_Backpack And n <= Inv_Slot_Scabbard
					If Inventory(n)\ItemTemplate\IsWeapon Then ShouldDrawRect = True
				EndIf
				If ShouldDrawRect Then Rect(InvX - (3 * MenuScale), InvY - (3 * MenuScale), INVENTORY_GFX_WIDTH + (6 * MenuScale), INVENTORY_GFX_HEIGHT + (6 * MenuScale))
			EndIf
			
			If IsMouseOn = n
				MouseSlot = n
				Color(200.0, 150.0, 0.0)
				Rect(InvX - 1, InvY - 1, INVENTORY_GFX_WIDTH + (2 * MenuScale), INVENTORY_GFX_HEIGHT + (2 * MenuScale))
				If mo\MouseHit2 Then ClickedDescription = (Not ClickedDescription)
			EndIf
			
			If InvLocked
				InvColorR = 150.0
				InvColorG = 0.0
				InvColorB = 0.0
			Else
				InvColorR = 5.0
				InvColorG = 5.0
				InvColorB = 5.0
			EndIf
			
			Color(255, 255, 255)
			RenderFrame(InvX, InvY, INVENTORY_GFX_WIDTH, INVENTORY_GFX_HEIGHT, False, InvColorR, InvColorG, InvColorB, 30.0, 30.0, 30.0)
			
			Local InvItemImage%
			
			If Inventory(n) <> Null
				If (n = Inv_Slot_Primary Lor n = Inv_Slot_Secondary) And Inventory(n)\InvImg2 <> ""
					InvItemImage = Inventory(n)\InvImg2
					InvImgWidth = 0;22
					InvImgHeight = 0;1
				Else
					InvItemImage = Inventory(n)\InvImg
					InvImgWidth = 5
					InvImgHeight = 5
				EndIf
				If IsMouseOn = n Lor SelectedItem <> Inventory(n) Then DrawBlock(InvItemImage, InvX + (INVENTORY_GFX_WIDTH / 2) - (((INVENTORY_GFX_WIDTH / 2) - InvImgWidth) * MenuScale), InvY + (INVENTORY_GFX_HEIGHT / 2) - (((INVENTORY_GFX_HEIGHT / 2) - InvImgHeight) * MenuScale))
			EndIf
			
			FrameX = mo\Viewport_Center_X - (INVENTORY_GFX_WIDTH / 2)
			If wi\Backpack
				FrameY = mo\Viewport_Center_Y + (INVENTORY_GFX_HEIGHT * 3)
			Else
				FrameY = mo\Viewport_Center_Y - (INVENTORY_GFX_HEIGHT / 2)
			EndIf
			
			TextEx(FrameX + (INVENTORY_GFX_WIDTH / 2) + (INVENTORY_GFX_WIDTH * 6), (FrameY + INVENTORY_GFX_HEIGHT + INVENTORY_GFX_SPACING - 15) + (INVENTORY_GFX_HEIGHT * 2), Format(GetLocalString("inventory", "weight.current"), CarriableWeight) + GetLocalString("inventory", "weight.kilos") + " / (" + MaxCarriableWeight + ")" + GetLocalString("inventory", "weight.kilos"), True)
			
			If Inventory(n) <> Null And SelectedItem <> Inventory(n)
				If IsMouseOn = n
					If SelectedItem = Null
						RenderFrame(FrameX + (INVENTORY_GFX_WIDTH * 5), FrameY + (INVENTORY_GFX_HEIGHT * 3.5), (INVENTORY_GFX_HEIGHT * 3), (INVENTORY_GFX_HEIGHT / 2), False, InvColorR, InvColorG, InvColorB, 30.0, 30.0, 30.0)
						SetFontEx(fo\FontID[Font_Default])
						Color(255, 255, 255)
						TextEx(FrameX + (INVENTORY_GFX_WIDTH * 6.5), (FrameY + INVENTORY_GFX_HEIGHT + INVENTORY_GFX_SPACING - 15) + (INVENTORY_GFX_HEIGHT * 2.9), Inventory(n)\DisplayName, True, True)
					EndIf
				EndIf
			EndIf
			
			x = x + INVENTORY_GFX_WIDTH + INVENTORY_GFX_SPACING
			If n = 2 Lor n = 5
				y = y + (INVENTORY_GFX_HEIGHT + INVENTORY_GFX_SPACING)
				InvYHeight = InvYHeight + (INVENTORY_GFX_HEIGHT + INVENTORY_GFX_SPACING)
				x = mo\Viewport_Center_X - ((INVENTORY_GFX_WIDTH * 9 / 3) + (INVENTORY_GFX_SPACING * (9 / 3 - 1))) / 2
			ElseIf n = 16 Lor n = 19
				InvYHeight = y - ((INVENTORY_GFX_HEIGHT + INVENTORY_GFX_SPACING) * 2.5)
				y = y + (INVENTORY_GFX_HEIGHT + INVENTORY_GFX_SPACING)
				x = mo\Viewport_Center_X - ((INVENTORY_GFX_WIDTH * 9 / 3) + (INVENTORY_GFX_SPACING * (9 / 3 - 1))) / 2
			ElseIf n = 22 Lor n = 25 Lor n = 28 Lor n = 31 Lor n = 34 Lor n = 37
				y = y + (INVENTORY_GFX_HEIGHT + INVENTORY_GFX_SPACING)
				InvYHeight = InvYHeight + (INVENTORY_GFX_HEIGHT + INVENTORY_GFX_SPACING)
				x = mo\Viewport_Center_X - ((INVENTORY_GFX_WIDTH * 9 / 3) + (INVENTORY_GFX_SPACING * (9 / 3 - 1))) / 2
			EndIf
		Next
		
;		If ClickedDescription
;			RenderFrame(mo\Viewport_Center_X, mo\Viewport_Center_Y, 512, 256)
;		EndIf
		
		If SelectedItem <> Null
			If mo\MouseDown1
				;If SelectedItem\ItemTemplate\SoundID <> 66 Then PlaySound_Strict(PickSFX[SelectedItem\ItemTemplate\SoundID])
				If MouseSlot = 66
					DrawBlock(SelectedItem\InvImg, MousePosX - InvImgSize, MousePosY - InvImgSize)
				ElseIf SelectedItem <> Inventory(MouseSlot)
					DrawBlock(SelectedItem\InvImg, MousePosX - InvImgSize, MousePosY - InvImgSize)
				EndIf
			EndIf
		EndIf
		
		RenderCursor()
	EndIf
	
End Function
;[End Block]

;[Block]
Function UpdateInventory%()
	Local PrevOtherOpen.Items, PrevItem.Items
	Local OtherSize%, OtherAmount%
	Local IsEmpty%
	Local IsMouseOn%
	Local ClosedInv%
	Local INVENTORY_GFX_WIDTH% = 70 * MenuScale
	Local INVENTORY_GFX_HEIGHT% = 70 * MenuScale
	Local INVENTORY_GFX_SPACING% = 35 * MenuScale
	Local i%, n%, z%, x%, y%, MouseSlot% = 66
	Local InvX%, InvY%, InvYHeight%, InvLocked% = False, InvColorR#, InvColorG#, InvColorB#
	
	;[Block]
	UpdateInventorySlots()
	;[End Block]
	
	If OtherOpen <> Null
		PrevOtherOpen = OtherOpen
		OtherSize = OtherOpen\InvSlots
		
		For i = 0 To OtherSize - 1
			If OtherOpen\SecondInv[i] <> Null Then OtherAmount = OtherAmount + 1
		Next
		
		InvOpen = False
		d_I\SelectedDoor = Null
		
		Local TempX% = 0
		
		x = mo\Viewport_Center_X - ((INVENTORY_GFX_WIDTH * 10 / 2) + (INVENTORY_GFX_SPACING * ((10 / 2) - 1))) / 2
		y = mo\Viewport_Center_Y - (INVENTORY_GFX_HEIGHT * ((OtherSize / 10 * 2) - 1)) - INVENTORY_GFX_SPACING
		
		ItemAmount = 0
		IsMouseOn = -1
		For n = 0 To OtherSize - 1
			If MouseOn(x, y, INVENTORY_GFX_WIDTH, INVENTORY_GFX_HEIGHT) Then IsMouseOn = n
			
			If IsMouseOn = n Then MouseSlot = n
			If OtherOpen = Null Then Exit
			
			If OtherOpen\SecondInv[n] <> Null And SelectedItem <> OtherOpen\SecondInv[n]
				If IsMouseOn = n
					If SelectedItem = Null
						If mo\MouseHit1
							SelectedItem = OtherOpen\SecondInv[n]
							
							If mo\DoubleClick And mo\DoubleClickSlot = n
								If SelectedItem\ItemTemplate\TempName = "scp714" Lor SelectedItem\ItemTemplate\TempName = "coarse714" Lor SelectedItem\ItemTemplate\TempName = "fine714" Lor SelectedItem\ItemTemplate\TempName = "ring"
									CreateMsg(GetLocalString("msg", "wallet.714"))
									SelectedItem = Null
									Return
								EndIf
								If OtherOpen\SecondInv[n]\ItemTemplate\SoundID <> 66 Then PlaySound_Strict(PickSFX[OtherOpen\SecondInv[n]\ItemTemplate\SoundID])
								OtherOpen = Null
								ClosedInv = True
								InvOpen = False
								mo\DoubleClick = False
							EndIf
						EndIf
					EndIf
				EndIf
				ItemAmount = ItemAmount + 1
			Else
				If IsMouseOn = n And mo\MouseHit1
					For z = 0 To OtherSize - 1
						If OtherOpen\SecondInv[z] = SelectedItem
							OtherOpen\SecondInv[z] = Null
							Exit
						EndIf
					Next
					OtherOpen\SecondInv[n] = SelectedItem
					SelectedItem = Null
				EndIf
			EndIf
			
			x = x + INVENTORY_GFX_WIDTH + INVENTORY_GFX_SPACING
			TempX = TempX + 1
			If TempX = 5
				TempX = 0
				y = y + (INVENTORY_GFX_HEIGHT * 2)
				x = mo\Viewport_Center_X - ((INVENTORY_GFX_WIDTH * 10 / 2) + (INVENTORY_GFX_SPACING * ((10 / 2) - 1))) / 2
			EndIf
		Next
		
		If mo\MouseHit1 Then mo\DoubleClickSlot = IsMouseOn
		
		If SelectedItem <> Null
			If (Not mo\MouseDown1) Lor mo\MouseHit2
				If MouseSlot = 66
					Local CameraYaw# = EntityYaw(Camera)
					
					If SelectedItem\ItemTemplate\SoundID <> 66 Then PlaySound_Strict(PickSFX[SelectedItem\ItemTemplate\SoundID])
					ShowEntity(SelectedItem\Collider)
					PositionEntity(SelectedItem\Collider, EntityX(Camera), EntityY(Camera), EntityZ(Camera))
					RotateEntity(SelectedItem\Collider, EntityPitch(Camera), CameraYaw + Rnd(-20.0, 20.0), 0.0)
					MoveEntity(SelectedItem\Collider, 0.0, -0.1, 0.1)
					RotateEntity(SelectedItem\Collider, 0.0, CameraYaw + Rnd(-110.0, 110.0), 0.0)
					ResetEntity(SelectedItem\Collider)
					SelectedItem\Dropped = 1
					SelectedItem\Picked = False
					For z = 0 To OtherSize - 1
						If OtherOpen\SecondInv[z] = SelectedItem
							OtherOpen\SecondInv[z] = Null
							Exit
						EndIf
					Next
					
					IsEmpty = True
					If OtherOpen\ItemTemplate\TempName = "wallet"
						If (Not IsEmpty)
							For z = 0 To OtherSize - 1
								If OtherOpen\SecondInv[z] <> Null
									Local Name$ = OtherOpen\SecondInv[z]\ItemTemplate\TempName
									
									If Name <> "25ct" And Name <> "coin" And Name <> "key" And Name <> "scp860" And Name <> "scp714" And Name <> "coarse714" And Name <> "fine714" And Name <> "ring" And Name <> "scp500pill" And Name <> "scp500pilldeath" And Name <> "pill"
										IsEmpty = False
										Exit
									EndIf
								EndIf
							Next
						EndIf
					Else
						For z = 0 To OtherSize - 1
							If OtherOpen\SecondInv[z] <> Null
								IsEmpty = False
								Exit
							EndIf
						Next
					EndIf
					
					If IsEmpty
						If OtherOpen\ItemTemplate\TempName = "clipboard"
							OtherOpen\InvImg = OtherOpen\ItemTemplate\InvImg2
							SetAnimTime(OtherOpen\Model, 17.0)
						ElseIf OtherOpen\ItemTemplate\TempName = "wallet"
							SetAnimTime(OtherOpen\Model, 0.0)
						EndIf
					EndIf
					
					SelectedItem = Null
					
					If (Not mo\MouseHit2)
						OtherOpen = Null
						ClosedInv = True
						MoveMouse(mo\Viewport_Center_X, mo\Viewport_Center_Y)
					EndIf
				Else
					If PrevOtherOpen\SecondInv[MouseSlot] = Null
						For z = 0 To OtherSize - 1
							If PrevOtherOpen\SecondInv[z] = SelectedItem
								PrevOtherOpen\SecondInv[z] = Null
								Exit
							EndIf
						Next
						PrevOtherOpen\SecondInv[MouseSlot] = SelectedItem
						SelectedItem = Null
					ElseIf PrevOtherOpen\SecondInv[MouseSlot] <> SelectedItem
						PrevItem = PrevOtherOpen\SecondInv[MouseSlot]
						
						Select(SelectedItem\ItemTemplate\TempName)
							Default
								;[Block]
								For z = 0 To OtherSize - 1
									If PrevOtherOpen\SecondInv[z] = SelectedItem
										PrevOtherOpen\SecondInv[z] = PrevItem
										Exit
									EndIf
								Next
								PrevOtherOpen\SecondInv[MouseSlot] = SelectedItem
								SelectedItem = Null
								;[End Block]
						End Select
					EndIf
				EndIf
				SelectedItem = Null
			EndIf
		EndIf
		
		If ClosedInv And (Not InvOpen)
			OtherOpen = Null
			StopMouseMovement()
		EndIf
	ElseIf InvOpen
		d_I\SelectedDoor = Null
		
		INVENTORY_GFX_SPACING% = 1 * MenuScale
		
		x = mo\Viewport_Center_X - ((INVENTORY_GFX_WIDTH * 9 / 3) + (INVENTORY_GFX_SPACING * (9 / 3 - 1))) / 2
		y = mo\Viewport_Center_Y - INVENTORY_GFX_HEIGHT - INVENTORY_GFX_SPACING
		
		If wi\Backpack
			InvYHeight = y * 0.3
		Else
			InvYHeight = y
		EndIf
		
		ItemAmount = 0
		IsMouseOn = -1
		For n = 0 To MaxItemAmount - 1
			
			If n < Inv_Slot_Head
				InvX = x + INVENTORY_GFX_WIDTH * 6.0
				InvY = InvYHeight
			ElseIf n > Inv_Slot_Scabbard
				InvX = x + INVENTORY_GFX_WIDTH * 6.0
				InvY = InvYHeight
			ElseIf n = Inv_Slot_Head
				InvX = x - INVENTORY_GFX_WIDTH * 3.0
				InvY = y - INVENTORY_GFX_HEIGHT * 3.5
			ElseIf n = Inv_Slot_Torso
				InvY = y - INVENTORY_GFX_HEIGHT
			ElseIf n = Inv_Slot_Gatget
				InvX = x - INVENTORY_GFX_WIDTH * 2.0
				InvY = y - INVENTORY_GFX_HEIGHT * 3.5
			ElseIf n = Inv_Slot_Backpack
				InvY = y - INVENTORY_GFX_HEIGHT
			ElseIf n = Inv_Slot_Primary
				INVENTORY_GFX_WIDTH = 400 * MenuScale
				INVENTORY_GFX_HEIGHT = 140 * MenuScale
				InvX = x - INVENTORY_GFX_WIDTH * 2.5
				InvY = y - INVENTORY_GFX_HEIGHT * 2.0
			ElseIf n = Inv_Slot_Secondary
				InvY = y - INVENTORY_GFX_HEIGHT / 2.0
			ElseIf n = Inv_Slot_Holster
				INVENTORY_GFX_WIDTH = 70 * MenuScale
				INVENTORY_GFX_HEIGHT = 70 * MenuScale
				InvY = y + INVENTORY_GFX_HEIGHT * 2.0
			ElseIf n = Inv_Slot_Scabbard
				InvX = InvX + INVENTORY_GFX_HEIGHT * 4.7
			EndIf
			
			If MouseOn(InvX, InvY, INVENTORY_GFX_WIDTH, INVENTORY_GFX_HEIGHT) Then IsMouseOn = n
			
			If IsMouseOn = n Then MouseSlot = n
			
			If Inventory(n) <> Null And SelectedItem <> Inventory(n)
				If IsMouseOn = n
					If SelectedItem = Null
						If mo\MouseHit1
							SelectedItem = Inventory(n)
							If mo\DoubleClick And mo\DoubleClickSlot = n
								If Inventory(n)\ItemTemplate\SoundID <> 66 Then PlaySound_Strict(PickSFX[Inventory(n)\ItemTemplate\SoundID])
								InvOpen = False
								mo\DoubleClick = False
							EndIf
						EndIf
					EndIf
				EndIf
				ItemAmount = ItemAmount + 1
			Else
				If IsMouseOn = n And mo\MouseHit1
					For z = 0 To MaxItemAmount - 1
						If Inventory(z) = SelectedItem
							Inventory(z) = Null
							Exit
						EndIf
					Next
					Inventory(n) = SelectedItem
					SelectedItem = Null
				EndIf
			EndIf
			
			x = x + INVENTORY_GFX_WIDTH + INVENTORY_GFX_SPACING
			If n = 2 Lor n = 5
				y = y + (INVENTORY_GFX_HEIGHT + INVENTORY_GFX_SPACING)
				InvYHeight = InvYHeight + (INVENTORY_GFX_HEIGHT + INVENTORY_GFX_SPACING)
				x = mo\Viewport_Center_X - ((INVENTORY_GFX_WIDTH * 9 / 3) + (INVENTORY_GFX_SPACING * (9 / 3 - 1))) / 2
			ElseIf n = 16 Lor n = 19
				InvYHeight = y - ((INVENTORY_GFX_HEIGHT + INVENTORY_GFX_SPACING) * 2.5)
				y = y + (INVENTORY_GFX_HEIGHT + INVENTORY_GFX_SPACING)
				x = mo\Viewport_Center_X - ((INVENTORY_GFX_WIDTH * 9 / 3) + (INVENTORY_GFX_SPACING * (9 / 3 - 1))) / 2
			ElseIf n = 22 Lor n = 25 Lor n = 28 Lor n = 31 Lor n = 34 Lor n = 37
				y = y + (INVENTORY_GFX_HEIGHT + INVENTORY_GFX_SPACING)
				InvYHeight = InvYHeight + (INVENTORY_GFX_HEIGHT + INVENTORY_GFX_SPACING)
				x = mo\Viewport_Center_X - ((INVENTORY_GFX_WIDTH * 9 / 3) + (INVENTORY_GFX_SPACING * (9 / 3 - 1))) / 2
			EndIf
		Next
		
		If mo\MouseHit1 Then mo\DoubleClickSlot = IsMouseOn
		
		If SelectedItem <> Null
			If (Not mo\MouseDown1) Lor mo\MouseHit2
				If MouseSlot = 66
					Local ShouldPreventDropping%
					
					Select(SelectedItem\ItemTemplate\TempName)
						Case "scp714"
							;[Block]
							ShouldPreventDropping = (I_714\Using = 2)
							;[End Block]
						Case "coarse714"
							;[Block]
							ShouldPreventDropping = (I_714\Using = 1)
							;[End Block]
						Case "scp427"
							;[Block]
							ShouldPreventDropping = I_427\Using
							;[End Block]
						Default
							;[Block]
							ShouldPreventDropping = False
							;[End Block]
					End Select
					If ShouldPreventDropping
						CreateHintMsg(GetLocalString("msg", "takeoff"))
					Else
						DropItem(SelectedItem)
;						InvOpen = mo\MouseHit2
					EndIf
					
					If (Not mo\MouseHit2)
						MoveMouse(mo\Viewport_Center_X, mo\Viewport_Center_Y)
						StopMouseMovement()
					EndIf
				Else
					If Inventory(MouseSlot) = Null
						
						; ~ Head Slot's Logic
						If MouseSlot = Inv_Slot_Head And SelectedItem\ItemTemplate\IsHead And (Not Inventory(Inv_Slot_Torso) <> Null And Inventory(Inv_Slot_Torso)\ItemTemplate\IsFullBody)
							PlaySound_Strict(wep_I\SelectSFX)
							For z% = 0 To MaxItemAmount - 1
								If Inventory(z) = SelectedItem Then Inventory(z) = Null : Exit
							Next
							Inventory(MouseSlot) = SelectedItem
							SelectedItem = Null
						ElseIf MouseSlot = Inv_Slot_Head And (Not SelectedItem\ItemTemplate\IsHead)
							PlaySound_Strict(wep_I\DenySFX)
						ElseIf MouseSlot = Inv_Slot_Head And SelectedItem\ItemTemplate\IsHead And Inventory(Inv_Slot_Torso) <> Null And Inventory(Inv_Slot_Torso)\ItemTemplate\IsFullBody
							PlaySound_Strict(wep_I\DenySFX)
							CreateHintMsg(GetFileLocalString("inventory", "remove_torso",Inventory(Inv_Slot_Torso)\ItemTemplate\Name))
						; ~ Torso Slot's Logic
						ElseIf MouseSlot = Inv_Slot_Torso And SelectedItem\ItemTemplate\IsTorso And (Not SelectedItem\ItemTemplate\IsFullBody And Inventory(Inv_Slot_Head) <> Null)
							PlaySound_Strict(wep_I\SelectSFX)
							For z% = 0 To MaxItemAmount - 1
								If Inventory(z) = SelectedItem Then Inventory(z) = Null : Exit
							Next
							Inventory(MouseSlot) = SelectedItem
							SelectedItem = Null
						ElseIf MouseSlot = Inv_Slot_Torso And (Not SelectedItem\ItemTemplate\IsTorso)
							PlaySound_Strict(wep_I\DenySFX)
						ElseIf MouseSlot = Inv_Slot_Torso And SelectedItem\ItemTemplate\IsTorso And SelectedItem\ItemTemplate\IsFullBody And Inventory(Inv_Slot_Head) <> Null
							PlaySound_Strict(wep_I\DenySFX)
							CreateHintMsg(GetFileLocalString("inventory", "remove_head",Inventory(Inv_Slot_Head)\ItemTemplate\Name))
						; ~ Gatget Slot's Logic
						ElseIf MouseSlot = Inv_Slot_Gatget And SelectedItem\ItemTemplate\IsGatget
							PlaySound_Strict(wep_I\SelectSFX)
							For z% = 0 To MaxItemAmount - 1
								If Inventory(z) = SelectedItem Then Inventory(z) = Null : Exit
							Next
							Inventory(MouseSlot) = SelectedItem
							SelectedItem = Null
						ElseIf MouseSlot = Inv_Slot_Gatget And (Not SelectedItem\ItemTemplate\IsGatget)
							PlaySound_Strict(wep_I\DenySFX)
						; ~ Backpack Slot's Logic	
						ElseIf MouseSlot = Inv_Slot_Backpack And SelectedItem\ItemTemplate\IsBackPack
							PlaySound_Strict(wep_I\SelectSFX)
							For z% = 0 To MaxItemAmount - 1
								If Inventory(z) = SelectedItem Then Inventory(z) = Null : Exit
							Next
							Inventory(MouseSlot) = SelectedItem
							SelectedItem = Null
						ElseIf MouseSlot = Inv_Slot_Backpack And (Not SelectedItem\ItemTemplate\IsBackPack)
							PlaySound_Strict(wep_I\DenySFX)
						; ~ Primary Weapon Slot's Logic	
						ElseIf MouseSlot = Inv_Slot_Primary And SelectedItem\ItemTemplate\IsWeapon > Unarmed
							PlaySound_Strict(wep_I\SelectSFX)
							For z% = 0 To MaxItemAmount - 1
								If Inventory(z) = SelectedItem Then Inventory(z) = Null : Exit
							Next
							wep_I\CurrentWeaponSlot[QuickSlot_Primary] = SelectedItem\ItemTemplate\IsWeapon
							Inventory(MouseSlot) = SelectedItem
							SelectedItem = Null
						ElseIf MouseSlot = Inv_Slot_Primary And (Not SelectedItem\ItemTemplate\IsWeapon)
							PlaySound_Strict(wep_I\DenySFX)
						; ~ Secondary Weapon Slot's Logic	
						ElseIf MouseSlot = Inv_Slot_Secondary And SelectedItem\ItemTemplate\IsWeapon > Unarmed
							PlaySound_Strict(wep_I\SelectSFX)
							For z% = 0 To MaxItemAmount - 1
								If Inventory(z) = SelectedItem Then Inventory(z) = Null : Exit
							Next
							wep_I\CurrentWeaponSlot[QuickSlot_Secondary] = SelectedItem\ItemTemplate\IsWeapon
							Inventory(MouseSlot) = SelectedItem
							SelectedItem = Null
						ElseIf MouseSlot = Inv_Slot_Secondary And (Not SelectedItem\ItemTemplate\IsWeapon)
							PlaySound_Strict(wep_I\DenySFX)
						; ~ Holster Weapon Slot's Logic	
						ElseIf MouseSlot = Inv_Slot_Holster And (SelectedItem\ItemTemplate\IsWeapon And SelectedItem\ItemTemplate\IsHolster)
							PlaySound_Strict(wep_I\SelectSFX)
							For z% = 0 To MaxItemAmount - 1
								If Inventory(z) = SelectedItem Then Inventory(z) = Null : Exit
							Next
							wep_I\CurrentWeaponSlot[QuickSlot_Holster] = SelectedItem\ItemTemplate\IsWeapon
							Inventory(MouseSlot) = SelectedItem
							SelectedItem = Null
						ElseIf MouseSlot = Inv_Slot_Holster And (Not SelectedItem\ItemTemplate\IsHolster) Then
							PlaySound_Strict(wep_I\DenySFX)
						; ~ Scabbard Weapon Slot's Logic	
						ElseIf MouseSlot = Inv_Slot_Scabbard And (SelectedItem\ItemTemplate\IsWeapon And SelectedItem\ItemTemplate\ISScabbard)
							PlaySound_Strict(wep_I\SelectSFX)
							For z% = 0 To MaxItemAmount - 1
								If Inventory(z) = SelectedItem Then Inventory(z) = Null : Exit
							Next
							wep_I\CurrentWeaponSlot[QuickSlot_Scabbard] = SelectedItem\ItemTemplate\IsWeapon
							Inventory(MouseSlot) = SelectedItem
							SelectedItem = Null
						ElseIf MouseSlot = Inv_Slot_Scabbard And (Not SelectedItem\ItemTemplate\ISScabbard)
							PlaySound_Strict(wep_I\DenySFX)
						; ~ Backpack's Inventory Slots' Logic
						ElseIf MouseSlot > Inv_Slot_Scabbard And (Not SelectedItem\ItemTemplate\IsBackPack)
							For z% = 0 To MaxItemAmount - 1
								If Inventory(z) = SelectedItem Then Inventory(z) = Null : Exit
							Next
							Inventory(MouseSlot) = SelectedItem
							SelectedItem = Null
						ElseIf MouseSlot > Inv_Slot_Scabbard And SelectedItem\ItemTemplate\IsBackPack
							PlaySound_Strict(wep_I\DenySFX)
						; ~ Other Slots' Logic	
						Else
							For z% = 0 To MaxItemAmount - 1
								If Inventory(z) = SelectedItem Then Inventory(z) = Null : Exit
							Next
							Inventory(MouseSlot) = SelectedItem
							SelectedItem = Null
						EndIf
						
					ElseIf Inventory(MouseSlot) <> SelectedItem
						PrevItem = Inventory(MouseSlot)
						
						Select(SelectedItem\ItemTemplate\TempName)
							Case "paper", "oldpaper", "origami", "key0", "key1", "key2", "key3", "key4", "key5", "key6", "keyomni", "playcard", "mastercard", "badge", "oldbadge", "burntbadge", "ticket", "25ct", "coin", "key", "scp860", "scp714", "coarse714", "fine714", "ring", "scp500pill", "scp500pilldeath", "pill"
								;[Block]
								If Inventory(MouseSlot)\ItemTemplate\TempName = "clipboard"
									; ~ Add an item to clipboard
									Local added.Items = Null
									Local b$ = SelectedItem\ItemTemplate\TempName
									Local c%, ri%
									
									If b <> "25ct" And b <> "coin" And b <> "key" And b <> "scp860" And b <> "scp714" And b <> "coarse714" And b <> "fine714" And b <> "ring" And b <> "scp500pill" And b <> "scp500pilldeath" And b <> "pill"
										For c = 0 To Inventory(MouseSlot)\InvSlots - 1
											If Inventory(MouseSlot)\SecondInv[c] = Null
												If SelectedItem <> Null
													Inventory(MouseSlot)\SecondInv[c] = SelectedItem
													Inventory(MouseSlot)\State = 1.0
													SetAnimTime(Inventory(MouseSlot)\Model, 0.0)
													Inventory(MouseSlot)\InvImg = Inventory(MouseSlot)\ItemTemplate\InvImg
													
													For ri = 0 To MaxItemAmount - 1
														If Inventory(ri) = SelectedItem
															Inventory(ri) = Null
															PlaySound_Strict(PickSFX[SelectedItem\ItemTemplate\SoundID])
															Exit
														EndIf
													Next
													added = SelectedItem
													SelectedItem = Null
													Exit
												EndIf
											EndIf
										Next
										If SelectedItem <> Null
											CreateMsg(GetLocalString("msg", "clipboard.full"))
										Else
											If added\ItemTemplate\TempName = "paper" Lor added\ItemTemplate\TempName = "oldpaper"
												CreateMsg(GetLocalString("msg", "clipboard.paper"))
											ElseIf added\ItemTemplate\TempName = "badge" Lor added\ItemTemplate\TempName = "oldbadge" Lor added\ItemTemplate\TempName = "burntbadge"
												CreateMsg(Format(GetLocalString("msg", "clipboard.badge"), added\ItemTemplate\Name))
											Else
												CreateMsg(Format(GetLocalString("msg", "clipboard.add"), added\ItemTemplate\Name))
											EndIf
										EndIf
									Else
										UpdateItemSwapping(MouseSlot, PrevItem)
									EndIf
								ElseIf Inventory(MouseSlot)\ItemTemplate\TempName = "wallet"
									; ~ Add an item to wallet
									added.Items = Null
									b = SelectedItem\ItemTemplate\TempName
									If b <> "paper" And b <> "oldpaper" And b <> "origami"
										If (SelectedItem\ItemTemplate\TempName = "scp714" And I_714\Using = 2) Lor (SelectedItem\ItemTemplate\TempName = "coarse714" And I_714\Using = 1)
											CreateMsg(GetLocalString("msg", "takeoff"))
											SelectedItem = Null
											Return
										EndIf
										
										For c = 0 To Inventory(MouseSlot)\InvSlots - 1
											If Inventory(MouseSlot)\SecondInv[c] = Null
												Inventory(MouseSlot)\SecondInv[c] = SelectedItem
												Inventory(MouseSlot)\State = 1.0
												If b <> "25ct" And b <> "coin" And b <> "key" And b <> "scp860" And b <> "scp714" And b <> "coarse714" And b <> "scp500pill" And b <> "scp500pilldeath" And b <> "pill" Then SetAnimTime(Inventory(MouseSlot)\Model, 3.0)
												Inventory(MouseSlot)\InvImg = Inventory(MouseSlot)\ItemTemplate\InvImg
												
												For ri = 0 To MaxItemAmount - 1
													If Inventory(ri) = SelectedItem
														Inventory(ri) = Null
														PlaySound_Strict(PickSFX[SelectedItem\ItemTemplate\SoundID])
														Exit
													EndIf
												Next
												added = SelectedItem
												SelectedItem = Null
												Exit
											EndIf
										Next
										If SelectedItem <> Null
											CreateMsg(GetLocalString("msg", "wallet.full"))
										Else
											CreateMsg(Format(GetLocalString("msg", "wallet.add"), added\ItemTemplate\Name))
										EndIf
									Else
										UpdateItemSwapping(MouseSlot, PrevItem)
									EndIf
								Else
									UpdateItemSwapping(MouseSlot, PrevItem)
								EndIf
								;[End Block]
							Case "coarsebat"
								;[Block]
								Select(Inventory(MouseSlot)\ItemTemplate\TempName)
									Case "nav"
										;[Block]
										If SelectedItem\ItemTemplate\SoundID <> 66 Then PlaySound_Strict(PickSFX[SelectedItem\ItemTemplate\SoundID])
										RemoveItem(SelectedItem)
										Inventory(MouseSlot)\State = Rnd(50.0)
										CreateMsg(GetLocalString("msg", "nav.bat"))
										;[End Block]
									Case "nav310"
										;[Block]
										CreateMsg(GetLocalString("msg", "nav.bat.notfit"))
										;[End Block]
									Case "navulti", "nav300"
										;[Block]
										CreateMsg(GetLocalString("msg", "nav.bat.no"))
										;[End Block]
									Case "radio"
										;[Block]
										If SelectedItem\ItemTemplate\SoundID <> 66 Then PlaySound_Strict(PickSFX[SelectedItem\ItemTemplate\SoundID])
										RemoveItem(SelectedItem)
										Inventory(MouseSlot)\State = Rnd(50.0)
										CreateMsg(GetLocalString("msg", "radio.bat"))
										;[End Block]
									Case "18vradio"
										;[Block]
										CreateMsg(GetLocalString("msg", "radio.bat.notfit"))
										;[End Block]
									Case "fineradio", "veryfineradio"
										;[Block]
										CreateMsg(GetLocalString("msg", "radio.bat.no"))
										;[End Block]
									Case "nvg"
										;[Block]
										If SelectedItem\ItemTemplate\SoundID <> 66 Then PlaySound_Strict(PickSFX[SelectedItem\ItemTemplate\SoundID])
										RemoveItem(SelectedItem)
										Inventory(MouseSlot)\State = Rnd(500.0)
										CreateMsg(GetLocalString("msg", "nvg.bat"))
										;[End Block]
									Case "nvg2"
										;[Block]
										CreateMsg(GetLocalString("msg", "nvg.bat.no"))
										;[End Block]
									Case "nvg3"
										;[Block]
										CreateMsg(GetLocalString("msg", "nvg.bat.notfit"))
										;[End Block]
									Case "scramble"
										;[Block]
										If SelectedItem\ItemTemplate\SoundID <> 66 Then PlaySound_Strict(PickSFX[SelectedItem\ItemTemplate\SoundID])
										RemoveItem(SelectedItem)
										Inventory(MouseSlot)\State = Rnd(500.0)
										CreateMsg(GetLocalString("msg", "gear.bat"))
										;[End Block]
									Case "scramble2"
										;[Block]
										CreateMsg(GetLocalString("msg", "gear.bat.notfit"))
										;[End Block]
									Default
										;[Block]
										UpdateItemSwapping(MouseSlot, PrevItem)
										;[End Block]
								End Select
								;[End Block]
							Case "bat"
								;[Block]
								Select(Inventory(MouseSlot)\ItemTemplate\TempName)
									Case "nav"
										;[Block]
										If SelectedItem\ItemTemplate\SoundID <> 66 Then PlaySound_Strict(PickSFX[SelectedItem\ItemTemplate\SoundID])
										RemoveItem(SelectedItem)
										Inventory(MouseSlot)\State = Rnd(100.0)
										CreateMsg(GetLocalString("msg", "nav.bat"))
										;[End Block]
									Case "nav310"
										;[Block]
										CreateMsg(GetLocalString("msg", "nav.bat.notfit"))
										;[End Block]
									Case "navulti", "nav300"
										;[Block]
										CreateMsg(GetLocalString("msg", "nav.bat.no"))
										;[End Block]
									Case "radio"
										;[Block]
										If SelectedItem\ItemTemplate\SoundID <> 66 Then PlaySound_Strict(PickSFX[SelectedItem\ItemTemplate\SoundID])
										RemoveItem(SelectedItem)
										Inventory(MouseSlot)\State = Rnd(100.0)
										CreateMsg(GetLocalString("msg", "radio.bat"))
										;[End Block]
									Case "18vradio"
										;[Block]
										CreateMsg(GetLocalString("msg", "radio.bat.notfit"))
										;[End Block]
									Case "fineradio", "veryfineradio"
										;[Block]
										CreateMsg(GetLocalString("msg", "radio.bat.no"))
										;[End Block]
									Case "nvg"
										;[Block]
										If SelectedItem\ItemTemplate\SoundID <> 66 Then PlaySound_Strict(PickSFX[SelectedItem\ItemTemplate\SoundID])
										RemoveItem(SelectedItem)
										Inventory(MouseSlot)\State = Rnd(0.0, 1000.0)
										CreateMsg(GetLocalString("msg", "nvg.bat"))
										;[End Block]
									Case "nvg2"
										;[Block]
										CreateMsg(GetLocalString("msg", "nvg.bat.no"))
										;[End Block]
									Case "nvg3"
										;[Block]
										CreateMsg(GetLocalString("msg", "nvg.bat.notfit"))
										;[End Block]
									Case "scramble"
										;[Block]
										If SelectedItem\ItemTemplate\SoundID <> 66 Then PlaySound_Strict(PickSFX[SelectedItem\ItemTemplate\SoundID])
										RemoveItem(SelectedItem)
										Inventory(MouseSlot)\State = Rnd(0.0, 1000.0)
										CreateMsg(GetLocalString("msg", "gear.bat"))
										;[End Block]
									Case "scramble2"
										;[Block]
										CreateMsg(GetLocalString("msg", "gear.bat.notfit"))
										;[End Block]
									Default
										;[Block]
										UpdateItemSwapping(MouseSlot, PrevItem)
										;[End Block]
								End Select
								;[End Block]
							Case "finebat"
								;[Block]
								Select(Inventory(MouseSlot)\ItemTemplate\TempName)
									Case "nav"
										;[Block]
										CreateMsg(GetLocalString("msg", "nav.bat.notfit"))
										;[End Block]
									Case "nav310"
										;[Block]
										If SelectedItem\ItemTemplate\SoundID <> 66 Then PlaySound_Strict(PickSFX[SelectedItem\ItemTemplate\SoundID])
										RemoveItem(SelectedItem)
										Inventory(MouseSlot)\State = Rnd(0.0, 200.0)
										CreateMsg(GetLocalString("msg", "nav.bat"))
										;[End Block]
									Case "navulti", "nav300"
										;[Block]
										CreateMsg(GetLocalString("msg", "nav.bat.no"))
										;[End Block]
									Case "radio"
										;[Block]
										CreateMsg(GetLocalString("msg", "radio.bat.notfit"))
										;[End Block]
									Case "18vradio"
										;[Block]
										If SelectedItem\ItemTemplate\SoundID <> 66 Then PlaySound_Strict(PickSFX[SelectedItem\ItemTemplate\SoundID])
										RemoveItem(SelectedItem)
										Inventory(MouseSlot)\State = Rnd(0.0, 200.0)
										CreateMsg(GetLocalString("msg", "radio.bat"))
										;[End Block]
									Case "fineradio", "veryfineradio"
										;[Block]
										CreateMsg(GetLocalString("msg", "radio.bat.no"))
										;[End Block]
									Case "nvg"
										;[Block]
										CreateMsg(GetLocalString("msg", "nvg.bat.notfit"))
										;[End Block]
									Case "nvg2"
										;[Block]
										CreateMsg(GetLocalString("msg", "nvg.bat.no"))
										;[End Block]
									Case "nvg3"
										;[Block]
										If SelectedItem\ItemTemplate\SoundID <> 66 Then PlaySound_Strict(PickSFX[SelectedItem\ItemTemplate\SoundID])
										RemoveItem(SelectedItem)
										Inventory(MouseSlot)\State = Rnd(1000.0)
										CreateMsg(GetLocalString("msg", "nvg.bat"))
										;[End Block]
									Case "scramble"
										;[Block]
										CreateMsg(GetLocalString("msg", "gear.bat.notfit"))
										;[End Block]
									Case "scramble2"
										;[Block]
										If SelectedItem\ItemTemplate\SoundID <> 66 Then PlaySound_Strict(PickSFX[SelectedItem\ItemTemplate\SoundID])
										RemoveItem(SelectedItem)
										Inventory(MouseSlot)\State = Rnd(500.0, 1000.0)
										CreateMsg(GetLocalString("msg", "gear.bat"))
										;[End Block]
									Default
										;[Block]
										UpdateItemSwapping(MouseSlot, PrevItem)
										;[End Block]
								End Select
								;[End Block]
							Case "veryfinebat", "killbat"
								;[Block]
								Select(Inventory(MouseSlot)\ItemTemplate\TempName)
									Case "nav"
										;[Block]
										If SelectedItem\ItemTemplate\SoundID <> 66 Then PlaySound_Strict(PickSFX[SelectedItem\ItemTemplate\SoundID])
										RemoveItem(SelectedItem)
										Inventory(MouseSlot)\State = Rnd(0.0, 1000.0)
										CreateMsg(GetLocalString("msg", "nav.bat"))
										;[End Block]
									Case "nav310"
										;[Block]
										CreateMsg(GetLocalString("msg", "nav.bat.notfit"))
										;[End Block]
									Case "navulti", "nav300"
										;[Block]
										CreateMsg(GetLocalString("msg", "nav.bat.no"))
										;[End Block]
									Case "radio"
										;[Block]
										If SelectedItem\ItemTemplate\SoundID <> 66 Then PlaySound_Strict(PickSFX[SelectedItem\ItemTemplate\SoundID])
										RemoveItem(SelectedItem)
										Inventory(MouseSlot)\State = Rnd(0.0, 1000.0)
										CreateMsg(GetLocalString("msg", "radio.bat"))
										;[End Block]
									Case "18vradio"
										;[Block]
										CreateMsg(GetLocalString("msg", "radio.bat.notfit"))
										;[End Block]
									Case "fineradio", "veryfineradio"
										;[Block]
										CreateMsg(GetLocalString("msg", "radio.bat.no"))
										;[End Block]
									Case "nvg"
										;[Block]
										If SelectedItem\ItemTemplate\SoundID <> 66 Then PlaySound_Strict(PickSFX[SelectedItem\ItemTemplate\SoundID])
										RemoveItem(SelectedItem)
										Inventory(MouseSlot)\State = Rnd(10000.0)
										CreateMsg(GetLocalString("msg", "nvg.bat"))
										;[End Block]
									Case "nvg2"
										;[Block]
										CreateMsg(GetLocalString("msg", "nvg.bat.no"))
										;[End Block]
									Case "nvg3"
										;[Block]
										CreateMsg(GetLocalString("msg", "nvg.bat.notfit"))
										;[End Block]
									Case "scramble"
										;[Block]
										If SelectedItem\ItemTemplate\SoundID <> 66 Then PlaySound_Strict(PickSFX[SelectedItem\ItemTemplate\SoundID])
										RemoveItem(SelectedItem)
										Inventory(MouseSlot)\State = Rnd(10000.0)
										CreateMsg(GetLocalString("msg", "gear.bat"))
										;[End Block]
									Case "scramble2"
										;[Block]
										CreateMsg(GetLocalString("msg", "gear.bat.notfit"))
										;[End Block]
									Default
										;[Block]
										UpdateItemSwapping(MouseSlot, PrevItem)
										;[End Block]
								End Select
								;[End Block]
							Default
								;[Block]
								UpdateItemSwapping(MouseSlot, PrevItem)
								;[End Block]
						End Select
					EndIf
				EndIf
				SelectedItem = Null
			EndIf
		EndIf
		If (Not InvOpen) Then StopMouseMovement()
	EndIf
	
End Function
;[End Block]

Function UpdateInventorySlots%()
	Local wep.Weapons, p%
	
	; ~ Head Slot
	;[Block]
	If Inventory(Inv_Slot_Head) <> Null And Left(Inventory(Inv_Slot_Head)\ItemTemplate\TempName, 3) = "nvg"
		If Inventory(Inv_Slot_Head)\ItemTemplate\TempName = "nvg" Then wi\NightVision = 1
		If Inventory(Inv_Slot_Head)\ItemTemplate\TempName = "nvg3" Then wi\NightVision = 2
		If Inventory(Inv_Slot_Head)\ItemTemplate\TempName = "nvg2" Then wi\NightVision = 3
		If Inventory(Inv_Slot_Head)\State > 0 Then
			opt\CameraFogFar = 30
		Else
			opt\CameraFogFar = 6
		EndIf
	Else
		wi\NightVision = 0
		opt\CameraFogFar = 6
	EndIf
	
	If Inventory(Inv_Slot_Head) <> Null And Left(Inventory(Inv_Slot_Head)\ItemTemplate\TempName, 8) = "scramble"
		If Inventory(Inv_Slot_Head)\ItemTemplate\TempName = "scramble" Then wi\SCRAMBLE = 1
		If Inventory(Inv_Slot_Head)\ItemTemplate\TempName = "scramble2" Then wi\SCRAMBLE = 2
	Else
		wi\SCRAMBLE = 0
	EndIf
	
	If Inventory(Inv_Slot_Head) <> Null And Left(Inventory(Inv_Slot_Head)\ItemTemplate\TempName, 7) = "gasmask"
		If Inventory(Inv_Slot_Head)\ItemTemplate\TempName = "gasmask" Then wi\GasMask = 1
		If Inventory(Inv_Slot_Head)\ItemTemplate\TempName = "gasmask2" Then wi\GasMask = 2
		If Inventory(Inv_Slot_Head)\ItemTemplate\TempName = "gasmask3" Then wi\GasMask = 3
		If Inventory(Inv_Slot_Head)\ItemTemplate\TempName = "gasmask4" Then wi\GasMask = 4
	Else
		wi\GasMask = 0
	EndIf
	
	If Inventory(Inv_Slot_Head) <> Null And Left(Inventory(Inv_Slot_Head)\ItemTemplate\TempName, 6) = "scp268"
		If I_268\Using = 0 And Inventory(Inv_Slot_Head)\ItemTemplate\TempName <> "scp268fake"
			PlaySound_Strict(LoadTempSound("SFX\SCP\268\InvisibilityOn.ogg"))
		EndIf
		If Inventory(Inv_Slot_Head)\ItemTemplate\TempName = "scp268fake" Then I_268\Using = 1
		If  I_268\Timer > 0.0
			If Inventory(Inv_Slot_Head)\ItemTemplate\TempName = "scp268" Then I_268\Using = 2
			If Inventory(Inv_Slot_Head)\ItemTemplate\TempName = "scp268fine" Then I_268\Using = 3
			If I_268\Using > 1 Then ShouldPlay = MUS_NULL
		Else
			If I_268\Using > 1 Then PlaySound_Strict(LoadTempSound("SFX\SCP\268\InvisibilityOff.ogg"))
			I_268\Using = -1
		EndIf
		GiveAchievement(Achv268)
	Else
		If I_268\Using > 1 Then PlaySound_Strict(LoadTempSound("SFX\SCP\268\InvisibilityOff.ogg"))
		I_268\Using = 0
	EndIf
	
	If Inventory(Inv_Slot_Head) <> Null And Left(Inventory(Inv_Slot_Head)\ItemTemplate\TempName, 6) = "helmet"
		wi\BallisticHelmet = 1
	Else
		wi\BallisticHelmet = 0
	EndIf
	;[End Block]
	
	; ~ Torso Slot
	;[Block]
	If Inventory(Inv_Slot_Torso) <> Null And Left(Inventory(Inv_Slot_Torso)\ItemTemplate\TempName, 3) = "hds"
		wi\HDS = 1
	Else
		wi\HDS = 0
	EndIf
	
	If Inventory(Inv_Slot_Torso) <> Null And Left(Inventory(Inv_Slot_Torso)\ItemTemplate\TempName, 4) = "vest"
		If Inventory(Inv_Slot_Torso)\ItemTemplate\TempName = "vest" Then wi\BallisticVest = 1
		If Inventory(Inv_Slot_Torso)\ItemTemplate\TempName = "vest2" Then wi\BallisticVest = 2
		If Inventory(Inv_Slot_Torso)\ItemTemplate\TempName = "vest3" Then wi\BallisticVest = 3
	Else
		wi\BallisticVest = 0
	EndIf
	
	If Inventory(Inv_Slot_Torso) <> Null And Left(Inventory(Inv_Slot_Torso)\ItemTemplate\TempName, 6) = "hazmat"
		If Inventory(Inv_Slot_Torso)\ItemTemplate\TempName = "hazmat" Then wi\HazmatSuit = 1
		If Inventory(Inv_Slot_Torso)\ItemTemplate\TempName = "hazmat2" Then wi\HazmatSuit = 2
		If Inventory(Inv_Slot_Torso)\ItemTemplate\TempName = "hazmat3" Then wi\HazmatSuit = 3
		If Inventory(Inv_Slot_Torso)\ItemTemplate\TempName = "hazmat4" Then wi\HazmatSuit = 4
	Else
		wi\HazmatSuit = 0
	EndIf
	;[End Block]
	
	; ~ Gatget Slot
	;[Block]
	
	;[End Block]
	
	; ~ Backpack Slot
	;[Block]
	If Inventory(Inv_Slot_Backpack) <> Null And Inventory(Inv_Slot_Backpack)\ItemTemplate\TempName = "backpack"
		wi\Backpack = 1
		MaxItemAmount = 38
		MaxCarriableWeight = (MaxWeightDefault + 15)
	Else
		wi\Backpack = 0
		MaxItemAmount = 17
		MaxCarriableWeight = MaxWeightDefault
	EndIf
	;[End Block]
	
	; ~ Weapon Slots
	;[Block]
	For p = Inv_Slot_Primary To Inv_Slot_Scabbard
		If Inventory(p) <> Null
			wep_I\CurrentWeaponSlot[p - Inv_Slot_Primary] = Inventory(p)\ItemTemplate\IsWeapon
		Else
			For wep = Each Weapons
				If wep\ID = wep_I\CurrentWeaponSlot[p - Inv_Slot_Primary] And wep\ID = wep_I\Using
					wep_I\SlotTimer = 0.0 : wep\State = Wep_State_Holster : Exit
				EndIf
			Next
			wep_I\CurrentWeaponSlot[p - Inv_Slot_Primary] = 0
		EndIf
	Next
	;[End Block]
	
End Function

Function UpdateItemSwapping(MouseSlotID%, PreviousItem.Items)
	Local z%, b%
	
	If MouseSlotID <> Inv_Slot_Head And MouseSlotID <> Inv_Slot_Torso And MouseSlotID <> Inv_Slot_Gatget And MouseSlotID <> Inv_Slot_Backpack And MouseSlotID <> Inv_Slot_Primary And MouseSlotID <> Inv_Slot_Secondary And MouseSlotID <> Inv_Slot_Holster And MouseSlotID <> Inv_Slot_Scabbard Then
		If Inventory(Inv_Slot_Head) <> SelectedItem And Inventory(Inv_Slot_Torso) <> SelectedItem And Inventory(Inv_Slot_Gatget) <> SelectedItem And Inventory(Inv_Slot_Backpack) <> SelectedItem And Inventory(Inv_Slot_Primary) <> SelectedItem And Inventory(Inv_Slot_Secondary) <> SelectedItem And Inventory(Inv_Slot_Holster) <> SelectedItem And Inventory(Inv_Slot_Scabbard) <> SelectedItem Then
			For z = 0 To MaxItemAmount - 1
				If Inventory(z) = SelectedItem
					Inventory(z) = PreviousItem
					Exit
				EndIf
			Next
			Inventory(MouseSlotID) = SelectedItem
		Else
			PlaySound_Strict(wep_I\DenySFX)
		EndIf
	Else
		PlaySound_Strict(wep_I\DenySFX)
	EndIf
	SelectedItem = Null
End Function

Function ClearInventory(WithBackpack# = False)
	Local invs%, ItemAmount%
	
	If WithBackpack Then
		ItemAmount = MaxInventorySpace
	Else
		ItemAmount = MaxItemAmount
	EndIf
	For invs = 0 To ItemAmount-1
		If Inventory(invs) <> Null Then
			RemoveItem(Inventory(invs))
		EndIf
	Next
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D TSS