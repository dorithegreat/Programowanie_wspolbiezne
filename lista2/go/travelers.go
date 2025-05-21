package main

import (
	"fmt"
	"math/rand"
	"sync"
	"time"
	"unicode"
)

const Nr_Of_Travelers int = 26

const Min_Steps int = 10
const Max_Steps int = 100

const Min_Delay time.Duration = 10 * time.Millisecond
const Max_Delay time.Duration = 50 * time.Millisecond

const Board_Width int = 15
const Board_Height int = 15

var Start_Time time.Time = time.Now()
var Seeds [Nr_Of_Travelers]int

var evict chan int = make(chan int)
var evict_confirmation chan struct{} = make(chan struct{})

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
func Printer(Report chan Traces_Sequence_Type, Stop chan struct{}) {
	// defer wg.Done()
	for {
		select {
		case report := <-Report:
			Print_Traces(report)

		case <-Stop:
			return
		}
		// Print_Traces(<-Report)
	}
}

type Traveler_Type struct {
	Id       int
	Symbol   rune
	Position Position_Type
}

type Board_Cell_Type struct {
	Occupied bool
	Wildcard int

	Occupy chan struct{}
	Leave  chan struct{}
	Stop   chan struct{}

	Add_Wildcard    chan int
	Remove_Wildcard chan struct{}
	// TODO: figure out the rest of the channels
}

func Board_Cell(cell *Board_Cell_Type) {
	var Has_Wildcard = false
	cell.Occupied = false

	for {
		if cell.Occupied {
			select {
			case <-cell.Leave:
				cell.Occupied = false
			default:
			}
		}

		if !cell.Occupied {
			select {
			case <-cell.Occupy:
				cell.Occupied = true
			case cell.Wildcard = <-cell.Add_Wildcard:
				if !Has_Wildcard {
					Has_Wildcard = false
				}
			default:
			}
		}

		select {
		case <-cell.Stop:
			return

		case <-cell.Remove_Wildcard:
			cell.Wildcard = -1
			Has_Wildcard = false
		default:
		}
	}
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

	Store_Trace := func() {
		Traces.Trace_Array[Traces.Last].Time_Stamp = Time_Stamp
		Traces.Trace_Array[Traces.Last].Id = Traveler.Id
		Traces.Trace_Array[Traces.Last].Position = Traveler.Position
		Traces.Trace_Array[Traces.Last].Symbol = Traveler.Symbol
		Traces.Last = Traces.Last + 1
		// println(Traces.Trace_Array[Traces.Last].Symbol)
	}

	Make_Step := func() {

		var n int
		n = rand.Intn(4)
		var New_Position Position_Type = Traveler.Position
		var Old_Position Position_Type = Traveler.Position

		switch n {
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
		case Board[New_Position.X][New_Position.Y].Occupy <- struct{}{}:
			Traveler.Position = New_Position
			Time_Stamp = time.Since(Start_Time)
			Store_Trace()
			Board[Old_Position.X][Old_Position.Y].Leave <- struct{}{}
			// fmt.Println("moved")

		case <-time.After(Max_Delay):
			Traveler.Symbol = unicode.ToLower(Traveler.Symbol)
			Done = true
			// fmt.Println("timeout")
		}

	}
	// fmt.Printf("Created a traveler with letter %c\n", Symbol)
	defer println(Id)
	defer wg.Done()

	Traveler.Id = Id
	Traveler.Symbol = Symbol

	Traces.Last = 0

	for {
		// fmt.Println("A")
		Traveler.Position.X = rand.Intn(Board_Width)
		Traveler.Position.Y = rand.Intn(Board_Height)
		if !Board[Traveler.Position.X][Traveler.Position.Y].Occupied {
			Board[Traveler.Position.X][Traveler.Position.Y].Occupy <- struct{}{}
			break
		}

	}

	Store_Trace()
	Nr_of_Steps = Min_Steps + rand.Intn(Max_Steps-Min_Steps)
	Time_Stamp = time.Since(Start_Time)

	// accept start
	<-ready

	for step := 0; step < Nr_of_Steps; step++ {
		time.Sleep(Min_Delay + time.Duration(rand.Intn(int(Max_Delay)-int(Min_Delay))))
		Time_Stamp = time.Since(Start_Time)

		Make_Step()
		if Done == true {
			// Traveler.Symbol = unicode.ToLower(Traveler.Symbol)
			Store_Trace()
			break
		}

	}
	// fmt.Println("Ddne")
	printer <- Traces
}

var rSymbol rune = 'A'
var wg sync.WaitGroup
var Board [Board_Width][Board_Height]Board_Cell_Type

