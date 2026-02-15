import axios from "./axiosConfig";

const BASE = "/api/payments";

export const createOrder = (bookingId) => {
  return axios.post(`${BASE}/create-order/${bookingId}`);
};

/**
 * @param {{ razorpay_order_id: string, razorpay_payment_id: string, razorpay_signature: string }} payload
 */
export const verifyPayment = (payload) => {
  return axios.post(`${BASE}/verify`, payload);
};
