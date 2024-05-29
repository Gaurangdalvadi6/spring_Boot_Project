package com.hotel.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hotel.exception.InvalidBookingRequestException;
import com.hotel.exception.ResourceNotFoundException;
import com.hotel.model.BookedRoom;
import com.hotel.model.Room;
import com.hotel.response.BookingResponse;
import com.hotel.response.RoomResponse;
import com.hotel.service.IBookingService;
import com.hotel.service.IRoomService;

import lombok.RequiredArgsConstructor;

@CrossOrigin("http://localhost:3000")
@RequiredArgsConstructor
@RestController
@RequestMapping("/bookings")
public class BookingController {

	@Autowired
	private IBookingService bookingService;
	
	@Autowired
	private IRoomService roomService;

	@GetMapping("all-bookings")
	public ResponseEntity<List<BookingResponse>> getAllBookings() {
		List<BookedRoom> bookings = bookingService.getAllBookings();
		List<BookingResponse> bookingResponses = new ArrayList<>();
		for (BookedRoom booking : bookings) {
			BookingResponse bookingResponse = getBookingResponse(booking);
			bookingResponses.add(bookingResponse);
		}
		return ResponseEntity.ok(bookingResponses);
	}

	@GetMapping("/confirmation/{confirmationCode}")
	public ResponseEntity<?> getBookingByConfiramtionCode(@PathVariable String confirmationCode) {
		try {
			BookedRoom booking = bookingService.findByBookingConfiramtionCode(confirmationCode);
			BookingResponse bookingResponse = getBookingResponse(booking);
			return ResponseEntity.ok(bookingResponse);
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}

	@PostMapping("/room/{roomId}/booking")
	public ResponseEntity<?> saveBooking(@PathVariable Long roomId, @RequestBody BookedRoom bookingRequest) {
		try {
			String confirmationCode = bookingService.saveBooking(roomId, bookingRequest);
			return ResponseEntity
					.ok("Room booked Successfully, Your booking confirmation Code is :" + confirmationCode);
		} catch (InvalidBookingRequestException ex) {
			return ResponseEntity.badRequest().body(ex.getMessage());
		}
	}
	
	@DeleteMapping("/booking/{bookingId}/delete")
	public void cancelBooking(@PathVariable Long bookingId) {
		bookingService.cancelBooking(bookingId);
	}
	
	private BookingResponse getBookingResponse(BookedRoom booking) {
		Room theRoom = roomService.getRoomById(booking.getRoom().getId()).get();
		RoomResponse room = new RoomResponse(theRoom.getId(), theRoom.getRoomType(), theRoom.getRoomPrice());
		return new BookingResponse(booking.getBookingId(),booking.getCheckInDate(),
									booking.getCheckOutDate(),booking.getGuestFullName(),
									booking.getGuestEmail(),booking.getNumOfAdults(),
									booking.getNumOfChildren(),booking.getTotalNumOfGuest(),
									booking.getBookingConfirmationCode(),room);
	}
}
