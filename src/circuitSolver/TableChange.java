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

    public int getvalue() {
        return value;
    }
}

class TableChange {
    private List<Result> list = new ArrayList<Result>();
    private Result[][] table = new Result[100][100];
    private int[][] detected = new int[100][100];
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
            // 直导线在探测过程中仅充当传递节点, 如果探测结束后还有直导线没有被探测, 则直接当作纯直导线处理
            detect(i - iChange, j + jChange, direction);
        } else if (type.equals("w") && direction == 0) {
            // 否则利用try-catch强行探测
            try {
                detect(i, j - 1, 2);
                detect(i, j + 1, 4);
            } catch (IndexOutOfBoundsException e) {
                // e.printStackTrace();
            }

            try {
                detect(i + 1, j, 1);
                detect(i - 1, j, 3);
            } catch (IndexOutOfBoundsException e) {
                // e.printStackTrace();
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
            return;
        }

        detected[i][j] = node;
        return;
    }

    private void generateFile() {
        for (Result r : list) {
            table[r.getX()][r.getY()] = r;
        }

        for (int i = 0; i < xx; i++) {
            for (int j = 0; j < yy; j++) {
                if (table[i][j] == null) {
                    table[i][j] = new Result(i, j, "w", 0);
                    detected[i][j] = -1;
                }
                if (table[i][j].getName().charAt(0) != 'w') {
                    table[i][j].setX(-1);
                    table[i][j].setY(-1);
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
                Result temp = table[i][j];
                if (temp.getName().charAt(0) != 'w') {
                    output += new String(table[i][j].getName() + "\t" + table[i][j].getvalue() + "\t" + temp.getX()
                            + "\t" + temp.getY() + "\n");
                }
            }
        }
    }

    public TableChange(int xx, int yy, List<Result> list) {
        this.list = list;
        this.xx = xx;
        this.yy = yy;

        this.generateFile();

        System.out.println("The node matrix is as below:");
        for (int i = 0; i < xx; i++) {
            for (int j = 0; j < yy; j++) {
                System.out.print(detected[i][j] + "\t");
            }
            System.out.println();
        }
    }

    public String getOutput() {
        return output;
    }
}