type Wildcard_Traveler_Type struct {
	Id         int
	Symbol     rune
	Position   Position_Type
	Lifestpan  time.Duration
	Start_Time time.Time
	Traces     Traces_Sequence_Type
	Finished   bool
}

func Wildcard_Manager(Stop chan struct{}, printer chan<- Traces_Sequence_Type) {
	Wildcards := []*Wildcard_Traveler_Type{}
	last_time := time.Now()
	id_counter := Nr_Of_Travelers

	Store_Trace := func(wildcard *Wildcard_Traveler_Type) {
		wildcard.Traces.Trace_Array[wildcard.Traces.Last].Time_Stamp = time.Now().Sub(Start_Time)
		wildcard.Traces.Trace_Array[wildcard.Traces.Last].Id = wildcard.Id
		wildcard.Traces.Trace_Array[wildcard.Traces.Last].Position = wildcard.Position
		wildcard.Traces.Trace_Array[wildcard.Traces.Last].Symbol = wildcard.Symbol
		wildcard.Traces.Last = wildcard.Traces.Last + 1
		// println(Traces.Trace_Array[Traces.Last].Symbol)
	}

	for {
		select {
		case <-Stop:
			fmt.Println("stopped")
			return

		case id := <-evict:
			fmt.Println("received evict")
			for i, v := range Wildcards {
				if v.Id == id {
					var n int
					n = rand.Intn(4)
					var New_Position Position_Type = v.Position
					var Old_Position Position_Type = v.Position

					switch n {
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

					Board[New_Position.X][New_Position.Y].Add_Wildcard <- v.Id
					<-evict_confirmation
					v.Position = New_Position
					Store_Trace(v)
					Wildcards[i] = v
					Board[Old_Position.X][Old_Position.Y].Remove_Wildcard <- struct{}{}
				}
			}
		case <-time.After(50 * time.Millisecond):
			// fmt.Println("timeout")

		}

		for i, v := range Wildcards {
			if !v.Finished && time.Now().Sub(v.Start_Time) > 200*time.Millisecond {
				v.Symbol = '.'
				v.Finished = true
				Store_Trace(v)
				printer <- v.Traces

				// v is a copy so we need to update the slice
				Wildcards[i] = v
			}

		}

		// fmt.Println(time.Now().Sub(last_time))

		if time.Now().Sub(last_time) > 500*time.Millisecond {
			// fmt.Println("created a wildcard")

			var Wildcard Wildcard_Traveler_Type
			X := rand.Intn(Board_Width)
			Y := rand.Intn(Board_Height)
			Digit := '0' + rand.Intn(10)

			Wildcard.Id = id_counter
			id_counter += 1

			Wildcard.Start_Time = time.Now()
			Wildcard.Position.X = X
			Wildcard.Position.Y = Y
			Wildcard.Symbol = rune(Digit)
			Wildcard.Finished = false

			Store_Trace(&Wildcard)
			Wildcards = append(Wildcards, &Wildcard)
			last_time = time.Now()

		}

		// select {}

	}
}

func main() {
	for i := 0; i < Board_Width; i++ {
		for j := 0; j < Board_Height; j++ {
			Board[i][j] = Board_Cell_Type{
				Occupy:          make(chan struct{}),
				Leave:           make(chan struct{}),
				Stop:            make(chan struct{}),
				Add_Wildcard:    make(chan int),
				Remove_Wildcard: make(chan struct{}),
			}

			go Board_Cell(&Board[i][j])
			// Board[i][j] <- struct{}{}
		}
	}

	p := make(chan Traces_Sequence_Type)
	s := make(chan struct{})
	go Printer(p, s)

	// evict := make(chan int)
	Stop := make(chan struct{})
	go Wildcard_Manager(Stop, p)

	for i := 0; i < Nr_Of_Travelers; i++ {
		Seeds[i] = rand.Intn(1000000)
	}

	fmt.Printf("-1 %d %d %d\n", Nr_Of_Travelers, Board_Width, Board_Height)

	ready := make(chan T)
	wg.Add(Nr_Of_Travelers)

	// fmt.Println(Nr_Of_Travelers)
	for i := 0; i < Nr_Of_Travelers; i++ {
		go Traveler_Task_Type(i, Seeds[i], rSymbol, ready, p)
		rSymbol = rune(rSymbol + 1)
	}
	close(ready)

	wg.Wait()

	fmt.Println("stopped waiting")

	// Stop <- struct{}{}
	close(Stop)
	for i := 0; i < Board_Width; i++ {
		for j := 0; j < Board_Height; j++ {
			Board[i][j].Stop <- struct{}{}
		}
	}
	// fmt.Println("stopped board")

	s <- struct{}{}

}
