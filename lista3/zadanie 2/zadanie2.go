package main

import (
	"fmt"
	"time"
)

type Process_State int

const Nr_Of_Processes int = 15

const Min_Steps int = 50
const Max_Steps int = 100

const Min_Delay time.Duration = 10 * time.Millisecond
const Max_Delay time.Duration = 50 * time.Millisecond

// enum

const (
	Local_Section Process_State = iota
	Entry_Protocol
	Critical_Section
	Exit_Protocol
)

// 2D board

const Board_Width int = Nr_Of_Processes
const Board_Height int = 4

var Start_Time time.Time = time.Now()
var Seeds [Nr_Of_Processes]int

var Entering [Nr_Of_Processes]bool
var Number [Nr_Of_Processes]int

type Position_Type struct {
	X int
	Y int
}

type Trace_Type struct {
	Time_Stamp time.Duration
	Id         int
	Position   Position_Type
	Symbol     rune
}

type Traces_Sequence_Type struct {
	Last        int
	Trace_Array [Max_Steps]Trace_Type
}

func Print_Trace(Trace Trace_Type) {
	// Symbol := " " + Trace.Symbol
	fmt.Printf("%f %d %d %d %c \n", Trace.Time_Stamp.Seconds(), Trace.Id,
		Trace.Position.X, Trace.Position.Y, Trace.Symbol)
	// println(Trace.Symbol)
}

func Print_Traces(Traces Traces_Sequence_Type) {
	for i := 0; i < Traces.Last; i++ {
		Print_Trace(Traces.Trace_Array[i])
	}
}

// bakery algorithm functions
func Maximum_Number() int {
	Current := 0

	for n := 0; n < Nr_Of_Processes; n++ {
		if Number[n] > Current {
			Current = Number[n]
		}
	}
	return Current
}

func lock(I int) {
	Entering[I] = true
	Number[I] = 1 + Maximum_Number()
	Entering[I] = false

	for j := 0; j < Nr_Of_Processes; j++ {
		if j != I {
			for Entering[j] == false {

			}

			for (Number[j] == 0) || (Number[I] < Number[j]) || (Number[I] == Number[j] && I < j) {

			}
		}
	}
}

func unlock(I int) {
	Number[I] = 0
}

// task Printer
func Printer(Report chan Traces_Sequence_Type) {
	// defer wg.Done()
	for i := 0; i < Nr_Of_Processes; i++ {
		Print_Traces(<-Report)
	}

	fmt.Printf("-1 %d %d %d", Nr_Of_Processes, Board_Width, Board_Height)

	fmt.Printf("%s; %s; %s; %s; ", Local_Section, Entry_Protocol, Critical_Section, Exit_Protocol)

	fmt.Println("EXTRA_LABEL;")
}

type Process_Type struct {
	Id       int
	Symbol   rune
	Position Position_Type
}

func Process_Task_Type() {
	// G := Generator
	var Process Process_Type
	var Time_Stamp time.Duration
	var Nr_of_Steps int
	var Traces Traces_Sequence_Type

	Store_Trace := func() {
		Traces.Trace_Array[Traces.Last].Time_Stamp = Time_Stamp
		Traces.Trace_Array[Traces.Last].Id = Process.Id
		Traces.Trace_Array[Traces.Last].Position = Process.Position
		Traces.Trace_Array[Traces.Last].Symbol = Process.Symbol
		Traces.Last = Traces.Last + 1
		// println(Traces.Trace_Array[Traces.Last].Symbol)
	}

	Change_State := func() {
		Time_Stamp = time.Since(Start_Time)
	}

}
