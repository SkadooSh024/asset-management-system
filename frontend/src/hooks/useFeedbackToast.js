import { useEffect, useRef } from "react";
import { useToast } from "../components/common/ToastProvider";

function useFeedbackToast({ successMessage, errorMessage }) {
  const toast = useToast();
  const previousSuccessRef = useRef("");
  const previousErrorRef = useRef("");

  useEffect(() => {
    if (successMessage && successMessage !== previousSuccessRef.current) {
      toast.success(successMessage);
    }

    previousSuccessRef.current = successMessage || "";
  }, [successMessage, toast]);

  useEffect(() => {
    if (errorMessage && errorMessage !== previousErrorRef.current) {
      toast.error(errorMessage);
    }

    previousErrorRef.current = errorMessage || "";
  }, [errorMessage, toast]);
}

export default useFeedbackToast;
