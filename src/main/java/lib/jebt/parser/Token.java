package lib.jebt.parser;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;


/**
 * Represents one token read while going through a Jebt template.
 */
public class Token {

    public static Token EOD = new Token();

    public Token(TokenType type, String text) {

        if (type == TokenType.END_OF_DOCUMENT) {
            throw new RuntimeException("Don't instantiate END_OF_DOCUMENT tokens, use static Token.EOD instead.");
        }

        this.type = type;
        this.text = text;
    }

    public Token(Cell cell) {

        if (cell == null) {
            this.type = TokenType.NEW_BLANK_CELL;
        } else if (cell.getCellTypeEnum() == CellType.STRING) {
            if (cell.getStringCellValue() == null || "".equals(cell.getStringCellValue())) {
                this.type = TokenType.NEW_BLANK_CELL;
            } else {
                this.type = TokenType.NEW_TEXT_CELL;
            }
        } else if (cell.getCellTypeEnum() == CellType.BLANK) {
            this.type = TokenType.NEW_BLANK_CELL;
        } else {
            this.type = TokenType.NEW_NON_TEXT_CELL;
        }
        this.cell = cell;
    }

    // USed only by EOD.
    private Token() {
        this.type = TokenType.END_OF_DOCUMENT;
        this.text = null;
    }

    private TokenType type;
    private String text;
    private Cell cell;

    public TokenType getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    public Cell getCell() {
        return cell;
    }

    public enum TokenType {TEXT, EXPRESSION, LOOP,
        NEW_BLANK_CELL, // Blank cells are either BLANK cell type, or String cells with empty content.
        NEW_BLANK_ROW, // Blank row can contain some blank cells. They will then be ignored and not returned as tokens.
        NEW_TEXT_CELL, // Empty Text cells are returned as blank cells.
        NEW_NON_TEXT_CELL, NEW_ROW, END_OF_DOCUMENT}
}
