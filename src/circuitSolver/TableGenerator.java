package circuitSolver;

import java.util.*;

class Result {
    private int x;
    private int y;
    private String name;
    private int value;

    public Result(int x, int y, String name, int value) {
        this.x = x;
        this.y = y;
        this.name = name;
        this.value = value;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }
}

class TableGenerator {
    private List<Result> list;
    private Result[][] table;
    private int[][] detected;
    private int[][] circuitTable;
    private int xx, yy;
    private int node = 0;
    private String output = new String();

    private void preProcessing() {
        table = new Result[xx][yy];
        detected = new int[xx][yy];

        int[][] column = new int[yy][2];
        for (int i = 0; i < column.length; i++) {
            for (int j = 0; j < column[0].length; j++) {
                column[i][j] = -1;
            }
        }

        int[][] row = new int[xx][2];
        for (int i = 0; i < row.length; i++) {
            for (int j = 0; j < row[0].length; j++) {
                row[i][j] = -1;
            }
        }

        for (Result r : list) {
            table[r.getX()][r.getY()] = r;
            switch (r.getName()) {
            case "wl1":
                column[r.getY()][0] = r.getX(); // 列的起点
                row[r.getX()][1] = r.getY(); // 行的终点
                break;
            case "wl2":
                column[r.getY()][1] = r.getX(); // 列的终点
                row[r.getX()][1] = r.getY(); // 行的终点
                break;
            case "wl3":
                column[r.getY()][1] = r.getX(); // 列的终点
                row[r.getX()][0] = r.getY(); // 行的起点
                break;
            case "wl4":
                column[r.getY()][0] = r.getX(); // 列的起点
                row[r.getX()][0] = r.getY(); // 行的起点
                break;
            case "wt1":
                column[r.getY()][0] = r.getX(); // 列的起点
                break;
            case "wt2":
                row[r.getX()][1] = r.getY(); // 行的终点
                break;
            case "wt3":
                column[r.getY()][1] = r.getX(); // 列的终点
                break;
            case "wt4":
                row[r.getX()][0] = r.getY(); // 行的起点
                break;
            }
        }

        // init the circuitTable
        circuitTable = new int[xx * 3][yy * 3];
        // set all to zero
        for (int i = 0; i < circuitTable.length; i++) {
            for (int j = 0; j < circuitTable[0].length; j++) {
                circuitTable[i][j] = 0;
            }
        }

        for (int i = 0; i < row.length; i++) {
            if (row[i][0] != -1 && row[i][1] != -1) {
                for (int j = row[i][0] + 1; j < row[i][1]; j++) {
                    if (table[i][j] == null)
                        table[i][j] = new Result(i, j, "w", 0);

                    circuitTable[i * 3 + 1][j * 3 + 0] = 1; // 左
                    circuitTable[i * 3 + 1][j * 3 + 1] = 1; // 中
                    circuitTable[i * 3 + 1][j * 3 + 2] = 1; // 右
                }
            } else if (!(row[i][0] == row[i][1] && row[i][0] == -1)) {
                throw new IllegalArgumentException("节点识别错误...");
            }
        }

        for (int i = 0; i < column.length; i++) {
            if (column[i][0] != -1 && column[i][1] != -1) {
                for (int j = column[i][0] + 1; j < column[i][1]; j++) {
                    if (table[j][i] == null)
                        table[j][i] = new Result(i, j, "w", 0);

                    circuitTable[j * 3 + 0][i * 3 + 1] = 1; // 上
                    circuitTable[j * 3 + 1][i * 3 + 1] = 1; // 中
                    circuitTable[j * 3 + 2][i * 3 + 1] = 1; // 下
                }
            } else if (!(column[i][0] == column[i][1] && column[i][0] == -1)) {
                throw new IllegalArgumentException("节点识别错误...");
            }
        }

        // add all the elements
        for (int i = 0; i < xx; i++) {
            for (int j = 0; j < yy; j++) {
                if (table[i][j] != null) {
                    switch (table[i][j].getName()) {
                    case "wl1":
                        circuitTable[i * 3 + 1][j * 3 + 0] = 1; // 左
                        circuitTable[i * 3 + 1][j * 3 + 1] = 1; // 中
                        circuitTable[i * 3 + 2][j * 3 + 1] = 1; // 下
                        break;
                    case "wl2":
                        circuitTable[i * 3 + 0][j * 3 + 1] = 1; // 上
                        circuitTable[i * 3 + 1][j * 3 + 1] = 1; // 中
                        circuitTable[i * 3 + 1][j * 3 + 0] = 1; // 左
                        break;
                    case "wl3":
                        circuitTable[i * 3 + 0][j * 3 + 1] = 1; // 上
                        circuitTable[i * 3 + 1][j * 3 + 1] = 1; // 中
                        circuitTable[i * 3 + 1][j * 3 + 2] = 1; // 右
                        break;
                    case "wl4":
                        circuitTable[i * 3 + 1][j * 3 + 1] = 1; // 中
                        circuitTable[i * 3 + 1][j * 3 + 2] = 1; // 右
                        circuitTable[i * 3 + 2][j * 3 + 1] = 1; // 下
                        break;
                    case "wt1":
                        circuitTable[i * 3 + 1][j * 3 + 0] = 1; // 左
                        circuitTable[i * 3 + 1][j * 3 + 1] = 1; // 中
                        circuitTable[i * 3 + 1][j * 3 + 2] = 1; // 右
                        circuitTable[i * 3 + 2][j * 3 + 1] = 1; // 下
                        break;
                    case "wt2":
                        circuitTable[i * 3 + 0][j * 3 + 1] = 1; // 上
                        circuitTable[i * 3 + 1][j * 3 + 0] = 1; // 左
                        circuitTable[i * 3 + 1][j * 3 + 1] = 1; // 中
                        circuitTable[i * 3 + 2][j * 3 + 1] = 1; // 下
                        break;
                    case "wt3":
                        circuitTable[i * 3 + 0][j * 3 + 1] = 1; // 上
                        circuitTable[i * 3 + 1][j * 3 + 0] = 1; // 左
                        circuitTable[i * 3 + 1][j * 3 + 1] = 1; // 中
                        circuitTable[i * 3 + 1][j * 3 + 2] = 1; // 右
                        break;
                    case "wt4":
                        circuitTable[i * 3 + 0][j * 3 + 1] = 1; // 上
                        circuitTable[i * 3 + 1][j * 3 + 1] = 1; // 中
                        circuitTable[i * 3 + 1][j * 3 + 2] = 1; // 右
                        circuitTable[i * 3 + 2][j * 3 + 1] = 1; // 下
                        break;
                    case "wp":
                        circuitTable[i * 3 + 0][j * 3 + 1] = 1; // 上
                        circuitTable[i * 3 + 1][j * 3 + 0] = 1; // 左
                        circuitTable[i * 3 + 1][j * 3 + 1] = 1; // 中
                        circuitTable[i * 3 + 1][j * 3 + 2] = 1; // 右
                        circuitTable[i * 3 + 2][j * 3 + 1] = 1; // 下
                        break;
                    }
                }
            }
        }

        for (int i = 0; i < xx; i++) {
            for (int j = 0; j < yy; j++) {
                if (table[i][j] == null) {
                    table[i][j] = new Result(i, j, "b", 0);
                    detected[i][j] = -1;
                }
                if (table[i][j].getName().charAt(0) != 'w') {
                    table[i][j].setX(-1);
                    table[i][j].setY(-1);
                } else {
                    detected[i][j] = -1;
                }
            }
        }
    }

