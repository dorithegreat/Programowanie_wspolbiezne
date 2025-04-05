package main

import (
	"fmt"
	"math/rand"
	"sync"
	"time"
)

const Nr_Of_Travelers int = 15

const Min_Steps int = 10
const Max_Steps int = 100

const Min_Delay time.Duration = 10 * time.Millisecond
const Max_Delay time.Duration = 50 * time.Millisecond

const Board_Width int = 15
const Board_Height int = 15

// TODO time, seeds

var Start_Time time.Time = time.Now()

type Position_Type struct {
	X int
	Y int
}

func Move_Down(Position Position_Type) {
	Position.Y = (Position.Y + 1) % Board_Height
}

func Move_Up(Postition Position_Type) {
	Postition.Y = (Postition.Y + Board_Height - 1) % Board_Height
}

func Move_Right(Postition Position_Type) {
	Postition.X = (Postition.X + 1) % Board_Width
}

func Move_Left(Position Position_Type) {
	Position.X = (Position.X + Board_Height - 1) % Board_Width
}

type Trace_Type struct {
	Time_Stamp time.Duration
	Id         int
	Position   Position_Type
	Symbol     string
}

type Traces_Sequence_Type struct {
	Last        int
	Trace_Array [Max_Steps]Trace_Type
}

func Print_Trace(Trace Trace_Type) {
	Symbol := " " + Trace.Symbol
	fmt.Printf("%f %d %d %d %s", Trace.Time_Stamp.Seconds(), Trace.Id,
		Trace.Position.X, Trace.Position.Y, Symbol)
}

func Print_Traces(Traces Traces_Sequence_Type) {
	for i := 0; i < Traces.Last; i++ {
		Print_Trace(Traces.Trace_Array[i])
	}
}

// task Printer
func Printer() {
	for i := 1; i < Nr_Of_Travelers; i++ {

	}
}

type Traveler_Type struct {
	Id      int
	Symbol  string
	Postion Position_Type
}

func Store_Trace(Traveler Traveler_Type, Time_Stamp time.Duration, Traces Traces_Sequence_Type) {
	Traces.Last = Traces.Last + 1
	Traces.Trace_Array[Traces.Last].Time_Stamp = Time_Stamp
	Traces.Trace_Array[Traces.Last].Id = Traveler.Id
	Traces.Trace_Array[Traces.Last].Position = Traveler.Postion
	Traces.Trace_Array[Traces.Last].Symbol = Traveler.Symbol
}

// task Traveler
func Traveler_Task_Type(Id int, Seed int, Symbol string, wg *sync.WaitGroup) {
	var Traveler Traveler_Type
	var Time_Stamp time.Duration
	var Nr_of_Steps int
	var Traces Traces_Sequence_Type
	Store_Trace := func() {
		Traces.Last = Traces.Last + 1
		Traces.Trace_Array[Traces.Last].Time_Stamp = Time_Stamp
		Traces.Trace_Array[Traces.Last].Id = Traveler.Id
		Traces.Trace_Array[Traces.Last].Position = Traveler.Postion
		Traces.Trace_Array[Traces.Last].Symbol = Traveler.Symbol
	}

	Make_Step := func() {
		var n int
		n = rand.Intn(4)
		switch n {
		case 0:
			Move_Up(Traveler.Postion)
		case 1:
			Move_Down(Traveler.Postion)
		case 2:
			Move_Left(Traveler.Postion)
		case 3:
			Move_Right(Traveler.Postion)
		default:
			fmt.Printf("??????? %d", n)
		}
	}

	Traveler.Id = Id
	Traveler.Symbol = Symbol

	Traveler.Postion.X = rand.Intn(Board_Width)
	Traveler.Postion.Y = rand.Intn(Board_Height)
	Store_Trace()
	Nr_of_Steps = Min_Steps + rand.Intn(Max_Steps-Min_Steps)
	Time_Stamp = time.Since(Start_Time)

	// TODO accept start

	for step := 0; step < Nr_of_Steps; step++ {
		time.Sleep(Min_Delay + time.Duration(rand.Intn(int(Max_Delay)-int(Min_Delay))))
		Make_Step()
		Store_Trace()
		Time_Stamp = time.Since(Start_Time)
		// TODO printer.report(traces)
	}
}

const Symbol string = "A"

func main() {
	fmt.Printf("-1 %d %d %d\n", Nr_Of_Travelers, Board_Width, Board_Height)

}
