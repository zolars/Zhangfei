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

    private void detect(int i, int j, int direction) {
        if (i < 0 || i > xx - 1 || j < 0 || j > yy - 1) {
            throw new IndexOutOfBoundsException("Stable error");
        }

        String type = table[i][j].getName().split("_")[0];
        int iChange = (direction - 2) % 2;
        int jChange = (direction - 3) % 2;

        if (type.equals("w") && direction != 0) {
            // ֱ������̽������н��䵱���ݽڵ�, ���̽���������ֱ����û�б�̽��, ��ֱ�ӵ�����ֱ���ߴ���
            table[i][j].setValue(1 - direction % 2);
            detect(i - iChange, j + jChange, direction);
        } else if (type.equals("w") && direction == 0) {
            // ��������try-catchǿ��̽��
            try { // ����ֱ����
                detect(i, j - 1, 2);
                detect(i, j + 1, 4);
            } catch (IndexOutOfBoundsException e) {
                table[i][j].setValue(0);
            }

            try { // ����ֱ����
                detect(i + 1, j, 1);
                detect(i - 1, j, 3);
            } catch (IndexOutOfBoundsException e) {
                table[i][j].setValue(1);
            }

        } else if (type.equals("wl1")) {
            if (direction == 1 || direction == 2) {
                throw new IllegalArgumentException("�ڵ�ʶ�����. λ��: [" + i + ", " + j + "]");
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
                throw new IllegalArgumentException("�ڵ�ʶ�����. λ��: [" + i + ", " + j + "]");
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
                throw new IllegalArgumentException("�ڵ�ʶ�����. λ��: [" + i + ", " + j + "]");
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
                throw new IllegalArgumentException("�ڵ�ʶ�����. λ��: [" + i + ", " + j + "]");
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
                throw new IllegalArgumentException("�ڵ�ʶ�����. λ��: [" + i + ", " + j + "]");
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
                throw new IllegalArgumentException("�ڵ�ʶ�����. λ��: [" + i + ", " + j + "]");
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
                throw new IllegalArgumentException("�ڵ�ʶ�����. λ��: [" + i + ", " + j + "]");
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
                throw new IllegalArgumentException("�ڵ�ʶ�����. λ��: [" + i + ", " + j + "]");
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
        table = new Result[xx][yy];
        detected = new int[xx][yy];

        for (Result r : list) {
            table[r.getX()][r.getY()] = r;
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
        circuitTable = new int[xx * 3][yy * 3];

        // set all to zero
        for (int i = 0; i < circuitTable.length; i++) {
            for (int j = 0; j < circuitTable[0].length; j++) {
                circuitTable[i][j] = 0;
            }
        }

        // add all the elements
        for (int i = 0; i < xx; i++) {
            for (int j = 0; j < yy; j++) {
                String name = table[i][j].getName();
                String names = name.substring(1);
                if (name.charAt(0) == 'b') {
                    // do nothing
                } else if (name.charAt(0) == 'w') {
                    switch (name.substring(1)) {
                    case "0":
                        circuitTable[i * 3 + 0][j * 3 + 1] = 1; // ��
                        circuitTable[i * 3 + 1][j * 3 + 1] = 1; // ��
                        circuitTable[i * 3 + 2][j * 3 + 1] = 1; // ��
                        break;
                    case "1":
                        circuitTable[i * 3 + 1][j * 3 + 0] = 1; // ��
                        circuitTable[i * 3 + 1][j * 3 + 1] = 1; // ��
                        circuitTable[i * 3 + 1][j * 3 + 2] = 1; // ��
                        break;
                    case "l1":
                        circuitTable[i * 3 + 1][j * 3 + 0] = 1; // ��
                        circuitTable[i * 3 + 1][j * 3 + 1] = 1; // ��
                        circuitTable[i * 3 + 2][j * 3 + 1] = 1; // ��
                        break;
                    case "l2":
                        circuitTable[i * 3 + 0][j * 3 + 1] = 1; // ��
                        circuitTable[i * 3 + 1][j * 3 + 1] = 1; // ��
                        circuitTable[i * 3 + 1][j * 3 + 0] = 1; // ��
                        break;
                    case "l3":
                        circuitTable[i * 3 + 0][j * 3 + 1] = 1; // ��
                        circuitTable[i * 3 + 1][j * 3 + 1] = 1; // ��
                        circuitTable[i * 3 + 1][j * 3 + 2] = 1; // ��
                        break;
                    case "l4":
                        circuitTable[i * 3 + 1][j * 3 + 1] = 1; // ��
                        circuitTable[i * 3 + 1][j * 3 + 2] = 1; // ��
                        circuitTable[i * 3 + 2][j * 3 + 1] = 1; // ��
                        break;
                    case "t1":
                        circuitTable[i * 3 + 1][j * 3 + 0] = 1; // ��
                        circuitTable[i * 3 + 1][j * 3 + 1] = 1; // ��
                        circuitTable[i * 3 + 1][j * 3 + 2] = 1; // ��
                        circuitTable[i * 3 + 2][j * 3 + 1] = 1; // ��
                        break;
                    case "t2":
                        circuitTable[i * 3 + 0][j * 3 + 1] = 1; // ��
                        circuitTable[i * 3 + 1][j * 3 + 0] = 1; // ��
                        circuitTable[i * 3 + 1][j * 3 + 1] = 1; // ��
                        circuitTable[i * 3 + 2][j * 3 + 1] = 1; // ��
                        break;
                    case "t3":
                        circuitTable[i * 3 + 0][j * 3 + 1] = 1; // ��
                        circuitTable[i * 3 + 1][j * 3 + 0] = 1; // ��
                        circuitTable[i * 3 + 1][j * 3 + 1] = 1; // ��
                        circuitTable[i * 3 + 1][j * 3 + 2] = 1; // ��
                        break;
                    case "t4":
                        circuitTable[i * 3 + 0][j * 3 + 1] = 1; // ��
                        circuitTable[i * 3 + 1][j * 3 + 1] = 1; // ��
                        circuitTable[i * 3 + 1][j * 3 + 2] = 1; // ��
                        circuitTable[i * 3 + 2][j * 3 + 1] = 1; // ��
                        break;
                    case "p":
                        circuitTable[i * 3 + 0][j * 3 + 1] = 1; // ��
                        circuitTable[i * 3 + 1][j * 3 + 0] = 1; // ��
                        circuitTable[i * 3 + 1][j * 3 + 1] = 1; // ��
                        circuitTable[i * 3 + 1][j * 3 + 2] = 1; // ��
                        circuitTable[i * 3 + 2][j * 3 + 1] = 1; // ��
                        break;
                    }
                } else {
                    if (detected[i][j] == -1) {
                        circuitTable[i * 3 + 0][j * 3 + 1] = 1; // ��
                        circuitTable[i * 3 + 1][j * 3 + 1] = 1; // ��
                        circuitTable[i * 3 + 2][j * 3 + 1] = 1; // ��
                    } else {
                        circuitTable[i * 3 + 1][j * 3 + 0] = 1; // ��
                        circuitTable[i * 3 + 1][j * 3 + 1] = 1; // ��
                        circuitTable[i * 3 + 1][j * 3 + 2] = 1; // ��
                    }
                }
            }
        }
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
