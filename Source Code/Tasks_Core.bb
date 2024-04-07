
; ~ Task Status ID Constants
;[Block]
Const TASK_STATUS_NEW = 0
Const TASK_STATUS_ALREADY = 1
Const TASK_STATUS_END = 2
Const TASK_STATUS_FAILED = 3
Const TASK_STATUS_CANCELED = 4
Const TASK_STATUS_UPDATED = 5

Const TASK_RENDER_TIME = 70.0*5.0
;[End Block]

; ~ Task ID Constants
;[Block]
Const Task_Ryan_CPT_0_Go_To_Tram% = 0
;[End Block]

Type NewTask
	Field ID%
	Field Txt$
	Field Desc$
	Field Status%
	Field Timer#
End Type

Function BeginTask.NewTask(ID%)
	Local t.NewTask
	
	For t = Each NewTask
		If t\ID = ID Then
			Return t
		EndIf
	Next
	
	t = New NewTask
	t\ID = ID
	Select(t\ID)
		Case Task_Ryan_CPT_0_Go_To_Tram
			t\Txt = GetLocalString("tasks", "task.ryan_cpt_0_go_to_tram")
			t\Desc = GetLocalString("tasks", "task.desc.ryan_cpt_0_go_to_tram")
	End Select
	t\Timer = TASK_RENDER_TIME
	t\Status = TASK_STATUS_NEW
	
	Return t
End Function

Function TaskExists(ID%)
	Local t.NewTask
	
	For t = Each NewTask
		If t\ID = ID Then
			Return True
		EndIf
	Next
	
	Return False
End Function

Function EndTask(ID%)
	Local t.NewTask
	
	For t = Each NewTask
		If t\ID = ID Then
			If t\Status <> TASK_STATUS_END And t\Status <> TASK_STATUS_FAILED Then
				t\Status = TASK_STATUS_END
				t\Timer = TASK_RENDER_TIME
			EndIf
			Exit
		EndIf
	Next
	
End Function

Function FailTask(ID%)
	Local t.NewTask
	
	For t = Each NewTask
		If t\ID = ID Then
			If t\Status <> TASK_STATUS_END And t\Status <> TASK_STATUS_FAILED And t\Status <> TASK_STATUS_CANCELED And t\Status <> TASK_STATUS_UPDATED Then
				t\Status = TASK_STATUS_FAILED
				t\Timer = TASK_RENDER_TIME
			EndIf
			Exit
		EndIf
	Next
	
End Function

Function CancelTask(ID%)
	Local t.NewTask
	
	For t = Each NewTask
		If t\ID = ID Then
			If t\Status <> TASK_STATUS_END And t\Status <> TASK_STATUS_FAILED And t\Status <> TASK_STATUS_CANCELED And t\Status <> TASK_STATUS_UPDATED Then
				t\Status = TASK_STATUS_CANCELED
				t\Timer = TASK_RENDER_TIME
			EndIf
			Exit
		EndIf
	Next
	
End Function

Function UpdateTask(ID%)
	Local t.NewTask
	
	For t = Each NewTask
		If t\ID = ID Then
			If t\Status <> TASK_STATUS_END And t\Status <> TASK_STATUS_FAILED And t\Status <> TASK_STATUS_CANCELED And t\Status <> TASK_STATUS_UPDATED Then
				t\Status = TASK_STATUS_UPDATED
				t\Timer = TASK_RENDER_TIME
			EndIf
			Exit
		EndIf
	Next
	
End Function

Function UpdateTasks()
	Local t.NewTask
	Local hasEndTask% = False
	Local hasFailedTask% = False
	Local hasCanceledTask% = False
	Local hasUpdatedTask% = False
	
	For t = Each NewTask
		If t\Status = TASK_STATUS_FAILED Then
			hasFailedTask = True
			hasEndTask = False
		ElseIf t\Status = TASK_STATUS_END And (Not hasFailedTask) Then
			hasEndTask = True
		ElseIf t\Status = TASK_STATUS_UPDATED
			hasUpdatedTask = True
		ElseIf t\Status = TASK_STATUS_CANCELED And (Not hasFailedTask) Then
			hasCanceledTask = True
			hasEndTask = False
			Exit
		EndIf
	Next
	
	For t = Each NewTask
;		If t\ID = TASK_FIND_ROOM3_CT_FUSEBOXES Then
			;t\txt = Format(GetLocalString("tasks", "find_all_fuseboxes"), Int(ecst\FusesAmount)) TODO!!!
;		EndIf
		If (hasEndTask And t\Status = TASK_STATUS_END) Lor (hasFailedTask And t\Status = TASK_STATUS_FAILED) Lor (hasCanceledTask And t\Status = TASK_STATUS_CANCELED) Lor (hasUpdatedTask And t\Status = TASK_STATUS_UPDATED) Lor ((Not hasEndTask) And (Not hasFailedTask) And (Not hasUpdatedTask) And (Not hasCanceledTask) And t\Status = TASK_STATUS_NEW) Then
			If t\Timer > 0.0 Then
				t\Timer = t\Timer - fps\Factor[0]
				If t\Timer <= 0.0 Then
					t\Timer = 0.0
					If t\Status = TASK_STATUS_END Lor t\Status = TASK_STATUS_CANCELED Lor t\Status = TASK_STATUS_FAILED Then
						Delete t
					Else
						t\Status = TASK_STATUS_ALREADY
					EndIf
				EndIf
			EndIf
		EndIf
	Next
	