    private void detect(int i, int j, int direction) {
        if (i < 0 || i > xx - 1 || j < 0 || j > yy - 1) {
            throw new IndexOutOfBoundsException("Stable error");
        }

        String type = table[i][j].getName().split("_")[0];
        int iChange = (direction - 2) % 2;
        int jChange = (direction - 3) % 2;

        if (type.equals("w") && direction != 0) {
            // 直导线在探测过程中仅充当传递节点, 如果探测结束后还有直导线没有被探测, 则直接当作纯直导线处理
            table[i][j].setValue(1 - direction % 2);
            detect(i - iChange, j + jChange, direction);
        } else if (type.equals("w") && direction == 0) {
            // 否则利用try-catch强行探测
            try { // 横向直导线
                detect(i, j - 1, 2);
                detect(i, j + 1, 4);
            } catch (IndexOutOfBoundsException e) {
                table[i][j].setValue(0);
            }

            try { // 纵向直导线
                detect(i + 1, j, 1);
                detect(i - 1, j, 3);
            } catch (IndexOutOfBoundsException e) {
                table[i][j].setValue(1);
            }

        } else if (type.equals("wl1")) {
            if (direction == 1 || direction == 2) {
                throw new IllegalArgumentException("节点识别错误. 位置: [" + i + ", " + j + "]");
            } else {
                if (direction == 0) {
                    detect(i + 1, j, 1);
                    detect(i, j - 1, 2);
                } else if (direction == 3) {
                    detect(i, j - 1, 2);
                } else if (direction == 4) {
                    detect(i + 1, j, 1);
                }
            }
        } else if (type.equals("wl2")) {
            if (direction == 2 || direction == 3) {
                throw new IllegalArgumentException("节点识别错误. 位置: [" + i + ", " + j + "]");
            } else {
                if (direction == 0) {
                    detect(i, j - 1, 2);
                    detect(i - 1, j, 3);
                } else if (direction == 1) {
                    detect(i, j - 1, 2);
                } else if (direction == 4) {
                    detect(i - 1, j, 3);
                }
            }
        } else if (type.equals("wl3")) {
            if (direction == 3 || direction == 4) {
                throw new IllegalArgumentException("节点识别错误. 位置: [" + i + ", " + j + "]");
            } else {
                if (direction == 0) {
                    detect(i - 1, j, 3);
                    detect(i, j + 1, 4);
                } else if (direction == 1) {
                    detect(i, j + 1, 4);
                } else if (direction == 2) {
                    detect(i - 1, j, 3);
                }
            }
        } else if (type.equals("wl4")) {
            if (direction == 4 || direction == 1) {
                throw new IllegalArgumentException("节点识别错误. 位置: [" + i + ", " + j + "]");
            } else {
                if (direction == 0) {
                    detect(i, j + 1, 4);
                    detect(i + 1, j, 1);
                } else if (direction == 2) {
                    detect(i + 1, j, 1);
                } else if (direction == 3) {
                    detect(i, j + 1, 4);
                }
            }
        } else if (type.equals("wt1")) {
            if (direction == 1) {
                throw new IllegalArgumentException("节点识别错误. 位置: [" + i + ", " + j + "]");
            } else {
                if (direction == 0) {
                    detect(i + 1, j, 1);
                    detect(i, j - 1, 2);
                    detect(i, j + 1, 4);
                } else if (direction == 2) {
                    detect(i + 1, j, 1);
                    detect(i, j - 1, 2);
                } else if (direction == 3) {
                    detect(i, j - 1, 2);
                    detect(i, j + 1, 4);
                } else if (direction == 4) {
                    detect(i, j + 1, 4);
                    detect(i + 1, j, 1);
                }
            }
        } else if (type.equals("wt2")) {
            if (direction == 2) {
                throw new IllegalArgumentException("节点识别错误. 位置: [" + i + ", " + j + "]");
            } else {
                if (direction == 0) {
                    detect(i + 1, j, 1);
                    detect(i, j - 1, 2);
                    detect(i - 1, j, 3);
                } else if (direction == 1) {
                    detect(i + 1, j, 1);
                    detect(i, j - 1, 2);
                } else if (direction == 3) {
                    detect(i, j - 1, 2);
                    detect(i - 1, j, 3);
                } else if (direction == 4) {
                    detect(i - 1, j, 3);
                    detect(i + 1, j, 1);
                }
            }
        } else if (type.equals("wt3")) {
            if (direction == 3) {
                throw new IllegalArgumentException("节点识别错误. 位置: [" + i + ", " + j + "]");
            } else {
                if (direction == 0) {
                    detect(i, j - 1, 2);
                    detect(i - 1, j, 3);
                    detect(i, j + 1, 4);
                } else if (direction == 1) {
                    detect(i, j - 1, 2);
                    detect(i, j + 1, 4);
                } else if (direction == 2) {
                    detect(i, j - 1, 2);
                    detect(i - 1, j, 3);
                } else if (direction == 4) {
                    detect(i - 1, j, 3);
                    detect(i, j + 1, 4);
                }
            }
        } else if (type.equals("wt4")) {
            if (direction == 4) {
                throw new IllegalArgumentException("节点识别错误. 位置: [" + i + ", " + j + "]");
            } else {
                if (direction == 0) {
                    detect(i + 1, j, 1);
                    detect(i - 1, j, 3);
                    detect(i, j + 1, 4);
                } else if (direction == 1) {
                    detect(i, j + 1, 4);
                    detect(i + 1, j, 1);
                } else if (direction == 2) {
                    detect(i + 1, j, 1);
                    detect(i - 1, j, 3);
                } else if (direction == 3) {
                    detect(i - 1, j, 3);
                    detect(i, j + 1, 4);
                }
            }
        } else if (type.equals("w+")) {
            if (direction == 0) {
                detect(i + 1, j, 1);
                detect(i, j - 1, 2);
                detect(i - 1, j, 3);
                detect(i, j + 1, 4);
            } else if (direction == 1) {
                detect(i + 1, j, 1);
                detect(i, j - 1, 2);
                detect(i, j + 1, 4);
            } else if (direction == 2) {
                detect(i + 1, j, 1);
                detect(i, j - 1, 2);
                detect(i - 1, j, 3);
            } else if (direction == 3) {
                detect(i, j - 1, 2);
                detect(i - 1, j, 3);
                detect(i, j + 1, 4);
            } else if (direction == 4) {
                detect(i + 1, j, 1);
                detect(i - 1, j, 3);
                detect(i, j + 1, 4);
            }
        } else {
            if (table[i][j].getX() < 0)
                table[i][j].setX(node);
            else
                table[i][j].setY(node);
            detected[i][j] = direction % 2 - 2;
            return;
        }

        detected[i][j] = node;
        return;
    }

