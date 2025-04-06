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
var Seeds [Nr_Of_Travelers]int

type Position_Type struct {
	X int
	Y int
}

func Move_Down(Position *Position_Type) {
	Position.Y = (Position.Y + 1) % Board_Height
}

func Move_Up(Postition *Position_Type) {
	Postition.Y = (Postition.Y + Board_Height - 1) % Board_Height
}

func Move_Right(Postition *Position_Type) {
	Postition.X = (Postition.X + 1) % Board_Width
}

func Move_Left(Position *Position_Type) {
	Position.X = (Position.X + Board_Width - 1) % Board_Width
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
}

func Print_Traces(Traces Traces_Sequence_Type) {
	for i := 0; i < Traces.Last; i++ {
		Print_Trace(Traces.Trace_Array[i])
	}
}

// task Printer
func Printer(Report chan Traces_Sequence_Type) {
	for {
		Print_Traces(<-Report)
	}
}

type Traveler_Type struct {
	Id      int
	Symbol  rune
	Postion Position_Type
}

// empty struct for passing around through channels
type T = struct{}

// task Traveler
func Traveler_Task_Type(Id int, Seed int, Symbol rune,
	ready <-chan T, printer chan<- Traces_Sequence_Type) {

	var Traveler Traveler_Type
	var Time_Stamp time.Duration
	var Nr_of_Steps int
	var Traces Traces_Sequence_Type

	Store_Trace := func() {
		Traces.Last = Traces.Last + 1
		Traces.Trace_Array[Traces.Last].Time_Stamp = Time_Stamp
		Traces.Trace_Array[Traces.Last].Id = Traveler.Id
		Traces.Trace_Array[Traces.Last].Position = Traveler.Postion
		Traces.Trace_Array[Traces.Last].Symbol = Symbol
		// println(Traces.Trace_Array[Traces.Last].Symbol)
	}

	Make_Step := func() {
		var n int
		n = rand.Intn(4)
		switch n {
		case 0:
			Move_Up(&Traveler.Postion)
		case 1:
			Move_Down(&Traveler.Postion)
		case 2:
			Move_Left(&Traveler.Postion)
		case 3:
			Move_Right(&Traveler.Postion)
		default:
			fmt.Printf("??????? %d\n", n)
		}
	}
	// fmt.Printf("Created a traveler with letter %c\n", Symbol)
	defer wg.Done()

	Traveler.Id = Id
	Traveler.Symbol = Symbol

	Traces.Last = -1

	Traveler.Postion.X = rand.Intn(Board_Width)
	Traveler.Postion.Y = rand.Intn(Board_Height)
	Store_Trace()
	Nr_of_Steps = Min_Steps + rand.Intn(Max_Steps-Min_Steps)
	Time_Stamp = time.Since(Start_Time)

	// accept start
	<-ready

	for step := 0; step < Nr_of_Steps; step++ {
		time.Sleep(Min_Delay + time.Duration(rand.Intn(int(Max_Delay)-int(Min_Delay))))
		Make_Step()
		Store_Trace()
		Time_Stamp = time.Since(Start_Time)

	}
	printer <- Traces
}

var rSymbol rune = 'A'
var wg sync.WaitGroup

func main() {
	for i := 0; i < Nr_Of_Travelers; i++ {
		Seeds[i] = rand.Intn(1000000)
	}

	fmt.Printf("-1 %d %d %d\n", Nr_Of_Travelers, Board_Width, Board_Height)

	p := make(chan Traces_Sequence_Type)
	go Printer(p)

	ready := make(chan T)
	wg.Add(Nr_Of_Travelers)

	// fmt.Println(Nr_Of_Travelers)
	for i := 0; i < Nr_Of_Travelers; i++ {
		go Traveler_Task_Type(i, Seeds[i], rSymbol, ready, p)
		rSymbol = rune(rSymbol + 1)
	}
	close(ready)

	wg.Wait()
}