End Function

Function RenderTasks()
	Local t.NewTask
	Local a#, x#, y#, hasNewTask%, hasEndTask%, hasFailedTask%, hasCanceledTask%, hasUpdatedTask%, globalTime#, txt$, atLeastOneTask%
	Local width#, height#
	
	If (opt\HUDEnabled And me\ShowHUD) Lor InvOpen Then
		hasNewTask = False
		globalTime = 0.0
		
		width# = 300
		height# = 200
		
		x = opt\GraphicWidth / 2
		y = opt\GraphicHeight / 6
		
		For t = Each NewTask
			If t\Status = TASK_STATUS_FAILED Then
				hasFailedTask = True
				hasEndTask = False
				hasNewTask = False
			ElseIf t\Status = TASK_STATUS_END And (Not hasFailedTask) Then
				hasEndTask = True
				hasNewTask = False
			ElseIf t\Status = TASK_STATUS_CANCELED Then
				hasCanceledTask = True
				hasEndTask = False
				hasNewTask = False
			ElseIf t\Status = TASK_STATUS_NEW And (Not hasEndTask) And (Not hasFailedTask) And (Not hasCanceledTask) Then
				hasNewTask = True
			ElseIf t\Status = TASK_STATUS_UPDATED And (Not hasEndTask) And (Not hasFailedTask) And (Not hasCanceledTask) Then
				hasUpdatedTask = True
			EndIf
		Next
		For t = Each NewTask
			If (t\Status = TASK_STATUS_FAILED And hasFailedTask) Lor (t\Status = TASK_STATUS_END And hasEndTask) Lor (t\Status = TASK_STATUS_CANCELED And hasCanceledTask) Lor (t\Status = TASK_STATUS_NEW And hasNewTask) Lor (t\Status = TASK_STATUS_UPDATED And hasUpdatedTask) Then
				If t\Timer > globalTime Then
					globalTime = t\Timer
				EndIf
			EndIf
		Next
		
		If hasEndTask Lor hasFailedTask Lor hasCanceledTask Lor hasUpdatedTask Lor hasNewTask Lor InvOpen Then
			If InvOpen Then
				txt = GetLocalString("tasks", "task.current")
			ElseIf hasFailedTask Then
				txt = GetLocalString("tasks", "task.failed")
			ElseIf hasCanceledTask Then
				txt = GetLocalString("tasks", "task.cancelled")
			ElseIf hasEndTask Then
				txt = GetLocalString("tasks", "task.completed")
			ElseIf hasUpdatedTask Then
				txt = GetLocalString("tasks", "task.updated")
			Else
				txt = GetLocalString("tasks", "task.new")
			EndIf
			SetFont fo\FontID[Font_Default]
			Color 255, 255, 255
			TextEx(x - 1, y + 15, Upper(txt), True, False)
			
			If InvOpen Then
				a = 255
			Else
				a = Min(globalTime / 2, 255)
			EndIf
			
			If (Not InvOpen) Then
				If hasEndTask Then
					Color 0,a,0
				ElseIf hasFailedTask Then
					Color a,0,0
				;ElseIf hasNewTask Then
				;	Color a,a,0           - Yellow color that pisses me off >:( ~ Wolfnaya
				Else
					Color a,a,a
				EndIf
			Else
				Color a,a,a
			EndIf
			
			TextEx(x - 1, y + 15, Upper(txt), True, False)
			
			y = y + 40.0
			SetFont fo\FontID[Font_Default]
			
			atLeastOneTask = False
			For t = Each NewTask
				If (InvOpen And t\Status <> TASK_STATUS_END) Lor ((Not InvOpen) And ((hasEndTask And t\Status = TASK_STATUS_END) Lor (hasUpdatedTask And t\Status = TASK_STATUS_UPDATED) Lor (hasFailedTask And t\Status = TASK_STATUS_FAILED) Lor (hasCanceledTask And t\Status = TASK_STATUS_CANCELED) Lor (hasNewTask And t\Status = TASK_STATUS_NEW))) Then
					If t\Timer > 0.0 Lor InvOpen Then
						If InvOpen Then
							a = 255
						Else
							a = Min(t\Timer / 2, 255)
						EndIf
						Color a, a, a
						TextEx(x - 1, y - 1, t\Txt, True, False)
						
						y = y + 20.0
						atLeastOneTask = True
					EndIf
				EndIf
			Next
			
			If InvOpen And (Not atLeastOneTask) Then
				txt = GetLocalString("tasks", "task.none")
				
				Color 255, 255, 255
				TextEx(x - 1, y - 1, txt, True, False)
			EndIf
		EndIf
	EndIf
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D TSS