    private void generateTableAndDetect() {
        for (int i = 0; i < xx; i++) {
            for (int j = 0; j < yy; j++) {
                if (table[i][j].getName().charAt(0) == 'w' && detected[i][j] <= 0) {
                    node++;
                    detect(i, j, 0);
                }
            }
        }

        for (int i = 0; i < xx; i++) {
            for (int j = 0; j < yy; j++) {
                if (table[i][j].getName().equals("w")) {
                    table[i][j].setName("w" + table[i][j].getValue());
                    table[i][j].setValue(0);
                }
            }
        }
    }

    private void generateMeshCircuit() {
    }

    private void generateOutput() {
        for (int i = 0; i < xx; i++) {
            for (int j = 0; j < yy; j++) {
                Result temp = table[i][j];
                if (temp.getName().charAt(0) != 'w' && temp.getName().charAt(0) != 'b') {
                    output += new String(table[i][j].getName() + "\t" + table[i][j].getValue() + "\t" + temp.getX()
                            + "\t" + temp.getY() + "\n");
                }
            }
        }
    }

    public TableGenerator(int xx, int yy, List<Result> list) {
        this.list = list;
        this.xx = xx;
        this.yy = yy;

        preProcessing();
        generateTableAndDetect();
        generateMeshCircuit();
        generateOutput();
    }

    public String getOutput() {
        return output;
    }

    public Result[][] getTable() {
        return table;
    }

    public int[][] getDetected() {
        return detected;
    }

    public int[][] getCircuitTable() {
        return circuitTable;
    }
}
