from abc import ABC, abstractmethod
from tkinter import messagebox

class ReportExporter(ABC):
    """Абстрактный класс экспорта. Определяет алгоритм формирования отчета"""
    
    def export(self, headers, rows, filename):
        """Общая логика сохранения файла"""
        try:
            h_lower = [h.lower().strip() for h in headers]
            price_idx = h_lower.index("цена") if "цена" in h_lower else -1
            qty_idx = h_lower.index("кол-во") if "кол-во" in h_lower else -1

            total_sum = 0.0

            with open(filename, 'w', encoding='utf-8') as f:
                f.write(self._start_document())
                
                f.write(self._format_headers(headers))
                
                for row in rows:
                    f.write(self._format_row(row))
                    
                    if price_idx != -1 and qty_idx != -1:
                        try:
                            price = float(row[price_idx])
                            qty = float(row[qty_idx])
                            total_sum += price * qty
                        except (ValueError, IndexError):
                            pass
                
                f.write(self._end_document(total_sum))
                
                hook_data = self._add_watermark()
                if hook_data:
                    f.write(hook_data)
            return True
        except Exception as e:
            messagebox.showerror("Ошибка", f"Не удалось сохранить файл: {e}")
            return False

    @abstractmethod
    def _start_document(self): pass

    @abstractmethod
    def _format_headers(self, headers): pass

    @abstractmethod
    def _format_row(self, row): pass

    @abstractmethod
    def _end_document(self, total_price): pass

    def _add_watermark(self):
        return None

class CSVExporter(ReportExporter):
    def _start_document(self): return ""
    
    def _format_headers(self, headers):
        return ",".join(headers) + "\n"
    
    def _format_row(self, row):
        return ",".join(row) + "\n"
    
    def _end_document(self, total_price):
        return f"\nИТОГО,,,{total_price:.2f}\n"

class HTMLExporter(ReportExporter):
    def _start_document(self):
        return "<html><head><meta charset='utf-8'></head><body style='font-family:Arial;'><h2>Отчет по продажам</h2><table border='1' cellspacing='0' cellpadding='5'>"
    
    def _format_headers(self, headers):
        res = "<tr style='background:#f2f2f2'>"
        for h in headers:
            res += f"<th>{h}</th>"
        return res + "</tr>"
    
    def _format_row(self, row):
        res = "<tr>"
        for val in row:
            res += f"<td>{val}</td>"
        return res + "</tr>"
    
    def _end_document(self, total_price):
        return f"</table><h3 style='color:#2c3e50;'>ОБЩАЯ СУММА: {total_price:.2f} руб.</h3></body></html>"