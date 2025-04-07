package main

import (
	"fmt"
	"math/rand"
	"sync"
	"time"
	"unicode"
)

const Nr_Of_Travelers int = 15

const Min_Steps int = 10
const Max_Steps int = 100

const Min_Delay time.Duration = 10 * time.Millisecond
const Max_Delay time.Duration = 50 * time.Millisecond

const Board_Width int = 15
const Board_Height int = 15

var Start_Time time.Time = time.Now()
var Seeds [Nr_Of_Travelers]int

type Position_Type struct {
	X int
	Y int
}

// type Board_Cell struct {
// 	ch chan struct{} = make(chan struct{}, 1
// }

// var Board [Board_Width][Board_Height]Board_Cell

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
	// println(Trace.Symbol)
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
	Id       int
	Symbol   rune
	Position Position_Type
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

	var Done bool = false

	var direction int

	Store_Trace := func() {
		Traces.Trace_Array[Traces.Last].Time_Stamp = Time_Stamp
		Traces.Trace_Array[Traces.Last].Id = Traveler.Id
		Traces.Trace_Array[Traces.Last].Position = Traveler.Position
		Traces.Trace_Array[Traces.Last].Symbol = Symbol
		Traces.Last = Traces.Last + 1
		// println(Traces.Trace_Array[Traces.Last].Symbol)
	}

	Make_Step := func() {
		// if unicode.IsLower(Traveler.Symbol) {
		// 	// Done = true
		// 	return
		// }

		var n int
		n = rand.Intn(4)
		var New_Position Position_Type = Traveler.Position

		switch direction {
		case 0:
			Move_Up(&New_Position)
		case 1:
			Move_Down(&New_Position)
		case 2:
			Move_Left(&New_Position)
		case 3:
			Move_Right(&New_Position)
		default:
			fmt.Printf("??????? %d\n", n)
		}

		select {
		case <-Board[New_Position.X][New_Position.Y]:
			// Board[New_Position.X][New_Position.Y].mu.Lock()
			Old_Position := Traveler.Position
			Traveler.Position = New_Position
			Board[Old_Position.X][Old_Position.Y] <- struct{}{}

		case <-time.After(Max_Delay):
			// println("timeout")
			Symbol = unicode.ToLower(Traveler.Symbol)
			// Symbol = 'a'
			// println(Traveler.Symbol)
			Done = true
		}

		// <-Board[New_Position.X][New_Position.Y]
		// Board[Traveler.Position.X][Traveler.Position.Y] <- struct{}{}
		// Traveler.Position = New_Position

	}
	// fmt.Printf("Created a traveler with letter %c\n", Symbol)
	defer wg.Done()

	Traveler.Id = Id
	Traveler.Symbol = Symbol

	Traces.Last = 0

	Traveler.Position.X = Traveler.Id
	Traveler.Position.Y = Traveler.Id
	<-Board[Traveler.Position.X][Traveler.Position.Y]

	if Traveler.Id%2 == 0 {
		direction = rand.Intn(2)
	} else {
		direction = rand.Intn(2) + 2
	}

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
		if Done == true {
			break
		}

	}
	printer <- Traces
}

var rSymbol rune = 'A'
var wg sync.WaitGroup
var Board [Board_Width][Board_Height]chan struct{}

func main() {
	for i := 0; i < Board_Width; i++ {
		for j := 0; j < Board_Height; j++ {
			Board[i][j] = make(chan struct{}, 1)
			Board[i][j] <- struct{}{}
		}
	}

	for i := 0; i < Nr_Of_Travelers; i++ {
		Seeds[i] = rand.Intn(1000000)
	}

	fmt.Printf("-1 %d %d %d\n", Nr_Of_Travelers, Board_Width, Board_Height)

	p := make(chan Traces_Sequence_Type, Nr_Of_Travelers)
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
