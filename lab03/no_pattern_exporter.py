import messagebox

def export_to_csv_no_pattern(headers, rows, filename):
    """Реализация экспорта в CSV без использования паттерна"""
    try:
        h_lower = [h.lower().strip() for h in headers]
        price_idx = h_lower.index("цена") if "цена" in h_lower else -1
        qty_idx = h_lower.index("кол-во") if "кол-во" in h_lower else -1
        total_sum = 0.0

        with open(filename, 'w', encoding='utf-8') as f:
            f.write(",".join(headers) + "\n")
            
            for row in rows:
                f.write(",".join(row) + "\n")
                if price_idx != -1 and qty_idx != -1:
                    try:
                        total_sum += float(row[price_idx]) * float(row[qty_idx])
                    except: pass
            
            f.write(f"\nИТОГО,,,{total_sum:.2f}\n")
        return True
    except Exception as e:
        messagebox.showerror("Ошибка", str(e))
        return False

def export_to_html_no_pattern(headers, rows, filename):
    """Реализация экспорта в HTML без использования паттерна"""
    try:
        h_lower = [h.lower().strip() for h in headers]
        price_idx = h_lower.index("цена") if "цена" in h_lower else -1
        qty_idx = h_lower.index("кол-во") if "кол-во" in h_lower else -1
        total_sum = 0.0

        with open(filename, 'w', encoding='utf-8') as f:
            f.write("<html><body><table border='1'><tr>")
            for h in headers:
                f.write(f"<th>{h}</th>")
            f.write("</tr>")
            
            for row in rows:
                f.write("<tr>")
                for val in row:
                    f.write(f"<td>{val}</td>")
                f.write("</tr>")
                
                if price_idx != -1 and qty_idx != -1:
                    try:
                        total_sum += float(row[price_idx]) * float(row[qty_idx])
                    except: pass
            
            f.write(f"</table><h3>ОБЩАЯ СУММА: {total_sum:.2f}</h3></body></html>")
        return True
    except Exception as e:
        messagebox.showerror("Ошибка", str(e))
        return False