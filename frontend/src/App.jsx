import AppRoutes from "./routes/AppRoutes";
import { ToastProvider } from "./components/common/ToastProvider";

function App() {
  return (
    <ToastProvider>
      <AppRoutes />
    </ToastProvider>
  );
}

export default App;
