import { createContext, useCallback, useContext, useEffect, useMemo, useRef, useState } from "react";

const ToastContext = createContext(null);

const DEFAULT_DURATION = 3600;
let toastSequence = 0;

function ToastViewport({ toasts, onClose }) {
  return (
    <div className="toast-viewport" aria-live="polite" aria-atomic="true">
      {toasts.map((toast) => (
        <article
          key={toast.id}
          className="toast-card"
          data-tone={toast.tone}
          role={toast.tone === "error" ? "alert" : "status"}
        >
          <div className="toast-body">
            <strong className="toast-title">{toast.title}</strong>
            <p className="toast-message">{toast.message}</p>
          </div>

          <button
            type="button"
            className="toast-close"
            aria-label="Đóng thông báo"
            onClick={() => onClose(toast.id)}
          >
            ×
          </button>
        </article>
      ))}
    </div>
  );
}

export function ToastProvider({ children }) {
  const [toasts, setToasts] = useState([]);
  const timersRef = useRef(new Map());

  const removeToast = useCallback((toastId) => {
    const timer = timersRef.current.get(toastId);
    if (timer) {
      window.clearTimeout(timer);
      timersRef.current.delete(toastId);
    }

    setToasts((current) => current.filter((toast) => toast.id !== toastId));
  }, []);

  useEffect(() => {
    return () => {
      timersRef.current.forEach((timer) => window.clearTimeout(timer));
      timersRef.current.clear();
    };
  }, []);

  const pushToast = useCallback((payload) => {
    toastSequence += 1;
    const toastId = toastSequence;
    const toast = {
      id: toastId,
      title: payload.title,
      message: payload.message,
      tone: payload.tone || "info",
    };

    setToasts((current) => [...current, toast]);

    const duration = payload.duration ?? DEFAULT_DURATION;
    if (duration > 0) {
      const timer = window.setTimeout(() => {
        removeToast(toastId);
      }, duration);
      timersRef.current.set(toastId, timer);
    }

    return toastId;
  }, [removeToast]);

  const api = useMemo(() => ({
    success(message, title = "Thành công") {
      return pushToast({ title, message, tone: "success" });
    },
    error(message, title = "Không thể thực hiện") {
      return pushToast({ title, message, tone: "error", duration: 4600 });
    },
    warning(message, title = "Lưu ý") {
      return pushToast({ title, message, tone: "warning", duration: 4200 });
    },
    info(message, title = "Thông báo") {
      return pushToast({ title, message, tone: "info" });
    },
    remove(toastId) {
      removeToast(toastId);
    },
  }), [pushToast, removeToast]);

  return (
    <ToastContext.Provider value={api}>
      {children}
      <ToastViewport toasts={toasts} onClose={removeToast} />
    </ToastContext.Provider>
  );
}

export function useToast() {
  const context = useContext(ToastContext);

  if (!context) {
    throw new Error("useToast phải được dùng bên trong ToastProvider.");
  }

  return context;
}
