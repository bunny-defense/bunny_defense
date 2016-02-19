open Printf

(*
let file = "example.dat"
let message = "Hello!"
  
let () =
  (* Write message to file *)
  let oc = open_out file in    (* create or truncate file, return channel *)
  fprintf oc "%s\n" message;   (* write something *)   
  close_out oc;                (* flush and close the channel *)
*)

let frs (x,y,z,t) = x;;
let scd (x,y,z,t) = y;;
let trd (x,y,z,t) = z;;
let frt (x,y,z,t) = t;;

let n_wave = 5;;
let difficulty = 20;;
let bunnies = [|"Bunny",1,200,1 ; "Heavy_Bunny",3,50,1 ; "Hare",1,35,3 ; "Otter",15,10,10|]
(* List of (bunny type, difficulty points, inverse rarity, first possible wave of appearance) *)
let sum_rarity =
  let res = ref 0 in
  for i=0 to Array.length(bunnies)-1 do
    res := (!res) + (trd bunnies.(i))
  done;
  !res;;

let file_name = "wave_" ^ (string_of_int n_wave);;
let oc = open_out file_name;;

let chose_bunny i =
  (* i is a random integer between 0 and sum_rarity-1, choses a random bunny based on rarity *)
  let j = ref (i - (trd bunnies.(0)))
  and bunny_index = ref 0 in
  while (!j >= 0) && (!bunny_index < Array.length(bunnies)) do
    incr bunny_index;
    j:= (!j) - (trd bunnies.(!bunny_index))
  done;
  !bunny_index;;


let rec wave n t=
  (* Prints a wave corresponding to the difficulty n in file wave_{n_wave}, starting at time t *)
  match n with
  |n when n <= 0
      -> ()
  |n  -> let bunny = bunnies.(chose_bunny (Random.int sum_rarity)) in
	let diff_decr = ref 0 in 
	if n_wave >= frt bunny then
	  begin
	    let diff = scd bunny in
	    fprintf oc "%f, %s\n" t (frs bunny);
	    diff_decr := diff
	  end;
	wave (n-(!diff_decr)) (t+.1.);;

wave difficulty 0.;;
